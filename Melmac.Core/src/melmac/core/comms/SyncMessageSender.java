package melmac.core.comms;

import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.threading.ProcessBase;

public final class SyncMessageSender extends ProcessBase implements MessageSender
{
    private final CommunicationsBase communicationsBase;
    private final int messageSentLogMessage;
    private volatile boolean pendingAcknowledgement;

    public SyncMessageSender(Logger logger, LogMessages logMessages, CommunicationsBase communicationsBase)
    {
        super(logger, logMessages.SndStrt, logMessages.SndStpd);
        this.communicationsBase = communicationsBase;
        this.messageSentLogMessage = logMessages.SndMeSe;
    }

    @Override
    public synchronized void reset()
    {
        pendingAcknowledgement = false;
        notifyAll();
    }

    @Override
    public synchronized void acknowledgementReceived()
    {
        pendingAcknowledgement = false;
        this.notifyAll();
    }

    @Override
    public void send(int messageType, boolean requiresAcknowledgement, int[] argBuffer, int argCount) throws Exception
    {
        if (!communicationsBase.startSend())
        {
            return;
        }

        communicationsBase.sendInt(messageType);
        communicationsBase.sendInt(argCount);

        for (int index = 0; index < argCount; index++)
        {
            communicationsBase.sendInt(argBuffer[index]);
        }

        communicationsBase.sendEnd();
        communicationsBase.completeSend();

        if (requiresAcknowledgement)
        {
            synchronized (this)
            {
                pendingAcknowledgement = true;

                while (pendingAcknowledgement)
                {
                    if (!waitCheckRunning())
                    {
                        return;
                    }
                }
            }
        }

        getLogger().log(false, Severity.Debug, messageSentLogMessage);
    }

    @Override
    public synchronized boolean isFull()
    {
        return pendingAcknowledgement;
    }

    @Override
    public boolean isSubscriber()
    {
        return false;
    }
}
