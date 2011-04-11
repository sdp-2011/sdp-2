package melmac.core.utils;

import melmac.core.world.*;

public final class Angle
{

    private Angle()
    {
    }

    public static double getAbsoluteAngle(Point vector)
    {
        // return the angle between the vector and the X axis
        // negative if below X axis, positive if above
        double radians = Math.atan2(vector.getY(), vector.getX());

        // assign the positive equivalent if negative
        while (radians < 0)
        {
            radians += 2 * Math.PI;
        }

        double degrees = Math.toDegrees(radians);

        //translate to the system's angle representation
        degrees += 90;

        return degrees % 360;
    }
}
