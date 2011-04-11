import sys, socket, select, time, helpers

from threading import Thread, Lock
from helpers.Logger import Logger
from helpers.Debug import Debug
from communication.CommProto import CommProto


"""
The vision server's thread is created and started by the vision processor.
Once a connection is established, clients can request information about:

1. (pitch) Coords of the pitch and goals. Send as single messages upon request.
2. (state) Coords of ball, robots (with directions). Will start streaming upon
	a 'start' request, stream can be stopped with a 'stop' request.

The full vision<-->control communication protocol used for this system is
described and implemented in python/src/vision/CommProto.py

@version 0.2
@author Nikolay Pulev
"""
class VisionServer(Thread):

	"""
	This is the constructor. It creates and sets the instance
	variables of the newly created object.
	"""
	def __init__(self, vision_processor, net_open, port, max_requests = 5):
		Thread.__init__(self, None, None, 'vision-processor')
		self.lock_ = Lock()				# create lock for sinchronisation
		self.alive_ = None
		self.v_proc_ = vision_processor	# initialise vision processor

		# create the server socket for listening
		self.srv_sock_ = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		# trun server on the network or on this computer only
		self.ip_ = socket.gethostname() if net_open else '127.0.0.1'
		self.port_ = port
		self.max_req_ = max_requests	# max request in the server queue

		# outgoing peer message queue 
		# { host: [(msg1, time_sent), (msg2, time_sent) ...] }
		self.outgoing_q_ = {}
		self.incoming_q_ = {}
		# outgoing broadcast message queue:
		# [ (msg1, time_sent), (msg2, time_sent) ...]
		self.out_bcst_q_ = []
		self.descriptors_ = [self.srv_sock_]	# a list of connections
		self.peers_ = [(self.ip_, self.port_)]
		self.state_stream_subscribers_ = []	# conns subscribed for state stream


	"""
	This method overrides Thread.run() and is started when the object's start()
	method is invoked.
	"""
	def run(self):
		self.start_server()

		while self.alive_:
			# get_read_ready_socks will block until a socket is ready for
			# reading or return an empty list when the of timeout 1 sec ends.
			for sock in self.get_read_ready_socks(self.descriptors_, 1):
				if sock == self.srv_sock_:	# if server socket has information
					self.accept_new_connection()
				else:	# get data from peer
					size = 10000
					data = b''
					length = 0
					chunk = CHUNK = 4 # bites
					host, port = self.peers_[self.descriptors_.index(sock)]
					while length < size:
						try:
							data_chunk = sock.recv(chunk)
						except:
							break
						if not data_chunk:	# if empty message
							Logger.log("Vision Server: [%s,%d]" % (
								host, port) + ' closed connection ...')
							sock.close()
							# remove sock from all lists
							self.remove_sock(sock)
							break
						data += data_chunk
						length += len(data_chunk)
						chunk = CHUNK - (length % CHUNK)
						if length == 8:
							size = CommProto.decode_chunk(data[4:8])
					if not data_chunk: continue # if las message was empty
					reply = CommProto.decode(data)
					Logger.log('VisionServer: [%s, %d] sent: %s' % (
						host, port, str(reply)))
					self.react_and_reply(sock, host, port, reply)


		# this is executed when thread is dying
		for sock in self.descriptors_:
			sock.close()
		Logger.log("Vision Server: Exiting ...")


	def kill_self(self):
		self.alive_ = False
		return True


	"""
	This function should be invoked only by a peer. It will try to kill the
	processor, which in terms will try to kill the server. An acknowledgement
	is sent that the command is received, but there's no guarantee that it will
	executed. In case it doesnt, the vision system will throw an exception.
	"""
	def kill_processor(self, sock = None):
		if sock and self.send_data(sock, [CommProto.ACK, 0]):
			Logger.log('Vision Server: Shutdown requested, server sent [0, 0]')
		if not self.v_proc_.kill_self():
			self.raise_exception('Vision Processor: Could not kill server!')


	def start_server(self):
		# bind to port at ip
		for i in range(V_SETT.STARTUP_ATTEMPTS)[1:]:
			try:
				print
				Logger.log('Vision Server: Start attempt [%d] ...' % i)
				self.srv_sock_.bind((self.ip_, self.port_))
				# set maximum number of requests in the queue
				self.srv_sock_.listen(self.max_req_)
				self.alive_ = True
				break
			except:
				Debug.print_stacktrace()
				if i >= V_SETT.STARTUP_ATTEMPTS: break
				Logger.log('Vision Server: Could not bind to [%s, %d]' % ( 
					self.ip_, self.port_))
				Logger.log('Vision Server: Retrying in %d seconds' % (
					V_SETT.STARTUP_ATTEMPT_TIMEOUT))
				time.sleep(V_SETT.STARTUP_ATTEMPT_TIMEOUT)

		if not self.alive_:
			Logger.log('Vision Server: Could not connect to ' +
				'[%s, %d]' % (self.ip_, self.port_))
			self.kill_processor()

		Logger.log('Vision Server: Started on [%s, %d]' % (
			self.ip_, self.port_))
		print 'To kill server, press with CTRL+C.'


	def accept_new_connection(self):
		sock, addr = self.srv_sock_.accept()	# get socket
		sock.settimeout(0.5)					# set timeout to 100 millis
		self.descriptors_.append(sock)			# add to connections
		try:
			self.peers_.append(sock.getpeername())
			Logger.log("Vision Server: [%s, %d] opened connection ..." % addr)
		except:
			Logger.log('Vision Server: Socket died and removed on accept ...')


	def send_data(self, sock, msg, mode = CommProto.MODE_ONCE, peer = None):
		with self.lock_:
			try:
				sock.send(CommProto.encode(msg))
				return True
			except:	# connection died, enqueue message if mode = MODE_SAFE
				Debug.print_stacktrace()
				enqueued = ''
				if mode == CommProto.MODE_SAFE:
					if not peer: return False
					print 'SEND FAILED, ENQUEUEING ...'
					if peer[0] in self.outgoing_q_:			# if in queue
						# append to queue
						self.outgoing_q_[peer[0]].append((msg, time.time()))
					else:
						# or create queue
						self.outgoing_q_[peer[0]] = [(msg, time.time())]
					enqueued = ', but saved in queue '
				self.remove_sock(sock)
				Logger.log('Socket died. Messag not sent%s...' % enqueued)
				return False


	def broadcast_to_subscribers(self, state):
		for sock in self.state_stream_subscribers_:
			self.send_data(sock, state)

	
	def	broadcast(self, msg, mode = CommProto.MODE_ONCE):
		if mode == CommProto.MODE_SAFE:
			broadcasted = False
			tm_sent = time.time()
			self.out_bcst_q_.append((msg, tm_sent))
		for sock in self.descriptors_:	# for each connection
			if sock != self.srv_sock_:
				if self.send_data(sock, msg, mode,	# try to send message
						self.peers_[self.descriptors_.index(sock)]):
					broadcasted = True
		if mode == CommProto.MODE_SAFE and broadcasted:
			# delete from broadcast queue
			self.out_bcst_q_.remove((msg, tm_sent))


	def flush_queues(self):
		# flush broadcast messages
		if len(self.out_bcst_q_):
			for message, tm_sent in self.out_bcst_q_:
				broadcasted = False
				# delete if timeout ended
				if time.time() - tm_sent >= V_SETT.MSG_QUEUE_TTL:
					self.out_bcst_q_.remove((message, tm_sent))
					continue
				for sock in self.descriptors_:
					if (sock != self.srv_sock_ and
							self.send_data(sock, message)):
						broadcasted = True
				if broadcasted: self.out_bcst_q_.remove((message, tm_sent))

		if not len(self.outgoing_q_):
			return

		# flush client-specific messages
		for sock in self.descriptors_:
			if sock != self.srv_sock_:
				host = self.peers_[self.descriptors_.index(sock)][0]
				if host in self.outgoing_q_:
					for message, tm_sent in self.outgoing_q_[host]:
						# remove message from queue if timeout ended
						if time.time() - tm_sent >= V_SETT.MSG_QUEUE_TTL:
							self.outgoing_q_.remove((message, tm_sent))
							continue
						Logger.log('Vision Server: Flushing ' +
							'\'%s\' to [%s] ...' % (str(message), host))
						if self.send_data(sock, message):
							self.outgoing_q_[host].remove((message, tm_sent))
					if not len(self.outgoing_q_[host]):
						self.outgoing_q_.pop(host)

		# filter old queued messages of peers who are not currently connected
		for host in set(self.outgoing_q_.keys()) - set(
				[host for host, port in self.peers_]):
			for msg, tm_sent in self.outgoing_q_[host]:
				if time.time() - tm_sent >= V_SETT.MSG_QUEUE_TTL:
					self.outgoing_q_[host].remove((msg, tm_sent))
			if not len(self.outgoing_q_[host]):
				del self.outgoing_q_[host]

		# flush incoming queue
		for msg, tm_recvd in self.incoming_q_:
			if time.time() - tm_recvd >= V_SETT.MSG_QUEUE_TTL:
				self.incoming_q_.remove((msg, tm_recvd))
				continue		
			if self.react_and_reply(msg):
				self.incoming_q_.remove((msg, tm_recvd))
					

	def reset_relationship_with(self, sock):
		"""
		This function is called upon peer request. It clears all previous
		history and information regarding the relationship between this peer and
		the server. For now, it clears the peer's message queue and the server's
		broadcast message queue.
		"""
		host = self.peers_[self.descriptors_.index(sock)][0]		# get host
		if host in self.outgoing_q_: self.outgoing_q_[host] = []	# clear msgs
		self.out_bcst_q_ = []	# clear broadcast msg queue


	def remove_sock(self, sock):
		if sock in self.descriptors_:
			index = self.descriptors_.index(sock)
			self.peers_.pop(index)			# remove from peers
			self.descriptors_.pop(index)	# remove from conn list
		if sock in self.state_stream_subscribers_:
			self.state_stream_subscribers_.remove(sock)


	"""
	This function will return sockets that are ready for reading. It will block
	until at least one socket in the self.descriptors_ is ready.
	"""
	def get_read_ready_socks(self, sockets, timeout = None):
		(ready_for_read, _, _) = self.select(sockets, [], [], timeout)
		return ready_for_read


	def get_write_ready_socks(self, sockets, timeout = None):
		(_, ready_for_write, _) = self.select([], sockets, [], timeout)
		return ready_for_write


	def select(self, read, write, have_expt, timeout):
		while 1:
			try:
				self.flush_queues()
				(s_read, s_write, s_expt) = select.select(read, write, 
					have_expt, timeout)
				return s_read, s_write, s_expt
			except select.error, e:
				Debug.print_stacktrace()
				Logger.log('Vision Server: Currpupted socket, ' +
					'trying to recover ...')
				for sock in self.descriptors_:
					try:
						# timeout 0 to remove delay
						select.select([sock], [], [], 0)
					except select.error, e:
						Debug.print_stacktrace()
						self.descriptors_.remove(sock)
						Logger.log('Socket deleted ...')
						if len(self.descriptors_) == 0:
							Logger.log('Vision Server: It was server socket, ' +
								' restarting ...')
							self.srv_sock_ = socket.socket(socket.AF_INET, 
								socket.SOCK_STREAM)
							self.descriptors_.append(slef.srv_sock_)
							self.start_server()
						break


	def react_and_reply(self, sock, host, port, msg_int_list):
		# decode message according to established protocol
		# min munber of args in a message is 2
		reply = None
		if not msg_int_list or len(msg_int_list) < 2:	# reply with corrupt msg
			reply = [CommProto.LOG, 1, CommProto.LOG_CRPT_MSG]

		elif msg_int_list[0] == CommProto.CTR_PITCH:	# pitch coords, no ackn
			reply = self.v_proc_.get_pitch()
			if not reply:
				msg = (msg_int_list, time.time())
				if host in self.incoming_q_:
					self.incoming_q_[host].append(msg)
				else:
					self.incoming_q_[host] = [msg]
					Logger.log('Vision Server: Message could not be send to ' +
						'[%s, %d] at ' % (host, port) + 'this point, so ' +
						'adding %s to input queue.' % str(msg_int_list))
					return False

		elif msg_int_list[0] == CommProto.CTR_STATE:	# ball & robots coords
			if not sock in self.state_stream_subscribers_:
				with self.lock_:
					self.state_stream_subscribers_.append(sock)

		elif msg_int_list[0] == CommProto.CTR_STOP_STATE:
			if sock in self.state_stream_subscribers_:
				with self.lock_:
					if sock in self.state_stream_subscribers_:
						self.state_stream_subscribers_.remove(sock)
				reply = [CommProto.ACK, 0]

		elif msg_int_list[0] == CommProto.CTR_RESET:	# state coords
			Logger.log('Clearing messaging queue for [%s, %d]' % (
				self.peers_[self.descriptors_.index(sock)]))
			self.reset_relationship_with(sock)			# reset
			reply = [CommProto.ACK, 0]

		elif msg_int_list[0] == CommProto.CTR_ACK:		# state coords
			return True

		else:											# unknown message
			reply = [CommProto.LOG, 1, CommProto.LOG_CRPT_MSG]

		if reply and self.send_data(sock, reply, CommProto.MODE_SAFE,
				(host, port)):
			Logger.log('Vision Server: Replied to [%s, %d]: %s' % (
				host, port, str(reply)))
		return True


	def raise_exception(self, msg):
		Logger.log(msg)
		raise Exception(msg)
		Debug.print_stacktrace()
