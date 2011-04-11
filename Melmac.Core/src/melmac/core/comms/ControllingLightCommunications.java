package melmac.core.comms;

import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public abstract class ControllingLightCommunications extends LightCommunications
{
    private final int resetMessageType;
    private volatile boolean resetRequired;

    protected ControllingLightCommunications(Logger logger, LogMessages logMessages, int inAcknowledgementMessageType,
                                             int resetMessageType, String threadNamePrefix, boolean sendFirst)
    {
        super(logger, logMessages, inAcknowledgementMessageType, threadNamePrefix, sendFirst);
        this.resetMessageType = resetMessageType;
    }

    @Override
    protected void onStart() throws Exception
    {
        super.onStart();

        synchronized (this)
        {
            resetRequired = true;
        }
    }

    @Override
    protected void onStop() throws Exception
    {
        super.onStop();

        synchronized (this)
        {
            resetRequired = true;
        }
    }

    private void sendReset()
    {
        boolean sendReset;

        synchronized (this)
        {
            sendReset = resetRequired;
            resetRequired = false;
        }

        if (sendReset)
        {
            try
            {
                sendSync(resetMessageType);
            }
            catch (Exception exception)
            {
            }
        }
    }

    @Override
    protected boolean connect() throws Exception
    {
        if (super.connect())
        {
            sendReset();
            return true;
        }

        return false;
    }

    @Override
    public void disconnect()
    {
        if (isClearToSend())
        {
            sendReset();
        }

        super.disconnect();
    }
}
