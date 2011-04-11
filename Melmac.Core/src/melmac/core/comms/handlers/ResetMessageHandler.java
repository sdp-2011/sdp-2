package melmac.core.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.core.utils.Constants;

public final class ResetMessageHandler implements MessageHandler
{
    private volatile boolean resetRequested = false;

    @Override
    public synchronized boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        resetRequested = true;
        this.notifyAll();
        return true;
    }

    public synchronized void waitForReset() throws Exception
    {
        while (true)
        {
            this.wait(Constants.MONITOR_WAIT_TIMEOUT);

            if (resetRequested)
            {
                resetRequested = false;
                return;
            }
        }
    }

    public synchronized boolean isResetRequested()
    {
        boolean temp = resetRequested;
        resetRequested = false;
        return temp;
    }
}
