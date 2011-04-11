package melmac.core.world;

import melmac.core.utils.Angle;

public final class RobotInfo extends ObjectInfo
{
    // 1cm = 1.54 pix => 20cm = 31 pix => longest ~ 45(testing needed)
    public static final int ROBOT_SIZE = 60;
    private final Point direction;

    public RobotInfo(Point position, Point velocity, boolean trusted, Point direction)
    {
        super(ROBOT_SIZE, position, velocity, trusted);
        this.direction = direction;
    }

    public Point getDirection()
    {
        return direction;
    }

    public double getAbsoluteAngle()
    {
        return Angle.getAbsoluteAngle(direction);
    }

    public double getRelativeAngleTo(Point point)
    {
        Point directionToTarget = getPosition().getDirectionTo(point);
        double angleToTarget = Angle.getAbsoluteAngle(directionToTarget);
        double selfAngle = Angle.getAbsoluteAngle(direction);
        return angleToTarget - selfAngle;
    }
}