package melmac.core.comms;

import melmac.core.logging.LogMessages;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.utils.Constants;
import melmac.core.threading.ProcessBase;
import melmac.core.threading.Process;
import melmac.core.threading.Subscriber;

public abstract class CommunicationsBase extends ProcessBase implements Communications
{
    private final int inAcknowledgementMessageType;
    private final LogMessages logMessages;
    private final Watchdog watchdog;
    private final boolean fullDuplexSupported;
    private volatile boolean connected;
    private volatile boolean sending;
    private volatile boolean receiving;
    private volatile boolean connecting;
    private volatile boolean disconnecting;

    protected CommunicationsBase(Logger logger, LogMessages logMessages, int inAcknowledgementMessageType,
                                 String threadNamePrefix, boolean fullDuplexSupported)
    {
        super(logger, logMessages.CmsStrt, logMessages.CmsStpd);
        this.inAcknowledgementMessageType = inAcknowledgementMessageType;
        this.logMessages = logMessages;
        this.watchdog = new Watchdog(logger, logMessages, threadNamePrefix, this);
        this.fullDuplexSupported = fullDuplexSupported;
    }

    protected abstract MessageProcessor getMessageProcessor();

    protected abstract MessageSender getMessageSender();

    protected abstract void sendInt(int value) throws Exception;

    protected abstract void sendEnd() throws Exception;

    protected boolean onConnect() throws Exception
    {
        return true;
    }

    protected void onDisconnect() throws Exception
    {
    }

    @Override
    protected void onStart() throws Exception
    {
        super.onStart();
        reset();
        // NOTE: Casting to Process to help LeJOS RCX linker
        ((Process) getMessageSender()).start();
        ((Process) getMessageProcessor()).start();
        watchdog.start();
    }

    @Override
    protected void onStop() throws Exception
    {
        super.onStop();
        reset();
        watchdog.stop();
        // NOTE: Casting to Process to help LeJOS RCX linker
        ((Process) getMessageProcessor()).stop();
        ((Process) getMessageSender()).stop();
        disconnect();
    }

    @Override
    public void reset()
    {
        getMessageSender().reset();
        getMessageProcessor().reset();
    }

    protected boolean connect() throws Exception
    {
        synchronized (this)
        {
            if (!isRunning() || connected || connecting)
            {
                return connected;
            }

            connecting = true;
        }

        getLogger().log(false, Severity.Info, logMessages.CmsWtng);

        while (!onConnect())
        {
            synchronized (this)
            {
                if (!waitCheckRunning())
                {
                    connecting = false;
                    return false;
                }
            }
        }

        synchronized (this)
        {
            connected = true;
            connecting = false;
            this.notifyAll();
        }

        MessageProcessor messageProcessor = getMessageProcessor();

        if (messageProcessor.isSubscriber())
        {
            ((Subscriber) messageProcessor).signal();
        }

        MessageSender messageSender = getMessageSender();

        if (messageSender.isSubscriber())
        {
            ((Subscriber) messageSender).signal();
        }

        getLogger().log(false, Severity.Info, logMessages.CmsCntd);
        return true;
    }

    public void disconnect()
    {
        synchronized (this)
        {
            if (!connected || disconnecting)
            {
                return;
            }

            disconnecting = true;
        }

        try
        {
            onDisconnect();
        }
        catch (Exception exception)
        {
            getLogger().log(false, Severity.Info, LogMessage.EFldDscnct);
        }

        synchronized (this)
        {
            disconnecting = false;
            connected = false;
            this.notifyAll();
        }

        getLogger().log(false, Severity.Info, logMessages.CmsDctd);
    }

    @Override
    public synchronized boolean isClearToSend()
    {
        return isRunning() && connected && !getMessageSender().isFull();
    }

    protected synchronized boolean isConnected()
    {
        return connected;
    }

    protected synchronized boolean isReceiving()
    {
        return receiving;
    }

    @Override
    public void setMessageHandler(int messageType, MessageHandler messageHandler)
    {
        getMessageProcessor().setMessageHandler(messageType, messageHandler);
    }

    @Override
    public void sendSync(int messageType) throws Exception
    {
        sendSync(messageType, Constants.EMPTY_BUFFER, 0);
    }

    @Override
    public void sendAsync(int messageType) throws Exception
    {
        sendAsync(messageType, Constants.EMPTY_BUFFER, 0);
    }

    @Override
    public void sendSync(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        getMessageSender().send(messageType, true, argBuffer, argCount);
    }

    @Override
    public void sendAsync(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        getMessageSender().send(messageType, false, argBuffer, argCount);
    }

    protected synchronized boolean canStartSend()
    {
        if (!isRunning() || !connected || sending || (!fullDuplexSupported && receiving))
        {
            return false;
        }

        return true;
    }

    protected synchronized boolean startSend()
    {
        if (!canStartSend())
        {
            return false;
        }

        sending = true;
        return true;
    }

    protected synchronized void completeSend()
    {
        sending = false;
    }

    protected boolean startReceive() throws Exception
    {
        boolean result;
        boolean disconnectRequired;

        synchronized (this)
        {
            if (!isRunning() || !connected || receiving)
            {
                result = false;
                disconnectRequired = false;
            }
            else if (!fullDuplexSupported && sending)
            {
                getLogger().log(false, Severity.Severe, LogMessage.EHlfDplxFl);
                result = false;
                disconnectRequired = true;
            }
            else
            {
                receiving = true;
                result = true;
                disconnectRequired = false;
            }
        }

        if (disconnectRequired)
        {
            disconnect();
        }

        return result;
    }

    protected void completeReceive(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        synchronized (this)
        {
            receiving = false;
        }

        if (messageType == inAcknowledgementMessageType)
        {
            getMessageSender().acknowledgementReceived();
        }
        else
        {
            getMessageProcessor().process(messageType, argBuffer, argCount);
        }
    }
}
