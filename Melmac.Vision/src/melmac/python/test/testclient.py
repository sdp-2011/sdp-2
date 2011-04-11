"""
A client written for the purpose of testing the server.
To test, run these in separate terminal windows in the following order:
$ python vision/start_vision.py
$ python testclient.py
"""

import os, sys, socket, __builtin__

def usage():
	print(
	"""
python testclient.py [
	(-h|--host) <ip address or hostanae>
	(-p|--port) <port>
	-s path/to/settings_file.py -h/--help ]
	(-r|--request) <*req_lst> -n <*times>
	
	<*req_lst>	a comma separated list of options from this list:
			1 - request pitch
			2 - start state
			3 - stop state
			4 - reset relationship
			5 - send unknown message
						
			If 1 is present, it will always be issued first and
			<times> number of times.
			If 2 is present, it will always be issues after 1
			if 1 is present	Stream will start and will stop
			after <times>
			messages.
			If 3 is present, server will stop streaming if 1 is
			is present.
			If 4 is present, the server should reset relationship.
	<*times>	the number of times the request is executed
			*applies only to pitch and state requests
	""")
	exit(0)

COMMPROTO_MAX_MSG_SIZE = 84

def recv(sock, size):
	return CommProto.decode(sock.recv(size))

def wait_ackn():
	ackn = None
	while ackn != [CommProto.ACK, 0]:
		print 'Waiting for ACKN ...'
		ackn = recv(sock, COMMPROTO_MAX_MSG_SIZE)
	print 'Received ACKN: ', ackn
	return True

def wait_log():
	log = None
	while log!= [CommProto.LOG, 1, CommProto.LOG_CRPT_MSG]:
		print 'Waiting for LOG message type 73 ...'
		log = recv(sock, COMMPROTO_MAX_MSG_SIZE)
		print 'Received: ', log
	print 'Good!' 
	return True


# Get default settings file
SRC_ROOT = os.path.join(os.getcwd(), os.path.dirname(__file__), '..', 'src')
SETTINGS_FILE = os.path.join(SRC_ROOT, 'settings', 'VisionSettings.py')

# import helpers and settings file
sys.path.append(SRC_ROOT)
from helpers.Importer import Importer
V_SETT = Importer.import_file(SETTINGS_FILE)
V_SETT.LOG_FILE = os.path.join(os.path.dirname(V_SETT.__file__), V_SETT.LOG_FILE)

# hack settings into __builtin__ so that other modules can access it
__builtin__.V_SETT = V_SETT

# import communication's protocol
from communication.CommProto import CommProto


port = ip = n = None
request = []

i = 1 # strat from first argument, 0 argument is scprit's filename
while i < len(sys.argv):
	if sys.argv[i] == '-s':
		i += 1
		SETTINGS_FILE = sys.argv[i]
	elif sys.argv[i] == '-r' or sys.argv[i] == '--request':
		i += 1
		for arg in sys.argv[i].split(','):
			request.append(int(arg))
	elif sys.argv[i] == '-i' or sys.argv[i] == '--host':
		i += 1
		ip = sys.argv[i]
	elif sys.argv[i] == '-p' or sys.argv[i] == '--port':
		i += 1
		port = int(sys.argv[i])
	elif sys.argv[i] == '-n':
		i += 1
		n = int(sys.argv[i])
		print n
	elif sys.argv[i] == '-h' or sys.argv[i] == '--help':
		usage()
	else:
		print 'unknown argument %s' % sys.argv[i]
		usage()
	i += 1

if not len(request):
	print 'Request arguments must be present!'
	usage()

n = n or 1 # set number of requests to 1 if n not provided

if not (port and ip):
	# get port
	port = port or V_SETT.VISION_SRV_PORT
	ip = ip or ''	# '' is localhost

sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)

print "testclient: Connecting to [%s, %d] ..." % (ip, port)
sock.connect((ip, port))

if CommProto.CTR_PITCH in request:
	print 'ASKING PITCH'
	for i in range(n):
		sock.send(CommProto.encode([CommProto.CTR_PITCH, 0]))
		reply = recv(sock, COMMPROTO_MAX_MSG_SIZE)
		print "[%s, %d] Receiving: P[%d]" % (ip, port, i), reply

if CommProto.STATE in request:
	print 'Start streaming ...'
	# start stream
	sock.send(CommProto.encode([CommProto.CTR_STATE, 0]))
	i = 0
	for i in range(n):
		reply = recv(sock, COMMPROTO_MAX_MSG_SIZE)
		print "[%s, %d] Receiving: S " % (ip, port), reply
	# stop stream
	print 'Stopping stream ...'
	sock.send(CommProto.encode([CommProto.CTR_STOP_STATE, 0]))
	wait_ackn()


if 5 in request:
	print 'Sending unknown request ...'
	sock.send(CommProto.encode([8, 0]))
	# server should relply with a 'corrupt message' log entry
	wait_log()

if CommProto.CTR_RESET in request:
	print 'Requesting reset ...'
	sock.send(CommProto.encode([CommProto.CTR_RESET, 0]))
	wait_ackn()

print "Done."
sock.close()
