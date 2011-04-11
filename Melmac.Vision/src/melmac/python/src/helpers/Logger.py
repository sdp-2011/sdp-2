import logging, sys

from __builtin__ import V_SETT	# import settings


"""
This class adds logging support to the vision system.

@version 0.1
@author Nikolay Pulev
"""
class Logger:
	
	def	__init__(): abstract

	def log(line):
		if V_SETT.LOGGING:
			print line 
			logging.debug(line)
	log = staticmethod(log)
