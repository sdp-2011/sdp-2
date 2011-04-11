package melmac.slaveblock.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.slaveblock.control.Controller;

public final class KickMessageHandler implements MessageHandler
{

    private final Controller controller;

    public KickMessageHandler(Controller controller)
    {
        this.controller = controller;
    }

    // NOTE: @Override not supported on RCX
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        this.controller.kick();
        return true;
    }
}
