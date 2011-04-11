package melmac.vision.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.vision.Vision;
import melmac.core.world.Dimension;
import melmac.core.world.PitchInfo;

public final class PitchMessageHandler implements MessageHandler
{

    private final Vision vision;

    public PitchMessageHandler(Vision vision)
    {
        this.vision = vision;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        Dimension dimension = new Dimension(argBuffer[0], argBuffer[1]);
        vision.setPitchInfo(new PitchInfo(dimension, argBuffer[2], argBuffer[3], argBuffer[4], argBuffer[5]));
        return false;
    }
}
