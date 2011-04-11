package melmac.core.world;

public abstract class ObjectInfo
{

    private final int radius;
    private final Point position;
    private final Point velocity;
    private final boolean trusted;

    public ObjectInfo(int radius, Point position, Point velocity, boolean trusted)
    {
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
        this.trusted = trusted;
    }

    public Point getPosition()
    {
        return position;
    }

    public int getRadius()
    {
        return radius;
    }

    public boolean isTrusted()
    {
        return trusted;
    }

    public Point getVelocity()
    {
        return velocity;
    }
}
