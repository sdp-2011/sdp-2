import cv

from helpers.Math import Math
from vision.ColorSpace import ColorSpace


"""
This class encomapasses the vision system's graphical user interface. It uses
OpenCV builtin functions to create and destroy windows and window components.
"""
class VisionGUI:

	WIN_COLOR = 'colored feed'
	WIN_RED_BIN = 'red binary image'
	WIN_BLU_BIN = 'blu binary image'
	WIN_YLW_BIN = 'ylw binary image'
	
	# TODO: Change integer values with these constancs all over the application
	COLOR_FEED = 0
	RED_BIN_FEED = 1
	BLU_BIN_FEED = 2
	YLW_BIN_FEED = 3
	GUI_SLIDERS = 4

	def __init__(): abstract


	def create_gui(red_color, blu_color, ylw_color):
		if 0 in V_SETT.DISPLAY_FEED:
			cv.NamedWindow(VisionGUI.WIN_COLOR, 1)

		if 1 in V_SETT.DISPLAY_FEED:
			cv.NamedWindow(VisionGUI.WIN_RED_BIN, 1)
			if 4 in V_SETT.DISPLAY_FEED:
				VisionGUI.atch_trackbars_to_win(
					VisionGUI.WIN_RED_BIN, red_color)
			else:
				VisionGUI.deatch_trackbars(
					VisionGUI.WIN_RED_BIN)

		if 2 in V_SETT.DISPLAY_FEED:
			cv.NamedWindow(VisionGUI.WIN_BLU_BIN, 1)
			if 4 in V_SETT.DISPLAY_FEED:
				VisionGUI.atch_trackbars_to_win(
					VisionGUI.WIN_BLU_BIN, blu_color)
			else:
				VisionGUI.deatch_trackbars(VisionGUI.WIN_BLU_BIN)

		if 3 in V_SETT.DISPLAY_FEED:
			cv.NamedWindow(VisionGUI.WIN_YLW_BIN, 1)
			if 4 in V_SETT.DISPLAY_FEED:
				VisionGUI.atch_trackbars_to_win(
					VisionGUI.WIN_YLW_BIN, ylw_color)
			else:
				VisionGUI.deatch_trackbars(VisionGUI.WIN_YLW_BIN)
	create_gui = staticmethod(create_gui)


	def display_visual_feedback(img,
				red_bin_img, blu_bin_img, ylw_bin_img,
				(bal_center, blu_center, ylw_center, blu_dir, ylw_dir),
				(l_goal_t_, l_goal_b_, r_goal_t_, r_goal_b_),
				(pitch_tl_, pitch_bl_, pitch_tr_, pitch_br_),
					(trust_bal, trust_blu, trust_ylw),
				(bal_vel, blu_vel, ylw_vel)):	

		if 0 in V_SETT.DISPLAY_FEED:	
			# display robot and ball centers
			# Ball
			cv.Circle(img, bal_center, 4, ColorSpace.RGB_BLACK, 2)
			# ylw velocity
			cv.Line(img, bal_center, Math.add_vectors(bal_center, 
				Math.int_vec(Math.scale_vec(bal_vel, 1/60.0))),
				ColorSpace.RGB_WHITE, 1, cv.CV_AA)	

			# Blue
			left = Math.rotate_vec(blu_dir, -90)
			left_point = Math.add_vectors(left, blu_center)
			cv.Line(img, Math.int_vec(Math.add_vectors(left_point,  blu_dir)),
				Math.int_vec(Math.sub_vectors(left_point, blu_dir)), 1)
			right_point = Math.add_vectors(Math.invert_vec(left), blu_center)
			cv.Line(img, Math.int_vec(Math.add_vectors(right_point,  blu_dir)),
				Math.int_vec(Math.sub_vectors(right_point, blu_dir)), 1)
			cv.Circle(img, blu_center, 4, ColorSpace.RGB_BLU, -2)
			cv.Circle(img, blu_center, 20, ColorSpace.RGB_BLACK, 1)
			# blue_dir
			cv.Line(img, blu_center, Math.add_vectors(blu_center, blu_dir),
				ColorSpace.RGB_BLACK, 1, cv.CV_AA)	
			# blu velocity
			cv.Line(img, blu_center, Math.add_vectors(blu_center, 
				Math.int_vec(Math.scale_vec(blu_vel, 1/60.0))),
				ColorSpace.RGB_WHITE, 1, cv.CV_AA)	

			# Yellow
			left = Math.rotate_vec(ylw_dir, -90)
			left_point = Math.add_vectors(left, ylw_center)
			cv.Line(img, Math.int_vec(Math.add_vectors(left_point,  ylw_dir)),
				Math.int_vec(Math.sub_vectors(left_point, ylw_dir)), 1)
			right_point = Math.add_vectors(Math.invert_vec(left), ylw_center)
			cv.Line(img, Math.int_vec(Math.add_vectors(right_point,  ylw_dir)),
				Math.int_vec(Math.sub_vectors(right_point, ylw_dir)), 1)
			cv.Circle(img, ylw_center, 4, ColorSpace.RGB_YLW, -2)
			cv.Circle(img, ylw_center, 20, ColorSpace.RGB_BLACK, 1)
			# ylw_dir
			cv.Line(img, ylw_center, Math.add_vectors(ylw_center, ylw_dir),
				ColorSpace.RGB_BLACK, 1, cv.CV_AA)
			# ylw velocity
			cv.Line(img, ylw_center, Math.add_vectors(ylw_center, 
				Math.int_vec(Math.scale_vec(ylw_vel, 1/60.0))),
				ColorSpace.RGB_WHITE, 1, cv.CV_AA)	

			# display goal lines
			cv.Circle(img, l_goal_t_, 1, ColorSpace.RGB_WHITE, 2)
			cv.Circle(img, l_goal_b_, 1, ColorSpace.RGB_WHITE, 2)
			cv.Circle(img, r_goal_t_, 1, ColorSpace.RGB_WHITE, 2)
			cv.Circle(img, r_goal_b_, 1, ColorSpace.RGB_WHITE, 2)

			# display pitch 
			cv.Circle(img, pitch_tl_, 1, ColorSpace.RGB_WHITE, 2)
			cv.Circle(img, pitch_bl_, 1, ColorSpace.RGB_WHITE, 2)
			cv.Circle(img, pitch_tr_, 1, ColorSpace.RGB_WHITE, 2)
			cv.Circle(img, pitch_br_, 1, ColorSpace.RGB_WHITE, 2)

			# show image
			cv.ShowImage(VisionGUI.WIN_COLOR, img)

		# display binary images if parameters given
		if 1 in V_SETT.DISPLAY_FEED:
			cv.Circle(red_bin_img, bal_center, 1, ColorSpace.RGB_BLACK, 3)
			cv.Circle(red_bin_img, bal_center, 40, ColorSpace.RGB_WHITE, 1)
			cv.ShowImage(VisionGUI.WIN_RED_BIN, red_bin_img)
		
		if 2 in V_SETT.DISPLAY_FEED:
			cv.Circle(blu_bin_img, blu_center, 1, ColorSpace.RGB_BLACK, 3)
			cv.Circle(blu_bin_img, blu_center, 40, ColorSpace.RGB_WHITE, 1)
			cv.ShowImage(VisionGUI.WIN_BLU_BIN, blu_bin_img)

		if 3 in V_SETT.DISPLAY_FEED:
			cv.Circle(ylw_bin_img, ylw_center, 1, ColorSpace.RGB_BLACK, 3)
			cv.Circle(ylw_bin_img, ylw_center, 40, ColorSpace.RGB_WHITE, 1)
			cv.ShowImage(VisionGUI.WIN_YLW_BIN, ylw_bin_img)

		cv.WaitKey(1000/35)
	display_visual_feedback = staticmethod(display_visual_feedback)


	def atch_trackbars_to_win(name, color):
		mode = color.get_mode()
		if mode == ColorSpace.HSV:
			v1 = 'H'; v2 = 'S';	v3 = 'V'
			mx_1 = ColorSpace.MAX_HUE
		elif mode == ColorSpace.RGB:
			v1 = 'R'; v2 = 'G';	v3 = 'B'
			mx_1 = ColorSpace.MAX_RGB

		i = 'mi '
		x = 'mx '
		mx_2 = ColorSpace.MAX_SAT 
		mx_3 = ColorSpace.MAX_VAL

		cv.CreateTrackbar(i + v1, name, color.mn_1_ or 0, mx_1, color.set_mn_1)
		cv.CreateTrackbar(i + v2, name, color.mn_1_ or 0, mx_2, color.set_mn_2)
		cv.CreateTrackbar(i + v3, name, color.mn_1_ or 0, mx_3, color.set_mn_3)
		cv.CreateTrackbar(x + v1, name, color.mx_1_ or mx_1, mx_1, color.set_mx_1)
		cv.CreateTrackbar(x + v2, name, color.mx_2_ or mx_2, mx_2, color.set_mx_2)
		cv.CreateTrackbar(x + v3, name, color.mx_3_ or mx_3, mx_2, color.set_mx_3)
	atch_trackbars_to_win = staticmethod(atch_trackbars_to_win)


	def deatch_trackbars(win_name):
			cv.NamedWindow(win_name, 1)
	deatch_trackbars = staticmethod(deatch_trackbars)


	def react_to_event(key):
		pass
	react_to_event = staticmethod(react_to_event)


	def update_gui():
		pass
	update_gui = staticmethod(update_gui)
