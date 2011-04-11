"""
This is the launcher of the Vision System. It starts the vision server which
stars the vision processor.
"""

import os, sys, logging, __builtin__, time
from helpers.Importer import Importer


def usage():
	print(
	"""
	python start_vision.py [-s <settings_file.py> --no-cam --show <list*>
				-b <pervrny> ]
	
		-s			load custom settings file
		-b			brightness in percent (e.g 50%)
		--show		display visual feedback according to the <list> which can
					contain the following arguments:
					0 - coloured image feed with all coordinates
					1 - binary image feed of ball with coordinates
					2 - binary image feed of blue robot with coordinates
					3 - binary image feed of yellow robot with coordinates
					4 - binary image of region near yellow and blue robots

		--no-cam	use an image instead of live camera feed


	N.B. In case '-s' is supplied to load a different configuration file, please
		 note that relative path to settings_file.py will not work with
		 symbolic links. Also, the name of settings_file.py must be changed to
		 something different than VisionSettings.py, otherwise Python will load
		 the default settings file.
	""")


# append helpers package to PYTHONPATH so that each module from now on can
# access ot it.
SRC_ROOT = Importer.get_full_path_from_curr(os.path.dirname(__file__))
sys.path.append(os.path.join(SRC_ROOT, 'helpers'))

# Default settings module, could be overriden by '-s' argument.
SETTINGS_FILE = os.path.join(SRC_ROOT, 'settings', 'VisionSettings.py')
USE_CAM = True
DISPLAY_FEED = []
VISION_SRV_PORT = None
BRIGHTNESS = None

i = 1
while i < len(sys.argv):
	if sys.argv[i] == '-s':
		i += 1
		# argument path is relative to dir of execution, so no changes needed
		SETTINGS_FILE = get_full_path_from_curr(sys.argv[i])

	elif sys.argv[i] == '-p' or sys.argv[i] == '--port':
		i += 1
		# argument path is relative to dir of execution, so no changes needed
		VISION_SRV_PORT = int(sys.argv[i])

	elif sys.argv[i] == '--show':
		i += 1
		# split argument list
		for arg in sys.argv[i].split(','):
			DISPLAY_FEED.append(int(arg))	# append each argument

	elif sys.argv[i] == '-b' or sys.argv[i] == '--brightness':
		i += 1
		# set the brightness of the image(done through the modv4l.sh script)
		BRIGHTNESS = sys.argv[i]

	elif sys.argv[i] == '--no-cam':
		USE_CAM = False

	else:
		print 'unknown argument %s' % sys.argv[i]
		usage()
		exit(1)
	i += 1

# import settings module 
V_SETT = Importer.import_file(SETTINGS_FILE)

if VISION_SRV_PORT:
	V_SETT.VISION_SRV_PORT = VISION_SRV_PORT

# Prepend module path to all paths in it so that they can become full paths.
mod_path = os.path.dirname(V_SETT.__file__)
V_SETT.FRAME_TOK = os.path.join(mod_path, V_SETT.FRAME_TOK)
V_SETT.LOG_FILE = os.path.join(mod_path, V_SETT.LOG_FILE)
V_SETT.USE_CAM = USE_CAM			# default value is True
V_SETT.DISPLAY_FEED = DISPLAY_FEED	# add dsiplay options to settings
V_SETT.SET_V4L_MOD = os.path.join(SRC_ROOT, 'bash', 'modv4l.sh')
V_SETT.BRIGHTNESS = BRIGHTNESS or '50%'		# set brightness

# To make the updated settings module accessible from now on, it is hacked into
# the __builtin__ module, and add the module there. __builtin__ is accessible
# from all modules from now on.
__builtin__.V_SETT = V_SETT

# Set up logging functionality.
logging.basicConfig(filename = V_SETT.LOG_FILE, level = logging.DEBUG)

# Create server log file if it does not exist.
if not os.path.isfile(V_SETT.LOG_FILE):
	log = open(V_SETT.LOG_FILE, 'w')
	log.close()

from vision.VisionProcessor import VisionProcessor

try:
	processor = VisionProcessor()
	processor.run()
except KeyboardInterrupt:
	print
	print 'Keyboard Interrupt! Terminating ...'
	processor.kill_self()
	time.sleep(1) # wait for the server to finish
	exit(0)
