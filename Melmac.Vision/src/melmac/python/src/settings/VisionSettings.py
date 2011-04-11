"""
Basic settings for the vision system
"""

import os

# This will print exceptions on the standard output.
DEBUG = True

# Stack trace depth
DEBUG_LEVEL = 5

# Logging functionality
LOGGING = True

# Logfile
LOG_FILE = os.path.join('..', '..', 'log', 'vision_server.log')

# In case --no-cam has been passed, the vision system uses a frame token to
# work on. So this is the path to the frame token that is going to be used.
FRAME_TOK = os.path.join('..', '..', 'frame.jpg')

# Time between server startup trials
STARTUP_ATTEMPT_TIMEOUT = 3 # seconds

# Number of server startup stials
STARTUP_ATTEMPTS = 10 # times

# The port on which the vision server operates
VISION_SRV_PORT = 5000

# Determines whether server operates on the network or only on localhost.
VISION_SRV_NET_OPEN = True # Fasle - on localhost, True - on network

# Timeout of queued messages
MSG_QUEUE_TTL = 10 # seconds

# The maximum number of pending requests the server accepts
MAX_PENDING_REQS = 3

# The maximum size of control system reguest messages in byts
MAX_REQ_MSG_SIZE = 60 # 2 bytes is one character
