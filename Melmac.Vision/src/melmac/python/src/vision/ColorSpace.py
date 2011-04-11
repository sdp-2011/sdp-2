import cv

from helpers import Debug

"""
This class allows easy work with colorspaces. It provides a wrapper to the
OpenCV InRanceS method making it easier to execute this procedure on images.

@version 0.1
@author Nikolay Pulev
"""
class ColorSpace:

	HSV = 0
	RGB = 1
	MAX_HUE = 180
	MAX_SAT = 255
	MAX_VAL = 255
	MAX_RGB = 255
	
	RGB_WHITE = cv.CV_RGB(255, 255, 255)
	RGB_BLACK = cv.CV_RGB(0, 0, 0)
	RGB_RED = cv.CV_RGB(255, 0, 0)
	RGB_BLU = cv.CV_RGB(0, 0, 255)
	RGB_YLW = cv.CV_RGB(255, 255, 0)
	

	def __init__(self, mode):
		if mode == ColorSpace.HSV:
			self.mode_ = ColorSpace.HSV
			self.mx_1_ = 180
		elif mode ==  ColorSpace.RGB:
			self.mode_ = ColorSpace.RGB
		else:
			Debug.raise_exception('ColorSpace: Invalid color mode.')

		self.win_name_ = None
		
		self.mx_2_ = 255
		self.mx_3_ = 255

	def set_trsh_bounds(self, min_bound, max_bound):
		self.mn_1_, self.mn_2_, self.mn_3_ = min_bound
		self.mx_1_, self.mx_2_, self.mx_3_ = max_bound

	def in_range_s(self, im):
		dst = cv.CreateImage(cv.GetSize(im), cv.IPL_DEPTH_8U, 1)
		cv.InRangeS(im, cv.Scalar(self.mn_1_, self.mn_2_, self.mn_3_),
			 cv.Scalar(self.mx_1_, self.mx_2_, self.mx_3_), dst)
		return dst
		

	def get_mode(self):
		return self.mode_


	def set_mn_1(self, mn_1):
		self.mn_1_ = mn_1


	def set_mn_2(self, mn_2):
		self.mn_2_ = mn_2


	def set_mn_3(self, mn_3):
		self.mn_3_ = mn_3


	def set_mx_1(self, mx_1):
		self.mx_1_ = mx_1


	def set_mx_2(self, mx_2):
		self.mx_2_ = mx_2


	def set_mx_3(self, mx_3):
		self.mx_3_ = mx_3
