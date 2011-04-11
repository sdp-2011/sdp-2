#!/bin/bash

#
# UNIVERSITY OF EDINBURGH, SDP GROUP 2, 2011
#

v4l_ctl=/usr/bin/v4lctl

# Set video source to Composite0 and mode to NTSC.
# These settings are not desired, but are needed for the desired
# to work. It seems that OpenCV can only react to the change if
# these have been set prior to the correct ones.
$v4l_ctl setnorm NTSC
$v4l_ctl setinput Composite0

# Now setting the correct values.
echo 'V4L: Setting video norm to PAL ...'
$v4l_ctl setnorm PAL	# set video norm to PAL
echo 'V4L: Setting video source to S-Video ...'
$v4l_ctl setinput S-Video

$v4l_ctl bright $1
echo 'Done. Exiting ...'

# Ready to go, have fun!
exit 0

