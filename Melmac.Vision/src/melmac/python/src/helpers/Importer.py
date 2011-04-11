import os, sys, math


"""
Allows importing of python modules from any location.

@version 0.1
@author Nikolay Pulev
"""
class Importer:

	def __init__(): abstract


	"""
	This function always returns a full path to a location, given as argument.
	If a relative path is given, the current path is prepended.
	"""
	def get_full_path_from_curr(path):
		# Python is itellignet.
		# If two full paths given, the second will be returned.
		# If one relative and one full path given, the full one will be returned.
		return os.path.join(os.getcwd(), path)
	get_full_path_from_curr = staticmethod(get_full_path_from_curr)


	"""
	Imports moduled from files, given a full_path
	This function also returns the module object so that it can be used
	inside the file.
	"""
	def import_file(mod_path):
		mod_dir, mod_filename = os.path.split(mod_path)
		mod_name, mod_extention = os.path.splitext(mod_filename)
		saved_dir = os.getcwd()	# save current dir
		os.chdir(mod_dir)		# go to the module's directory
		# It seems python cannot import module if it's location is not on
		# the PYTHONPATH. So add it to the current path list if not present.
		if not (os.getcwd() in sys.path):
			sys.path.append(os.getcwd())
		mod_obj = __import__(mod_name)		# obtain module as object
		mod_obj.__file__ = mod_path			# set module necessary property
		globals()[mod_name] = mod_obj		# may be ommitted
		os.chdir(saved_dir)
		return mod_obj
	import_file = staticmethod(import_file)
