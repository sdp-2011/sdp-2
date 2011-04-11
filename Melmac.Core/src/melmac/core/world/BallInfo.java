package melmac.core.world;

public final class BallInfo extends ObjectInfo
{

    public static final int RADIUS = 5;
    public static final int DIAMETER = RADIUS * 2;

    public BallInfo(Point position, Point velocity, boolean trusted)
    {
        super(RADIUS, position, velocity, trusted);
    }
}