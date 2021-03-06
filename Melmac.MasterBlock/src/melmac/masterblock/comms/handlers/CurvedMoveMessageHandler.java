package melmac.masterblock.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.masterblock.control.Controller;

public final class CurvedMoveMessageHandler implements MessageHandler
{

    private final Controller controller;

    public CurvedMoveMessageHandler(Controller controller)
    {
        this.controller = controller;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        controller.curvedMove(argBuffer[0], argBuffer[1], argBuffer[2], argBuffer[3], argBuffer[4]);
        return true;
    }
}
