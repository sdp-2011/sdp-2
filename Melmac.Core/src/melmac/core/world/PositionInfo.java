package melmac.core.world;

public final class PositionInfo
{

    private final RobotInfo blueRobot;
    private final RobotInfo yellowRobot;
    private final BallInfo ball;

    public PositionInfo(RobotInfo blueRobot, RobotInfo yellowRobot, BallInfo ball)
    {
        this.blueRobot = blueRobot;
        this.yellowRobot = yellowRobot;
        this.ball = ball;
    }

    public BallInfo getBall()
    {
        return ball;
    }

    public RobotInfo getBlueRobot()
    {
        return blueRobot;
    }

    public RobotInfo getYellowRobot()
    {
        return yellowRobot;
    }
}
