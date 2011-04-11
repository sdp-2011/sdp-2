package melmac.masterblock.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.masterblock.control.Controller;

public final class MoveMessageHandler implements MessageHandler
{

    private final Controller controller;

    public MoveMessageHandler(Controller controller)
    {
        this.controller = controller;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        controller.move(argBuffer[0], argBuffer[1]);
        return false;
    }
}
