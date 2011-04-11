import logging, sys, traceback

from __builtin__ import V_SETT # import settings


"""
Adds printing and logging stacktrace functionality.

@version 0.1
@author Nikolay Pulev
"""
class Debug:

	def __init__(): abstract

	def print_stacktrace():
		typ, val, trcb = sys.exc_info()	# exception type, value and traceback

		if V_SETT.DEBUG:				# print to standard output if DEBUG enabled
			traceback.print_exception(typ, val, trcb,
				limit = V_SETT.DEBUG_LEVEL, file = sys.stdout)

			if V_SETT.LOGGING:			# wrote to log file if ENAB_LOG enabled
				log_file = open(V_SETT.LOG_FILE, 'a')	# open in append mode
				traceback.print_exception(typ, val, trcb,
					limit = V_SETT.DEBUG_LEVEL, file = log_file)
				log_file.close()
	print_stacktrace = staticmethod(print_stacktrace)


	def print_vals(names = [], vals = []):
		print
		for name, val in zip(names, vals):
			print str(name) + ' : ' + str(val)
		print
	print_vals = staticmethod(print_vals)


	def breakpt():
		raw_input()
	breakpt = staticmethod(breakpt)
