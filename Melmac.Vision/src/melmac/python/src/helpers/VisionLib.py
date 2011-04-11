"""
This is library with helper functions for operations done in vision processor.
"""

import cv
import math

from Debug import Debug


"""
Given a contour sequence, this will return the contour with the largest
area.
"""
def find_largest_contour(seq, exclude = None):
	max_area = 0
	largest = None
	try:
		while seq:
			if seq != exclude:
				area = cv.ContourArea(seq)
				if max_area < area:
					max_area = area
					largest = seq
			seq = seq.h_next()
		return largest
	except:
		Debug.print_stacktrace()
		return None


"""
Returns centroid of blob given it's image moments.
"""
def get_contour_center(moments):
	spatial_moment10 = cv.GetSpatialMoment(moments , 1, 0)
	spatial_moment01 = cv.GetSpatialMoment(moments, 0, 1)
	area = abs(cv.GetCentralMoment(moments, 0, 0))

	# Ensuring that threre is no division by zero.
	# PLEASE DO NOT TOUCH THIS, DO NOT TRY TO AVOID 0 DIVISION BY ADDING
	# A VALUE TO AREA BELOW, BECAUSE IT WOULD FAIL IN SOME CASES
	area = area or 0.01
	return (spatial_moment10 / area, spatial_moment01 / area)


"""
Only works with irregular shapes. Will not work if objects with equal area
exist (e.g. circles, lines or rectangles drawn with OpenCV).
"""
def contours_area_sort(seq, nr_returned = None):
	if not seq: None
	largest = {}
	while seq:
		largest[cv.ContourArea(seq)] = seq
		seq = seq.h_next()
	sorted_keys = largest.keys()
	sorted_keys.sort()
	nr_returned = nr_returned or len(sorted_keys)
	sorted_keys = sorted_keys[-nr_returned:]
	return [largest[key] for key in sorted_keys]


"""
A blob is (diag_pt1, diag_pt2, centroid)
"""
def get_blob_from_contour(contour):
	bound_rect = cv.BoundingRect(list(contour)) # (x, y, wid, hig)
	# get points of longest diagonal
	pt1 = (bound_rect[0], bound_rect[1])
	pt2 = Math.add_vectors(pt1, (bound_rect[2], bound_rect[3]))
	# get centroid by adding them and scaling the result by 1/2
	# float is used(*2.0*), otherwise scaling will be by 0
	centroid = Math.int_vec(Math.add_vectors(pt1, pt2, 1 / 2.0))
	return pt1, pt2, centroid


"""
Retruns a blob list looking like this [(diag_pt1, diag_pt2, centroid), ...]
"""
def get_blobs_from_seq(contour):
	points = []
	while contour:
		points.append(self.get_blob_from_contour(contour))
		contour = contour.h_next()

	if not len(points):
		return None
	return points 


"""
Given a blob and a list of blobs, this funtion searches through the list for
the closest blob to the fisrt one.
A blob is a tuple of 3 points (lognest_diag_pt1, longest_diag_pt2, centroid)
"""
def find_closest_blob(base_blob, blob_list):
	min_len = 100000
	nearest = None
	for blob in blob_list:
		length = Math.ecl_dist(base_blob[2], blob[2])
		if length < min_len:
			min_len = length
			nearest = blob
	return nearest


def find_largest_blob(blob_list):
	# This is not an accurate calculation.
	max_len = -1 
	largest = None
	for blob in blob_list:
		length = Math.ecl_dist(blob[0], blob[1])
		if length > max_len:
			max_len = length
			largest = blob
	return largest


"""
This method returns the exact angle of the robot orientation using image
moments. However, near the bisecting lines of the four quadrants, the angle
can be the exact opposite of the real one.
"""
def direction_from_moments(moments):
	cmoment11 = cv.GetNormalizedCentralMoment(moments, 1, 1)
	cmoment20 = cv.GetNormalizedCentralMoment(moments, 2, 0)
	cmoment02 = cv.GetNormalizedCentralMoment(moments, 0, 2)
	cmoment30 = cv.GetNormalizedCentralMoment(moments, 3, 0)
	cmoment03 = cv.GetNormalizedCentralMoment(moments, 0, 3)

	try:
		orientation = 1.0 / 2.0 * math.atan(
			2.0 * cmoment11 / (cmoment20 - cmoment02))
		orientation = math.degrees(orientation)
	except:
		orientation = 1.0 / 0.01

	if cmoment11 == 0.0 and (cmoment20 - cmoment02) < 0.0:
		orientation = orientation + 90.0
	elif cmoment11 > 0.0 and (cmoment20 - cmoment02) < 0.0:
		orientation = orientation + 90.0
	elif cmoment11 > 0.0 and (cmoment20-cmoment02) == 0.0:
		orientation = orientation + 45.0
	elif cmoment11 > 0.0 and (cmoment20-cmoment02) > 0.0:
		orientation = orientation
	elif cmoment11 == 0.0 and (cmoment20 - cmoment02) == 0.0:
		orientation = orientation
	elif cmoment11 < 0.0 and (cmoment20 - cmoment02) > 0.0:
		orientation = orientation
	elif cmoment11 < 0.0 and (cmoment20-cmoment02) == 0.0:
		orientation = orientation - 45.0
	elif cmoment11 < 0.0 and (cmoment20-cmoment02) < 0.0:
		orientation = orientation - 90.0
	elif cmoment11 == 0.0 and (cmoment20 - cmoment02) > 0.0:
		orientation = orientation - 90.0

	try:
		skew_x = cmoment30 / (cmoment20**(3.0 / 2.0))
	except:
		skew_x = cmoment30 / 0.1
	try:
		skew_y = cmoment03 / (cmoment02**(3.0 / 2.0))
	except:
		skew_y = cmoment03 / 0.1

	if orientation >= (- 45.0) and orientation <= 45.0:
		if skew_x > 0.0:
			orientation = orientation
		elif skew_x < 0.0 and orientation > 0.0:
			orientation = orientation - 180.0
		elif skew_x < 0.0 and orientation < 0.0:
			orientation = orientation + 180.0
		elif skew_x == 0.0:
			if skew_y > 0.0:
				orientation == 90.0
			elif skew_y < 0.0:
				orientation == - 90.0
	elif (orientation <= (- 45.0) and orientation >= (- 90.0)) or (
				orientation >= 45.0 and orientation <= 90.0):
		if skew_y > 0.0 and orientation > 0.0:
			orientation = orientation
		elif skew_y > 0.0 and orientation < 0.0:
			orientation = orientation + 180.0
		elif skew_y < 0.0 and orientation > 0.0:
			orientation = orientation - 180.0
		elif skew_y < 0.0 and orientation < 0.0:
			orientation = orientation
		elif skew_y == 0.0:
			if skew_x > 0.0:
				orientation = 0.0
			elif skew_x < 0.0:
				orientation = 180.0

	if orientation >= (- 90.0) and orientation <= 180.0:
		orientation = orientation + 90.0
	elif orientation >= (- 180.0) and orientation <= (- 90.0):
		orientation = orientation + 450.0
	return orientation
