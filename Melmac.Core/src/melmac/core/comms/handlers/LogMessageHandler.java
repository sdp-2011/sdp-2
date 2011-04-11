package melmac.core.comms.handlers;

import melmac.core.utils.Constants;
import melmac.core.comms.MessageHandler;
import melmac.core.logging.Logger;

public final class LogMessageHandler implements MessageHandler
{

    private final int[] argBuffer = new int[Constants.ARGUMENT_BUFFER_SIZE];
    private final Logger logger;

    public LogMessageHandler(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        System.arraycopy(argBuffer, 3, this.argBuffer, 0, argCount - 3);
        logger.log(true, argBuffer[1], argBuffer[2], this.argBuffer, argCount - 3, argBuffer[0]);
        return false;
    }
}
