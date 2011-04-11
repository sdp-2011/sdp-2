package melmac.masterblock.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.masterblock.control.Controller;

public final class StopMessageHandler implements MessageHandler
{

    private final Controller controller;

    public StopMessageHandler(Controller controller)
    {
        this.controller = controller;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        controller.stop();
        return true;
    }
}
