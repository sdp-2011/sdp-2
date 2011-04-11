import sys, __builtin__

from helpers.importing import import_file
from __builtin__ import V_SETT # import settings


class Tracker:
	def get_colour(): abstract # an abstract method
	

class BallTracker(Tracker):
	def get_colour():
		return 'red'


	def get_ball():
		return 'I am the ball and I am %s!' % BallTracker.get_colour()

	get_colour = staticmethod(get_colour)
	get_ball = staticmethod(get_ball)


class SelfTracker(Tracker):
	def get_colour():
		return 'blue'


	def get_self():
		return 'I am ME and I am %s!' % SelfTracker.get_colour()

	get_colour = staticmethod(get_colour)
	get_self = staticmethod(get_self)


class OpponentTracker(Tracker):
	def get_colour():
		return 'yellow'

		
	def get_opponent():
		return 'I am the opponent and I am %s!' % OpponentTracker.get_colour()

	get_colour = staticmethod(get_colour)
	get_opponent = staticmethod(get_opponent)


class PitchTracker(Tracker):
	def get_colour():
		return 'green'


	def get_pitch():
		return 'I am the pitch and I am %s!' % PitchTracker.get_colour()

	get_colour = staticmethod(get_colour)
	get_pitch = staticmethod(get_pitch)
