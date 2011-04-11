package melmac.core.comms;

import melmac.core.logging.LogMessages;
import melmac.core.utils.Constants;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.threading.AsyncProcess;

public final class AsyncMessageProcessor extends AsyncProcess implements MessageProcessor
{
    private final Object messageHandlersLock = new Object();
    private final CommunicationsBase communicationsBase;
    private final int outAcknowledgementMessageType;
    private final int queueOverflowLogMessage;
    private final int resetForcedLogMessage;
    private final int messageQueuedLogMessage;
    private final int messageProcessedLogMessage;
    private final MessageHandler[] messageHandlers;
    private final int[] messageTypes = new int[Constants.MESSAGE_BUFFER_SIZE];
    private final int[][] messageArgBuffers = new int[Constants.MESSAGE_BUFFER_SIZE][Constants.ARGUMENT_BUFFER_SIZE];
    private final int[] messageArgCounts = new int[Constants.MESSAGE_BUFFER_SIZE];
    private final int[] messageStatuses = new int[Constants.MESSAGE_BUFFER_SIZE];
    private volatile int nextMessageIndex;
    private volatile int queueLength;
    private volatile boolean resetRequested;

    public AsyncMessageProcessor(Logger logger, LogMessages logMessages, String threadNamePrefix,
                                 CommunicationsBase communicationsBase, int outAcknowledgementMessageType,
                                 int messageTypeCount)
    {
        super(logger, logMessages.PrcStrt, logMessages.PrcStpd, logMessages.PrcDied, logMessages.PrcSgnd,
              threadNamePrefix + "-AsyncMessageProcessor");
        this.communicationsBase = communicationsBase;
        this.outAcknowledgementMessageType = outAcknowledgementMessageType;
        this.queueOverflowLogMessage = logMessages.PrcQuOF;
        this.resetForcedLogMessage = logMessages.SndReFo;
        this.messageQueuedLogMessage = logMessages.PrcMeQu;
        this.messageProcessedLogMessage = logMessages.PrcMePr;
        this.messageHandlers = new MessageHandler[messageTypeCount];
    }

    @Override
    public synchronized void reset()
    {
        resetRequested = true;
    }

    private synchronized void checkReset()
    {
        if (resetRequested)
        {
            int iteration = 0;

            while (queueLength > 0 && iteration < Constants.MESSAGE_BUFFER_SIZE)
            {
                if (!waitCheckRunning())
                {
                    break;
                }

                iteration++;
            }

            if (iteration == Constants.MESSAGE_BUFFER_SIZE)
            {
                getLogger().log(true, Severity.Warning, resetForcedLogMessage);
            }

            for (int messageIndex = 0; messageIndex < Constants.MESSAGE_BUFFER_SIZE; messageIndex++)
            {
                messageStatuses[messageIndex] = InMessageStatus.Processed;
            }

            queueLength = 0;
            nextMessageIndex = 0;
            resetRequested = false;
        }
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
    public synchronized void process(int messageType, int[] argBuffer, int argCount) throws InterruptedException
    {
        checkReset();

        if (queueLength >= Constants.MESSAGE_BUFFER_SIZE || messageStatuses[nextMessageIndex]
                                                            != InMessageStatus.Processed)
        {
            getLogger().log(true, Severity.Warning, queueOverflowLogMessage);
            reset();
        }

        messageTypes[nextMessageIndex] = messageType;
        messageArgCounts[nextMessageIndex] = argCount;
        messageStatuses[nextMessageIndex] = InMessageStatus.PendingProcessing;
        queueLength++;
        System.arraycopy(argBuffer, 0, messageArgBuffers[nextMessageIndex], 0, argCount);
        getLogger().log(false, Severity.Debug, messageQueuedLogMessage, nextMessageIndex);

        if (nextMessageIndex == (Constants.MESSAGE_BUFFER_SIZE - 1))
        {
            nextMessageIndex = 0;
        }
        else
        {
            nextMessageIndex++;
        }

        notifyAll();
    }

    private synchronized int checkState(boolean updateStatus) throws Exception
    {
        for (int index = 0; index < Constants.MESSAGE_BUFFER_SIZE; index++)
        {
            int messageIndex = index + nextMessageIndex;

            if (messageIndex >= Constants.MESSAGE_BUFFER_SIZE)
            {
                messageIndex -= Constants.MESSAGE_BUFFER_SIZE;
            }

            if (messageStatuses[messageIndex] == InMessageStatus.PendingProcessing)
            {
                if (updateStatus)
                {
                    messageStatuses[messageIndex] = InMessageStatus.Processing;
                }

                return messageIndex;
            }
        }

        return -1;
    }

    @Override
    protected boolean shouldWait() throws Exception
    {
        return checkState(false) < 0;
    }

    @Override
    protected boolean execute() throws Exception
    {
        checkReset();
        int messageIndex = checkState(true);
        boolean sendAcknowledgement = false;

        if (messageIndex < 0)
        {
            return true;
        }

        synchronized (messageHandlersLock)
        {
            MessageHandler messageHandler = messageHandlers[messageTypes[messageIndex]];

            if (messageHandler != null)
            {
                sendAcknowledgement = messageHandler.handle(messageTypes[messageIndex],
                                                            messageArgBuffers[messageIndex],
                                                            messageArgCounts[messageIndex]);
            }
        }

        if (sendAcknowledgement)
        {
            communicationsBase.sendAsync(outAcknowledgementMessageType, Constants.EMPTY_BUFFER, 0);
        }

        synchronized (this)
        {
            if (messageStatuses[messageIndex] == InMessageStatus.Processing)
            {
                messageStatuses[messageIndex] = InMessageStatus.Processed;
                queueLength--;
                getLogger().log(false, Severity.Debug, messageProcessedLogMessage, messageIndex);
                this.notifyAll();
            }
        }

        return true;
    }

    @Override
    public boolean isSubscriber()
    {
        return true;
    }
}
