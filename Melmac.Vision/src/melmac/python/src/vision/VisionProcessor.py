import sys, cv, time

from Camera import Camera
from ColorSpace import ColorSpace

from communication.VisionServer import VisionServer
from communication.CommProto import CommProto

from gui.VisionGUI import VisionGUI

from helpers.Debug import Debug
from helpers.Math import Math
from helpers.Locator import Locator
from helpers.Logger import Logger
from helpers.VisionLib import *

from threading import Lock
from __builtin__ import V_SETT # import vision settings


"""
The vision processor of the Vision system. It performs operations over the
video stream of the camera and updates the current position of the objects
on the field. The processor runs in a separate thread. The vision server
gets information from the processor about object location and sends it over
the network to the control system.

@version 0.1
@author Nikolay Pulev, PetarIvanov, Dimitar Dimitrov
"""
class VisionProcessor:

	def __init__(self):
		self.lock_ = Lock()		# create a lock for this thread
		self.alive_ = True

		# create and initialise server for this processor
		self.server_ = VisionServer(
			self, V_SETT.VISION_SRV_NET_OPEN,
			V_SETT.VISION_SRV_PORT, V_SETT.MAX_PENDING_REQS)

		# obtain frame capture device obj
		self.cam_ = Camera(0)
		self.fps_ = 0

		# flags that will tell wheter data can be trusted (accurate)
		self.trust_bal_ = self.trust_blu_ = self.trust_ylw_ = None
		# these hold robots' and ball's coordinates and direction
		self.bal_center_ = None
		self.blu_robot_center_ = None
		self.ylw_robot_center_ = None
		self.blu_dir_ = self.ylw_dir_ = None
		# these hold pitcj and goal coordinates
		self.l_goal_t_ = self.l_goal_b_ = self.r_goal_t_ = self.r_goal_b_ = None
		self.pitch_tl_ = self.pitch_bl_ = self.pitch_tr_ = self.pitch_br_ = None

		# create colour objects and set their threshold values
		self.red_color_ = ColorSpace(ColorSpace.HSV)
		self.red_color_.set_trsh_bounds((0, 80, 80), (10, 255, 255)) # pitch 2
		self.blu_color_ = ColorSpace(ColorSpace.HSV)
		self.blu_color_.set_trsh_bounds((92, 132, 199), (180, 255, 255))
		self.ylw_color_ = ColorSpace(ColorSpace.RGB)
		self.ylw_color_.set_trsh_bounds((0, 0, 0), (255, 255, 255))


	"""
	This is vision processor's main loop.
	"""
	def run(self):
		self.server_.start()	# start up communication server
		self.perform_homography()
		VisionGUI.create_gui(self.red_color_, self.blu_color_, self.ylw_color_)

		# Initialise some value in the beginning
		prev_bal_center = (0, 0)
		prev_blu_center = (0, 0)
		prev_ylw_center = (0, 0)

		# should be safe, as during the first frame method should accurately
		# determine direction, if no direction can be determined, than
		# no object is present(or thresholding is broken), so doesn't matter
		prev_blu_dir = (0, -20)
		prev_ylw_dir = (0, -20)

		prev_time = time.time()
		frame_count = 0
		one_sec = 0

		while self.alive_:
			try:
				start = time.time()
				(prev_bal_center, prev_blu_center, prev_ylw_center,
					prev_blu_dir, prev_ylw_dir) = self.detect_objects(
						prev_bal_center, prev_blu_center, prev_ylw_center,
						 prev_blu_dir, prev_ylw_dir)

				# update frame rate
				frame_count += 1
				one_sec += time.time() - start
				if one_sec >= 1.0:
					self.fps_ = frame_count
					frame_count = 0
					one_sec = one_sec % 1.0

			except:
				Debug.print_stacktrace()
				self.kill_self()
				time.sleep(1) # wait for server to finish
				print
		Logger.log('Vision Processor: Exiting ...')


	def kill_self(self):
		self.kill_server()
		self.alive_ = False
		return True


	def kill_server(self):
		if self.server_.kill_self():
			return True	
		raise Exception('Vision Processor:','Could not kill server!')


	def get_pitch(self):
		if not (self.pitch_tl_ and self.pitch_bl_ and self.pitch_tr_ and
				self.pitch_br_ and self.l_goal_t_ and self.l_goal_b_ and
				self.l_goal_b_ and self.r_goal_t_ and self.r_goal_b_):
				return None
		# TODO: Hardcoded, correct as soon as possible
		with self.lock_:	# acquire lock, release afterwards
			# pitch width and height
			p_wdt = self.pitch_tr_[0] - self.pitch_tl_[0]
			p_hgt = self.pitch_bl_[1] - self.pitch_tl_[1]
			# goalas' Y coordinate
			tl_goal_Y_ = self.l_goal_t_[1] - self.pitch_tl_[1]
			bl_goal_Y_ = self.l_goal_b_[1] - self.pitch_tl_[1]
			tr_goal_Y_ = self.r_goal_t_[1] - self.pitch_tl_[1]
			br_goal_Y_ = self.r_goal_b_[1] - self.pitch_tl_[1]

		# message to be encoded is <msg_code><nr_args><arg1><arg2>...
		return [CommProto.PITCH, 6, p_wdt, p_hgt,
				tl_goal_Y_, bl_goal_Y_, tr_goal_Y_, br_goal_Y_]


	def get_state(self):
		bal_X, bal_Y = Math.sub_vectors(self.bal_center_, self.pitch_tl_)
		blu_X, blu_Y = Math.sub_vectors(
			self.blu_robot_center_, self.pitch_tl_)
		ylw_X, ylw_Y = Math.sub_vectors(
			self.ylw_robot_center_, self.pitch_tl_)
		blu_dir_X, blu_dir_Y = self.blu_dir_
		ylw_dir_X, ylw_dir_Y = self.ylw_dir_
		trust_bal = int(round(self.trust_bal_))
		trust_blu = int(round(self.trust_blu_))
		trust_ylw = int(round(self.trust_ylw_))
		bal_velocity_X, bal_velocity_Y = self.bal_vel_
		blu_velocity_X, blu_velocity_Y = self.blu_vel_
		ylw_velocity_X, ylw_velocity_Y = self.ylw_vel_

		# message to be encoded is <msg_code><nr_args><arg1><arg2>...
		return [CommProto.STATE, 19, bal_X, bal_Y, blu_X, blu_Y, ylw_X, ylw_Y,
			blu_dir_X, blu_dir_Y, ylw_dir_X, ylw_dir_Y,
			trust_bal, trust_blu, trust_ylw, bal_velocity_X, bal_velocity_Y,
			blu_velocity_X, blu_velocity_Y, ylw_velocity_X, ylw_velocity_Y]


	def perform_homography(self):
		# TODO: get a new frame
		# TODO: these are hardcoded, use real homography procedures
		self.pitch_tl_ = (20, 25)
		self.pitch_bl_ = (20, 320)
		self.pitch_tr_ = (600, 25)
		self.pitch_br_ = (600, 320)

		self.l_goal_t_ = (20, 100)
		self.l_goal_b_ = (20, 250)
		self.r_goal_t_ = (600, 100)
		self.r_goal_b_ = (600, 250)

		# calculate distortion matrix, for barrel effect correction
		ilist = [	[600.95880126953125, 0, 330.65557861328125], 
			[0.0, 600.8642578125, 266.62376403808594],
			[0.0, 0.0, 1.0]]
		dlist = [	-0.3258521556854248, 0.19688290357589722,
			-0.0078322244547307491, -0.0044014849700033665]
		self.imat_ = cv.CreateMat(3, 3, 1111638021)
		self.dmat_ = cv.CreateMat(4, 1, 1111638021)

		for i in range(3):
			for j in range(3):
				self.imat_[i,j] = ilist[i][j]
			for i in range(4):
				self.dmat_[i,0] = dlist[i]


	def detect_objects(self, prev_bal_center, prev_blu_center, prev_ylw_center,
			prev_blu_dir, prev_ylw_dir):
		# get camera or dummy frame and unidstort
		img = self.cam_.get_frame()
		# Size of frame is 640x480, so cut (50, 80) from the left to remove
		# the distortion. Usage: cv.SubRect(x, y, width, height)
		# TODO: Use homography info, current crop is hadrcoded
		img = cv.GetSubRect(img, (10, 70, 620, 340))	# crop
		size = cv.GetSize(img)

		blu_dir = ylw_dir = None
		bal_center = blu_center = ylw_center = None
		trust_bal = trust_ylw = trust_blu = True

		# create and HSV and RGB iamge for later use
		hsv_img = cv.CreateImage(size, cv.IPL_DEPTH_8U, 3)
		cv.CvtColor(img, hsv_img, cv.CV_BGR2HSV)
		rgb_img = cv.CreateImage(size, cv.IPL_DEPTH_8U, 3)
		cv.CvtColor(img, rgb_img, cv.CV_BGR2RGB)

		# detect ball using hsv_img
		red_bin_img, bal_center = self.detect_ball(hsv_img,
			(3, 3), 3, (3, 3), 3)
		if not bal_center:
			bal_center = prev_bal_center
			trust_bal = False

		# detect blue robot using hsv_img
		blu_trsh = self.blu_color_.in_range_s(hsv_img)			# threshold
		erd_mat = (1, 1); erd = 1; dil_mat = (2, 2); dil = 5	# erode/dilate
		cv.Erode(blu_trsh, blu_trsh, cv.CreateStructuringElementEx(
				erd_mat[0], erd_mat[1], 0, 0, 0), erd)
		cv.Dilate(blu_trsh, blu_trsh, cv.CreateStructuringElementEx(
				dil_mat[0], dil_mat[1], 0, 0, 0), dil)
		cv.Erode(blu_trsh, blu_trsh, cv.CreateStructuringElementEx(
			erd_mat[0] * 6, erd_mat[1] * 6, 0, 0, 0), erd)

		blu_bin_img, blu_center, blu_dir, b_ang = self.detect_robot(blu_trsh)

		# detect yellow robot using rgb_img and invert
		ylw_trsh = self.ylw_color_.in_range_s(hsv_img)	# threshold
		cv.Not(ylw_trsh, ylw_trsh)						# invert
		erd_mat = (1, 1); erd = 3						# erode
		cv.Erode(ylw_trsh, ylw_trsh, cv.CreateStructuringElementEx(
			erd_mat[0] * 2, erd_mat[1] * 2, 0, 0, 0), 2)

		ylw_bin_img, ylw_center, ylw_dir, y_ang = self.detect_robot(ylw_trsh)

		# fix values if current data cannot be trusted
		# ball
		if not prev_bal_center:
			prev_bal_center = bal_center
			trust_bal = False
		if trust_bal and bal_center: prev_bal_center = bal_center

		# blue robot
		if not blu_center:
			blu_center = prev_blu_center
			blu_dir = prev_blu_dir
		else:
			prev_blu_center = blu_center
			prev_blu_dir = blu_dir
		if not b_ang: b_ang = -1

		# yellow robot
		if not ylw_center:
			ylw_center = prev_ylw_center
			ylw_dir = prev_ylw_dir
		else:
			prev_ylw_center = ylw_center
			preb_ylw_dir = ylw_dir
		if not y_ang: y_ang = -1

		# fix coordinates because of difference between thresholded image
		# and real image
		bal_center = Math.add_vectors(bal_center, (8, 8))
		blu_center = Math.add_vectors(blu_center, (6, 7))
		# It seems yellow does not need correction anymore
		# ylw_center = Math.add_vectors(ylw_center, (6, 7))

		sys.stdout.write('\b' * 106 +
		'BAL (%-4.4s,%-4.4s) BLU (%-4.4s,%-4.4s,%-4.4s)' % (
			str(bal_center[0]) if trust_bal else 'X',
			str(bal_center[1]) if trust_bal else 'X',
			str(blu_center[0]) if trust_blu else 'X',
			str(blu_center[1]) if trust_blu else 'X',
			str(int(round(b_ang))) if trust_blu else 'X') +
		' YLW (%-4.4s,%-4.4s,%-4.4s) ' % (
			str(ylw_center[0]) if trust_ylw else 'X',
			str(ylw_center[1]) if trust_ylw else 'X',
			str(int(round(y_ang))) if trust_ylw else 'X'
		) +
		'FPS: %d     ' % self.fps_)	

		# adding to cache
		Locator.add_data_tuple_to_ball_cache((bal_center, time.time()))
		Locator.add_data_tuple_to_yellow_cache((ylw_center, time.time()))
		Locator.add_data_tuple_to_blue_cache((blu_center, time.time()))

		# adding to smoothed caches
		Locator.add_data_tuple_to_smoothed_ball_cache((bal_center, time.time()))
		Locator.add_data_tuple_to_smoothed_yellow_cache((ylw_center, time.time()))
		Locator.add_data_tuple_to_smoothed_blue_cache((blu_center, time.time()))

		# checking for huge changes in velocity
		previous_ball_frame, last_ball_frame = Locator.simple_get_members(Locator.ball_cache,1)
		previous_blu_frame, last_blu_frame = Locator.simple_get_members(Locator.blue_cache,1)
		previous_ylw_frame, last_ylw_frame = Locator.simple_get_members(Locator.yellow_cache,1)

		if Math.ecl_dist(previous_ball_frame[0], last_ball_frame[0]) > 3:
			bal_vel = Locator.calc_velocity_vector_ball(1)
		else:
			bal_vel = Locator.calc_velocity_vector_ball(10)
		if Math.ecl_dist(previous_blu_frame[0], last_blu_frame[0]) > 3:
			blu_vel = Locator.calc_velocity_vector_blue(1)
		else:
			blu_vel = Locator.calc_velocity_vector_blue(10)
		if Math.ecl_dist(previous_ylw_frame[0], last_ylw_frame[0]) > 3:
			ylw_vel = Locator.calc_velocity_vector_yellow(1)
		else:
			ylw_vel = Locator.calc_velocity_vector_yellow(10)
		
		# update centroids
		bal_center = self.bal_center_ = Locator.smoothed_ball_cache[
			len(Locator.smoothed_ball_cache)-1][0]
		blu_center = self.blu_robot_center_ = Locator.blue_cache[
			len(Locator.smoothed_blue_cache)-1][0]
		ylw_center = self.ylw_robot_center_ = Locator.yellow_cache[
			len(Locator.smoothed_yellow_cache)-1][0]

		# update direction vecotrs
		self.blu_dir_ = blu_dir
		self.ylw_dir_ = ylw_dir

		# update 'trust' variables
		# TODO: Revew these, they may no longer be needed
		self.trust_bal_ = trust_bal
		self.trust_blu_ = trust_blu
		self.trust_ylw_ = trust_ylw

		# update velocities from cache
		self.bal_vel_ = bal_vel
		self.blu_vel_ = blu_vel
		self.ylw_vel_ = ylw_vel

		# send information to data stream subscribers 
		self.server_.broadcast_to_subscribers(self.get_state())

		# display visula feedback if there is something to display
		if len(V_SETT.DISPLAY_FEED):
			VisionGUI.display_visual_feedback(img,
				red_bin_img, blu_bin_img, ylw_bin_img,
				(bal_center, blu_center, ylw_center, blu_dir, ylw_dir),
				(self.l_goal_t_, self.l_goal_b_,
					self.r_goal_t_, self.r_goal_b_),
				(self.pitch_tl_, self.pitch_bl_,
					self.pitch_tr_, self.pitch_br_),
					(trust_bal, trust_blu, trust_ylw),
					(bal_vel, blu_vel, ylw_vel))

		return (prev_bal_center, prev_blu_center, prev_ylw_center,
					prev_blu_dir, prev_ylw_dir)


	def detect_ball(self, hsv_img, erd_mat, erd, dil_mat, dil):
		size = cv.GetSize(hsv_img)
		# colours on pitch2 (note conversion is from BGR2HSV not RGB2HSV!)
		trsh_im = self.red_color_.in_range_s(hsv_img)

		cv.Dilate(trsh_im, trsh_im,
			cv.CreateStructuringElementEx(dil_mat[0], dil_mat[1], 0, 0, 0), dil)
		cv.Erode(trsh_im, trsh_im,
			cv.CreateStructuringElementEx(erd_mat[0], erd_mat[1], 0, 0, 0), erd)

		tmp_im = cv.CreateImage(size, cv.IPL_DEPTH_8U, 1)
		cv.Copy(trsh_im, tmp_im)
		largest = find_largest_contour(cv.FindContours(
			tmp_im, cv.CreateMemStorage(0),
			cv.CV_RETR_EXTERNAL, cv.CV_CHAIN_APPROX_NONE))
		if not largest: return trsh_im, None
		return trsh_im, Math.int_vec(get_contour_center(cv.Moments(largest)))


	def detect_robot(self, trsh_im):
		# angle might not be accurate, but always determines a line
		# that is collinear to the longest part of the T on the robot plate.
		t_contour, t_center, ang = self.detect_robot_via_moments(trsh_im)
		if not (t_center and t_contour):
			return trsh_im, None, None, None
		# create dir vec (may be wrong when error_expected() returns True)
		# pointing upwards (systems 0 degree) and rotate it by the angle.
		t_dir = Math.int_vec(Math.rotate_vec((0, -20), ang))
		trust_dir = True	# lets assume this is the correct vector.
		
		# if mistake possible(robot orientation in danger zone), perform check
		if self.dir_error_expected(ang, 10):
			# Try to get the direction of the back using
			# get_back_dir(centroid, radius, supposed_direction_of_T,
			#	line_thikness, thresholded_bin_image)
			t_bck_dir = self.get_back_dir(t_contour, t_center, 20, t_dir, 12,
				cv.GetSize(trsh_im))
			# if the vectors are pointing relatively in one direction
			# there is an error, so invert vector
			if t_bck_dir and Math.ang_btw_vecs(t_dir, t_bck_dir) < 90:
				t_dir = Math.invert_vec(t_dir)
		return trsh_im, t_center, t_dir, ang


	"""
	To solve the orientation problem, a thick black line (a bit thinner
	than the horizontal width of the hat of the T) is drawn in order to
	slplit the T in two (along) and delete the base of the T. This way,
	two small blobs (the tipls of the hat of the T) will remain. Then
	a vector between their midpoint and the centroid of the T is found.
	The angle between the newly foud vector and the direction vector of
	the robot (obtained with 'detect_robot_via_moments') is checked. If
	it is less then 90 degrees, the two vectors point relatively in the
	same direction and there is an error (the direction vector points
	twards tha hat of the T), otherwise, the vector is correct.
	"""
	def get_back_dir(self, t_contour, centroid, rad, t_dir, thickness, size):
		roi = self.draw_contour(t_contour, size)
		#roi = self.get_circular_roi(centroid, rad, bin_im)
		# draw a thick line from tip of 'dir_vec' to tip of the inverted	
		# 'dir_vec'. This is done, to ensure that the 'stand' of the T is
		# removed and it's hat is split in two.
		cv.Line(roi, Math.add_vectors(t_dir, centroid),
			Math.add_vectors(Math.invert_vec(t_dir), centroid),
			ColorSpace.RGB_BLACK, thickness)

		tmp_im = cv.CreateImage(cv.GetSize(roi), cv.IPL_DEPTH_8U, 1)
		cv.Copy(roi, tmp_im)	# create image for FindContours
		seq = cv.FindContours(tmp_im, cv.CreateMemStorage(0),
			cv.CV_RETR_EXTERNAL, cv.CV_CHAIN_APPROX_NONE)

		# sort contours from ROI and try to take two with the biggest area
		contours = contours_area_sort(seq)[-2:]
		if not contours : return None
		nr_contours = len(contours)
		if nr_contours == 2:	# if two available, get vec to midpoint
			pt1 = get_contour_center(cv.Moments(contours[0]))
			pt2 = get_contour_center(cv.Moments(contours[1]))
			mid = Math.add_vectors(pt1, pt2, 1 / 2.0)
		elif nr_contours:		# if only one, retun it as mid point
			mid = get_contour_center(cv.Moments(contours[0]))
		# no contours found, check failed, get prev value
		else: return None

		mid = Math.int_vec(mid)
		dir_vec = Math.sub_vectors(mid, centroid)

		# show vector
		cv.Line(roi, centroid, Math.add_vectors(centroid, dir_vec),
			ColorSpace.RGB_WHITE, 1)
		cv.ShowImage('w', roi)
		return dir_vec	# return the back direction vec


	"""
	r point, a radius and a binary image, this function, removes
	all binary objects out of circle. A new image is returned and the arguments
	are not changed.
	"""
	def get_circular_roi(self, center, rad, src):
		# create an empty one-channel image
		outside = cv.CreateImage(cv.GetSize(src), cv.IPL_DEPTH_8U, 1)
		cv.SetZero(outside) # make sure nothing is in the image
		# create white (filled) circle, by passing -1
		cv.Circle(outside, center, rad, ColorSpace.RGB_WHITE, -1)
		# invert to create the circular window
		cv.Not(outside, outside)
		# Subtract the background from image to get only the are desired ROI
		roi = cv.CreateImage(cv.GetSize(src), cv.IPL_DEPTH_8U, 1)
		cv.Sub(src, outside, roi)
		return roi


	"""
	Given a contour and a size parameter, this function draws the contour on
	an empty image with the specified size.
	"""
	def draw_contour(self, contour, size):
		# create an empty one-channel image
		contour_im = cv.CreateImage(size, cv.IPL_DEPTH_8U, 1)
		cv.SetZero(contour_im) # make sure nothing is in the image
		cv.DrawContours(contour_im, contour, cv.Scalar(255), cv.Scalar(255), 0,
			cv.CV_FILLED)
		return contour_im


	"""
	Checks whether the supposed robot direction vector is in range of the
	bisecting lines	of the 4 quadrats, where detect_robot_via_moments may
	give erroneus angle.
	"""	
	def dir_error_expected(self, ang, margin):
		quad_ang = ang % 90	# take modulo to simplify angle
		return quad_ang >= 45 - margin and quad_ang <= 45 + margin


	"""
	This obtains robot's coordinates and orientation through moments.
	To understand how moments work, please read Wikipedia page.
	"""
	def detect_robot_via_moments(self, trsh_im):
		# Create a copy of trsh im, so that the parameter is not changed.
		# Needed, because FindContours corrupts it's input image.
		tmp_im = cv.CreateImage(cv.GetSize(trsh_im), cv.IPL_DEPTH_8U, 1)
		cv.Copy(trsh_im, tmp_im)
		largest = find_largest_contour(cv.FindContours(tmp_im,
			cv.CreateMemStorage(0),	cv.CV_RETR_LIST, cv.CV_CHAIN_APPROX_NONE))

		if not largest: return (None, None, None)

		moments = cv.Moments(largest)	# get moments
		centroid = Math.int_vec(get_contour_center(moments))
		orientation = direction_from_moments(moments)
		return largest, centroid, orientation


	def barrel_undistort(self, img):
		try:
			dst = cv.CreateImage(cv.GetSize(img), cv.IPL_DEPTH_8U, 3)
			cv.Undistort2(img, dst, self.imat_, self.dmat_)
			return dst
		except: return None
