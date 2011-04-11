import os, cv

from helpers.Logger import Logger
from __builtin__ import V_SETT

"""
This class abstracts out the mechanism of captiring frames. This allows
the processor to work on other sources of information like photos or videos.
The decision on what type of input is going to be used when the vision system
is operating is determined by a setting in python/src/VisionSettings.py (the
settings file). This can also be overriden by passing aguments to the vision
system's starting script (--no-cam).
Currently it only supports photoes, but videos could be added.
This feature is mainly needed for testing the visual system without the need
of a real world setup.

@version 0.1
@author Nikolay Pulev
"""
class Camera:
	
	def __init__(self, device_num, brigtness = V_SETT.BRIGHTNESS,
			use_cam = V_SETT.USE_CAM):
		self.device_num_ = device_num
		if use_cam:
			self.cap_ = cv.CaptureFromCAM(device_num)
			# invoke script for changing v4l settings only if os is Linux
			if os.sep == '/':
				print 'Trying to fix V4L settings ...'
				os.system(V_SETT.SET_V4L_MOD + ' ' + V_SETT.BRIGHTNESS)
		else:
			Logger.log('Cannot access camera device %d, working with token frames ...'
				% self.device_num_)
			self.cap_ = None
			self.frame_tok_ = cv.LoadImage(V_SETT.FRAME_TOK, 1)


	def get_frame(self):
		if self.cap_:
			return cv.QueryFrame(self.cap_)
		return self.frame_tok_


	def get_cv_cap(self):
			return self.cap_
