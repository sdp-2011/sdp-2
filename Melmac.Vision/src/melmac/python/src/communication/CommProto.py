import struct, sys

from  helpers.Debug import Debug


"""
This class describes the messages used in the the communication protocol between
the Java control and Python vision systems.
"""
class CommProto:

	# messages from vision to control
	ACK = 0				# acknowledgement
	PITCH = 1			# sending pitch information (goal & pitch coordinates)
	STATE = 2			# sending robots and ball coordinates (state)
	LOG = 3				# send something to gobal log

	# messages from control to vision
	CTR_ACK = 0
	CTR_PITCH = 1
	CTR_STATE = 2
	CTR_STOP_STATE = 3	# stop sending data about robots and ball coordinates
	CTR_RESET = 4		# reset relationship with peer

	# Log messages
	LOG_CRPT_MSG = 73	# corrupted or unknown message recieved

	# Modes of seding messages
	MODE_SAFE = 0		# makes sure msg is sent to recepient (use queues)
	MODE_ONCE = 1		# send now or discar (no queues used)


	"""
	Encodes a list of integers into a log byte string which will be passed
	as an agrument to socket.send() for the purpose of sending data in accordance
	to the agreed protocol between the vision and control systems.
	"""
	def encode(int_list):
		result = b''				# initialise byte sting message
		for int_arg in int_list:	# encode
			result += struct.pack('!i', int_arg)
		return result
	encode = staticmethod(encode)
	

	"""
	Decodes a list of integers from bytes to a python array of integers and returns
	the list as a result. If the message is corrupt or not according to established
	protocol, None is returned.
	"""
	def decode(msg):
		result = []
		try:
			msg_type = CommProto.decode_chunk(msg[0:4])	# extract message type
			nr_args = CommProto.decode_chunk(msg[4:8])	# extract number of arugments
			result.extend([msg_type, nr_args])		# put them into the result
			offset = 8
			for i in range(nr_args):
				beg = offset + i * 4
				end = beg + 4
				result.append(struct.unpack('!i', msg[beg:end])[0])
			return result
		except:
			Debug.print_stacktrace()
			return None
	decode = staticmethod(decode)


	def decode_chunk(msg):
		try:
			return struct.unpack('!i', msg)[0]
		except:
			Debug.print_stacktrace(sys.exc_info())
			return None
	decode_chunk = staticmethod(decode_chunk)
