import math

from helpers.Debug import Debug

"""
This class defines some handy functions for various mathematic operations.

@version 0.1
@author Nikolay Pulev, Dimitar Dimitrov
"""
class Math:

	def __init__(): abstract


	"""
	Given a vector as a tuple and an angle in radians, this function calculates
	and returns the roteated coordinates of the vector in the form of a new
	vector (tuple) scaled 'scale' times.
	"""
	def rotate_vec((x, y), angle, scale = 1):
		ang = math.radians(angle)
		# rotate vector by angle with matrix
		#	cos	sin
		#	-sin	cos
		return ((math.cos(ang) * x - math.sin(ang) * y) * scale,
				(math.sin(ang) * x + math.cos(ang) * y) * scale)
	rotate_vec = staticmethod(rotate_vec)


	"""
	Adds two vectors. [v1 - v2 -> result]. Result is tuple.
	"""
	def add_vectors((x1, y1), (x2, y2), scale = 1):
		return (x1 + x2) * scale, (y1 + y2) * scale
	add_vectors = staticmethod(add_vectors)


	"""
	Retuns the differene between two vectors. [v1 - v2 -> result]. Result is
	a tuple
	"""
	def sub_vectors((x1, y1), (x2, y2)):
		return (x1 - x2, y1 - y2)
	sub_vectors = staticmethod(sub_vectors)


	"""
	Calculates the Euclidean distance between two points. Returns a float.
	"""
	def ecl_dist(pt1, pt2):
		return math.sqrt(math.pow(pt1[0] - pt2[0], 2) +
			math.pow(pt1[1] - pt2[1], 2))
	ecl_dist = staticmethod(ecl_dist)


	"""
	Calculates the average of a list of numbers. Returns an intereger.
	"""
	def calc_average_for_numbers(numbers):
		sum_numbers = sum(numbers)
		return int(round(float(sum_numbers)/float(len(numbers))))
	calc_average_for_numbers = staticmethod(calc_average_for_numbers)


	"""
	Calculates the average of a list of vectors. Returns a tuple, (x,y),
	where x and y are integers.
	"""
	def calc_average_for_vectors(vectors):
		sum_x = sum([float(x) for (x,y) in vectors])
		sum_y = sum([float(y) for (x,y) in vectors])
		return (int(round(sum_x/len(vectors))),int(round(sum_y/len(vectors))))
	calc_average_for_vectors = staticmethod(calc_average_for_vectors)


	"""
	Calculates the average of a list of tuples of the type stored in the ball, yellow, and blue caches.
	"""
	def calc_average_for_vectors_cache(vectors):
		sum_x = sum([float(x) for ((x,y),z) in vectors])
		sum_y = sum([float(y) for ((x,y),z) in vectors])
		return ((int(round(sum_x/len(vectors))),int(round(sum_y/len(vectors)))),vectors[len(vectors)-1][1])
	calc_average_for_vectors_cache = staticmethod(calc_average_for_vectors_cache)


	"""
	Calculates the average of a list of vectors, using weights. Returns a
	tuple, (x,y), where x and y are integers. The weights should sum up to 1.0.
	For example, if you have 3 vectors, and thus 3 weights the weights can be
	[0.8,0.1,0.1], [0.5,0.3,0.2], etc.
	"""
	def calc_weighted_average_for_vectors(vectors, weights):
		if len(weights) != len(vectors):
			return Math.calc_average_for_vectors_cache(vectors)
		else:
			index = 0
			weighted_sum_X = 0
			weighted_sum_Y = 0
			try:
				for vector in vectors:
					weighted_sum_X = weighted_sum_X + vector[0][0] * weights[index]
					weighted_sum_Y = weighted_sum_Y + vector[0][1] * weights[index]
					index = index + 1
			except:
				average_X = float(weighted_sum_X) / len(vectors)
				average_Y = float(weighted_sym_Y) / len(vectors)
			else:
				average_X = float(weighted_sum_X) / len(vectors)
				average_Y = float(weighted_sum_Y) / len(vectors)
			return ((int(round(average_X)), int(round(average_Y))),vectors[len(vectors)-1][1])
	calc_weighted_average_for_vectors = staticmethod(calc_weighted_average_for_vectors)


	"""
	Determines the angle between two vectors.
	Always returns a positive number.
	"""
	# TODO: what happens when distance is 0?
	def ang_btw_vecs((x, y), (x2, y2)):
		# round the value 2 digits after the floating point
		distance = round(Math.ecl_dist((x, y), (0, 0)) *
					 Math.ecl_dist((x2, y2), (0, 0)), 2)
		if not distance: return 0
		return math.degrees(math.acos((x * x2 + y * y2) / distance))
	ang_btw_vecs = staticmethod(ang_btw_vecs)


	"""
	Returns a vector in the opposite direction scaled 'scale' times.
	"""
	def invert_vec((x, y), scale = 1):
		return (- x * scale, - y * scale)
	invert_vec = staticmethod(invert_vec)



	"""
	Scales the vector's x and y coordinats with the argument given.
	"""
	def scale_vec((x, y), scale):
		return (x * scale, y * scale)
	scale_vec = staticmethod(scale_vec)

	"""
	Returns a vector, casted to integer values.
	"""
	def int_vec((x, y)):
		return (int(round(x)), int(round(y)))
	int_vec = staticmethod(int_vec)
