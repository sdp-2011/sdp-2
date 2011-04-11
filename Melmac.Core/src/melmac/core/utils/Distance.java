package melmac.core.utils;

import melmac.core.world.ObjectInfo;

public final class Distance
{

    private Distance()
    {
    }

    public static double squaredEuclidean(Vector a, Vector b)
    {
        double xDiff = a.getX() - b.getX();
        double yDiff = a.getY() - b.getY();
        return xDiff * xDiff + yDiff * yDiff;
    }

    public static double euclidean(Vector a, Vector b)
    {
        return Math.sqrt(squaredEuclidean(a, b));
    }

    public static boolean isCloserTo(Vector what, Vector to, Vector than)
    {
        return Distance.squaredEuclidean(what, to) < Distance.squaredEuclidean(than, to);
    }

    public static boolean isCloserTo(ObjectInfo what, ObjectInfo to, ObjectInfo than)
    {
        return isCloserTo(what.getPosition(), to.getPosition(), than.getPosition());
    }
}
