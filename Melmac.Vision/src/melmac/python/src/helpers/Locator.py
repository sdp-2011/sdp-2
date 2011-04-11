from helpers.Math import Math

"""
This class defines some handy functions for obtaining more accurate and smoothed
(averaged over several frames) coordinates and velocities.

@version 0.1
@author Dimitar Dimitrov
"""
class Locator:

	# list = [(points as a tuple, time), (points2 as a tuple, time2) ...]
	ball_cache = [((0.0, 0.0), 0.0), ((0.0, 0.0), 0.0)]
	yellow_cache = [((0.0, 0.0), 0.0), ((0.0, 0.0), 0.0)]
	blue_cache = [((0.0, 0.0), 0.0), ((0.0, 0.0), 0.0)]

	ball_cache_max_size = 20
	yellow_cache_max_size = 20
	blue_cache_max_size = 20

	smoothed_ball_cache = [((0.0, 0.0), 0.0), ((0.0, 0.0), 0.0)]
	smoothed_yellow_cache = [((0.0, 0.0), 0.0), ((0.0, 0.0), 0.0)]
	smoothed_blue_cache = [((0.0, 0.0), 0.0), ((0.0, 0.0), 0.0)]

	smoothed_ball_cache_max_size = 20
	smoothed_yellow_cache_max_size = 20
	smoothed_blue_cache_max_size = 20

	def __init__(): abstract


	def add_data_tuple_to_ball_cache(data_tuple):
		Locator.add_data_tuple_to_cache(data_tuple, Locator.ball_cache, Locator.ball_cache_max_size)
	add_data_tuple_to_ball_cache = staticmethod(add_data_tuple_to_ball_cache)


	def add_data_tuple_to_yellow_cache(data_tuple):
		Locator.add_data_tuple_to_cache(data_tuple, Locator.yellow_cache, Locator.yellow_cache_max_size)
	add_data_tuple_to_yellow_cache = staticmethod(add_data_tuple_to_yellow_cache)


	def add_data_tuple_to_blue_cache(data_tuple):
		Locator.add_data_tuple_to_cache(data_tuple, Locator.blue_cache, Locator.blue_cache_max_size)
	add_data_tuple_to_blue_cache = staticmethod(add_data_tuple_to_blue_cache)


	"""
	Through these methods, a certain number of points is chosen for smoothing. The lists with the weights
	for each point are also input here. For example, currently, the last 2 points from the smoothed cache
	are used to predict the next point, which is smoothed with the point obtained through the vision system 
	for smoothing.
	"""
	def add_data_tuple_to_smoothed_ball_cache(data_tuple):
		chosen_vectors = Locator.get_members(Locator.ball_cache, 3)
		smoothed_data_tuple = Math.calc_weighted_average_for_vectors(chosen_vectors, [0.2,0.3,0.5])
		Locator.add_data_tuple_to_cache(smoothed_data_tuple, Locator.smoothed_ball_cache, Locator.smoothed_ball_cache_max_size)
	add_data_tuple_to_smoothed_ball_cache = staticmethod(add_data_tuple_to_smoothed_ball_cache)


	def add_data_tuple_to_smoothed_yellow_cache(data_tuple):
		chosen_vectors = Locator.get_members(Locator.yellow_cache, 3)
		smoothed_data_tuple = Math.calc_weighted_average_for_vectors(chosen_vectors, [0.2,0.3,0.5])
		Locator.add_data_tuple_to_cache(smoothed_data_tuple, Locator.smoothed_yellow_cache, Locator.smoothed_yellow_cache_max_size)
	add_data_tuple_to_smoothed_yellow_cache = staticmethod(add_data_tuple_to_smoothed_yellow_cache)


	def add_data_tuple_to_smoothed_blue_cache(data_tuple):
		chosen_vectors = Locator.get_members(Locator.blue_cache, 3)
		smoothed_data_tuple = Math.calc_weighted_average_for_vectors(chosen_vectors, [0.2,0.3,0.5])
		Locator.add_data_tuple_to_cache(smoothed_data_tuple, Locator.smoothed_blue_cache, Locator.smoothed_blue_cache_max_size)
	add_data_tuple_to_smoothed_blue_cache = staticmethod(add_data_tuple_to_smoothed_blue_cache)


	# number_of_members is an integer, say for example you have a = [1,2,3,4,5], and number_of_members = 4.
	# Then, the last 4 members of the list would be picked ([2,3,4,5]), and from this new list the first
	# and the last members will be used. The function returns a velocity vector (in pixels/minute)
	def calc_velocity_vector_ball(number_of_members):
		return Locator.calc_velocity_vector(Locator.smoothed_ball_cache, number_of_members)
	calc_velocity_vector_ball = staticmethod(calc_velocity_vector_ball)


	# same as the above.
	def calc_velocity_vector_yellow(number_of_members):
		return Locator.calc_velocity_vector(Locator.smoothed_yellow_cache, number_of_members)
	calc_velocity_vector_yellow = staticmethod(calc_velocity_vector_yellow)


	# same as the above.
	def calc_velocity_vector_blue(number_of_members):
		return Locator.calc_velocity_vector(Locator.smoothed_blue_cache, number_of_members)
	calc_velocity_vector_blue = staticmethod(calc_velocity_vector_blue)


	"""
	This function takes as input a data_tuple and the cache to which the data_tuple
	should be added, and returns the cache with the data_tuple in it. If the cache is
	already bigger than the maxsize, the last data_tuple in the cache is popped out.
	"""
	# data_tuple = (data_tuple, time)
	def add_data_tuple_to_cache(data_tuple, cache, maxsize):
		while len(cache) >= maxsize:
			cache.pop(0)
		cache.append(data_tuple)
		return cache
	add_data_tuple_to_cache = staticmethod(add_data_tuple_to_cache)


	"""
	This function returns the last 'n' members inserted into the cache. If there
	are less than 'n' members it would return all members. Please avoid passing 
	number_of_members = 0
	"""
	def get_members(cache, number_of_members):
		if len(cache) <= 1:
			return None
		else:		
			return cache[(-number_of_members - 1):]
	get_members = staticmethod(get_members)


	"""
	The function returns two tuples. The first tuple has the data from an older 
	frame. Please do not use pop, as it will mess things up. Talk to me if you
	want to argue :). Please avoid having number_of_members = 0
	"""
	def simple_get_members(cache, number_of_members):
		if len(cache) <= 1:
			return None
		else:
			members = cache[(-number_of_members - 1):]
			return (members[0], members[len(members)-1])
	simple_get_members = staticmethod(simple_get_members)


	"""
	Calculates the average of 'n' numbers from the cache. If there are less than 
	'n' members it would calculate the average of all members. Returns an intereger.
	"""
	def smoothe_numbers(cache, number_of_members):
		members = Locator.get_members(cache, number_of_members)
		if members != None:
			return Math.calc_average_for_numbers(members)
		else:
			return int(0)
	smoothe_numbers = staticmethod(smoothe_numbers)


	"""
	Calculates the average of 'n' vectors from the cache. If there are less than 
	'n' members it would calculate the average of all members. Returns a vector (x,y).
	"""
	def smoothe_vectors(cache, number_of_members):
		members = Locator.get_members(cache, number_of_members)
		if members != None:
			return Math.calc_average_for_vectors(members)
		else:
			return (int(0),int(0))
	smoothe_vectors = staticmethod(smoothe_vectors)


	"""
	Weighted smoothing. A list of weights can be passed as a parameter - use floats.
	See Math.py for a better explanation of how it works (calc_weighted_average_for_vectors).
	"""
	def weighted_smoothing(cache, number_of_members, list_of_weights):
		members_chosen = Locators.simple_get_members(cache, number_of_members)
		return Math.calc_weighted_average_for_vectors(members_chosen, list_of_weights)
	weighted_smoothing = staticmethod(weighted_smoothing)


	"""
	Calculates the velocity vector for 60 sec.
	older_newer_elem_tuple = [((x,y),time),((x2,y2),time2)], where the first tuple is
	from an older frame.
	"""
	def calc_velocity_vector(cache, number_of_members):
		if len(cache) <= 1:
			return (int(0),int(0))
		else:
			older_newer_elem_tuple = Locator.simple_get_members(cache, number_of_members)
			if older_newer_elem_tuple != None:
				velocity_vector = Math.sub_vectors(older_newer_elem_tuple[1][0], older_newer_elem_tuple[0][0])
				the_time = older_newer_elem_tuple[1][1] - older_newer_elem_tuple[0][1]
				try:
					velocity_vector = tuple([element*(1.0/the_time) for element in velocity_vector])
					velocity_vector = tuple([int(round(element*60.0)) for element in velocity_vector])
				except:
					return (int(0),int(0))
				return velocity_vector
			else:
				return (int(0),int(0))
	calc_velocity_vector = staticmethod(calc_velocity_vector)
