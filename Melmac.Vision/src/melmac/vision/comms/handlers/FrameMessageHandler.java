package melmac.vision.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.core.world.BallInfo;
import melmac.core.world.Point;
import melmac.core.world.PositionInfo;
import melmac.core.world.RobotInfo;
import melmac.vision.Vision;

public final class FrameMessageHandler implements MessageHandler
{

    private final Vision vision;

    public FrameMessageHandler(Vision vision)
    {
        this.vision = vision;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        BallInfo ball = new BallInfo(new Point(argBuffer[0], argBuffer[1]), new Point(argBuffer[13], argBuffer[14]), argBuffer[10] == 0);
        RobotInfo blueRobot = new RobotInfo(new Point(argBuffer[2], argBuffer[3]), new Point(argBuffer[15], argBuffer[16]), argBuffer[11] == 0, new Point(argBuffer[6], argBuffer[7]));
        RobotInfo yellowRobot = new RobotInfo(new Point(argBuffer[4], argBuffer[5]), new Point(argBuffer[17], argBuffer[18]), argBuffer[12] == 0, new Point(argBuffer[8], argBuffer[9]));
        vision.setPositionInfo(new PositionInfo(blueRobot, yellowRobot, ball));
        return false;
    }
}
