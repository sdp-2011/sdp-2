package melmac.core.utils;

import melmac.core.world.Point;

public class Vector
{

    private static Point ORIGIN = new Point(0, 0);
    private final int x;
    private final int y;

    protected Vector(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    /**
     * Retruns the direction vector from this point to the point given as an
     * argument.
     */
    public Point getDirectionTo(Point destination)
    {
        return new Point(destination.getX() - x, destination.getY() - y);
    }

    @Override
    public String toString()
    {
        return "[" + String.valueOf(x) + "," + String.valueOf(y) + "]";
    }

    public Point translate(int x, int y)
    {
        return new Point(this.x + x, this.y + y);
    }

    public double getLength() {
        return Distance.euclidean(ORIGIN, this);
    }

    public static Point add(Point vec1, Point vec2)
    {
        return new Point(vec1.getX() + vec2.getX(), vec1.getY() + vec2.getY());
    }

    public static Point subtract(Point vec1, Point vec2)
    {
        return new Point(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY());
    }

    /**
     * Retuns a vector, pointing at the middle point of the line that connects
     * the tips of the two parameter vectors.
     */
    public static Point getMidPoint(Point vec1, Point vec2)
    {
        return Vector.scale(Vector.add(vec1, vec2), 1 / 2.0);
    }

    public static Point scale(Point vec, double scale)
    {
        return new Point((int) (vec.getX() * scale), (int) (vec.getY() * scale));
    }

    /**
     * Returns the dot product of two vectors.
     */
    public static Point dot(Point vec1, Point vec2)
    {
        return new Point(vec1.getX() * vec2.getY(), vec1.getY() * vec2.getY());
    }
}
