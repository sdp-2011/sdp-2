package melmac.core.utils;

import melmac.core.world.Point;

public final class PointOfIntersection
{

    public static Point intersectionOfTwoLines(Point startOne, Point startTwo, Point endOne, Point endTwo)
    {
        double p = startOne.getX();
        double q = startOne.getY();
        double r = startTwo.getX();
        double s = startTwo.getY();
        double e = endOne.getX();
        double f = endOne.getY();
        double g = endTwo.getX();
        double h = endTwo.getY();

        if (r == p)
        {
            p += 1.0;
        }

        if (g == e)
        {
            e += 1.0;
        }

        // finding gradient for Line1 and 2
        double m1 = (s - q) / (r - p);
        double m2 = (h - f) / (g - e);

        if (m1 == m2)
        {
            m2 += 1.0;
        }

        // Equation of the straight line: y-y1 = m(x-x1)
        double x = ((m1 * p) - (m2 * e) + f - q) / (m1 - m2);
        double y = ((m1 * m2) * (p - e) + (m1 * f) - (m2 * q)) / (m1 - m2);

        return new Point((int) x, (int) y);
    }
}
