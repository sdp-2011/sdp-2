package melmac.masterblock.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.masterblock.control.Controller;

public final class SpinMessageHandler implements MessageHandler
{

    private final Controller controller;

    public SpinMessageHandler(Controller controller)
    {
        this.controller = controller;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        controller.spin(argBuffer[0], argBuffer[1]);
        return true;
    }
}
