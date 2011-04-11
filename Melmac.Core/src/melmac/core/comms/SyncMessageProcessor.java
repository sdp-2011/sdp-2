package melmac.core.comms;

import melmac.core.logging.LogMessages;
import melmac.core.utils.Constants;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.threading.ProcessBase;

public final class SyncMessageProcessor extends ProcessBase implements MessageProcessor
{
    private final Object messageHandlersLock = new Object();
    private final CommunicationsBase communicationsBase;
    private final int acknowledgementMessageType;
    private final int messageProcessedLogMessage;
    private final MessageHandler[] messageHandlers;

    public SyncMessageProcessor(Logger logger, LogMessages logMessages, CommunicationsBase communicationsBase,
                                int acknowledgementMessageType, int messageTypeCount)
    {
        super(logger, logMessages.PrcStrt, logMessages.PrcStpd);
        this.communicationsBase = communicationsBase;
        this.acknowledgementMessageType = acknowledgementMessageType;
        this.messageProcessedLogMessage = logMessages.PrcMePr;
        this.messageHandlers = new MessageHandler[messageTypeCount];
    }

    @Override
    public void reset()
    {
    }

    @Override
    public void setMessageHandler(int messageType, MessageHandler messageHandler)
    {
        synchronized (messageHandlersLock)
        {
            messageHandlers[messageType] = messageHandler;
        }
    }

    @Override
    public void process(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        if (!isRunning())
        {
            return;
        }

        boolean sendAcknowledgement = false;

        synchronized (messageHandlersLock)
        {
            MessageHandler messageHandler = messageHandlers[messageType];

            if (messageHandler != null)
            {
                sendAcknowledgement = messageHandler.handle(messageType, argBuffer, argCount);
            }
        }

        if (sendAcknowledgement)
        {
            communicationsBase.sendAsync(acknowledgementMessageType, Constants.EMPTY_BUFFER, 0);
        }

        getLogger().log(false, Severity.Debug, messageProcessedLogMessage);
    }

    @Override
    public boolean isSubscriber()
    {
        return false;
    }
}
