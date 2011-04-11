package melmac.core.comms;

import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;
import melmac.core.utils.Constants;

public abstract class LightCommunications extends CommunicationsBase
{
    private final boolean sendFirst;
    private volatile boolean otherSeen;

    protected LightCommunications(Logger logger, LogMessages logMessages, int inAcknowledgementMessageType,
                                  String threadNamePrefix, boolean sendFirst)
    {
        super(logger, logMessages, inAcknowledgementMessageType, threadNamePrefix, false);
        this.sendFirst = sendFirst;
    }

    protected abstract LightReceiver getLightReceiver();

    protected abstract void turnLightOn();

    protected abstract void turnLightOff();

    @Override
    protected void sendInt(int value) throws Exception
    {
        do
        {
            pulse(value % 2 == 0 ? 1 : 2);
            value /= 2;
        }
        while (value > 0);

        Thread.sleep(Constants.LIGHT_TIME_BASE);
    }

    @Override
    protected void sendEnd() throws Exception
    {
        pulse(1);
    }

    private synchronized void pulse(int multiple) throws Exception
    {
        turnLightOn();
        Thread.sleep(Constants.LIGHT_TIME_BASE * multiple);
        turnLightOff();
        Thread.sleep(Constants.LIGHT_TIME_BASE);
    }

    @Override
    protected synchronized boolean onConnect() throws Exception
    {
        if (sendFirst)
        {
            otherSeen = true;
            return true;
        }

        while (!otherSeen)
        {
            if (!waitCheckRunning())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    protected synchronized void onDisconnect() throws Exception
    {
        otherSeen = false;
    }

    public synchronized void otherSeen()
    {
        if (!otherSeen)
        {
            otherSeen = true;
            this.notifyAll();

            while (!isConnected())
            {
                if (!waitCheckRunning())
                {
                    return;
                }
            }
        }
    }

    @Override
    protected void onStart() throws Exception
    {
        synchronized (this)
        {
            otherSeen = false;
        }

        super.onStart();
        getLightReceiver().start();
    }

    @Override
    protected void onStop() throws Exception
    {
        super.onStop();
        getLightReceiver().stop();
    }
}
