package melmac.core.comms;

import melmac.core.logging.LogMessages;
import melmac.core.utils.Constants;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.threading.AsyncProcess;

public final class AsyncMessageSender extends AsyncProcess implements MessageSender
{
    private final CommunicationsBase communicationsBase;
    private final int queueOverflowLogMessage;
    private final int resetForcedLogMessage;
    private final int messageQueuedLogMessage;
    private final int messageSentLogMessage;
    private final int[] messageTypes = new int[Constants.MESSAGE_BUFFER_SIZE];
    private final int[][] messageArgBuffers = new int[Constants.MESSAGE_BUFFER_SIZE][Constants.ARGUMENT_BUFFER_SIZE];
    private final int[] messageArgCounts = new int[Constants.MESSAGE_BUFFER_SIZE];
    private final int[] messageStatuses = new int[Constants.MESSAGE_BUFFER_SIZE];
    private final boolean[] messageRequiresAcknowledgments = new boolean[Constants.MESSAGE_BUFFER_SIZE];
    private volatile int nextMessageIndex;
    private volatile int queueLength;

    public AsyncMessageSender(Logger logger, LogMessages logMessages, String threadNamePrefix,
                              CommunicationsBase communicationsBase)
    {
        super(logger, logMessages.SndStrt, logMessages.SndStpd, logMessages.SndDied, logMessages.SndSgnd,
              threadNamePrefix + "-AsyncMessageSender");
        this.communicationsBase = communicationsBase;
        this.queueOverflowLogMessage = logMessages.SndQuOF;
        this.resetForcedLogMessage = logMessages.SndReFo;
        this.messageQueuedLogMessage = logMessages.SndMeQu;
        this.messageSentLogMessage = logMessages.SndMeSe;
    }

    @Override
    public synchronized void reset()
    {
        if (communicationsBase.canStartSend())
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
        }

        for (int messageIndex = 0; messageIndex < Constants.MESSAGE_BUFFER_SIZE; messageIndex++)
        {
            messageStatuses[messageIndex] = OutMessageStatus.Sent;
        }

        queueLength = 0;
        nextMessageIndex = 0;
        this.notifyAll();
    }

    @Override
    public synchronized void acknowledgementReceived()
    {
        for (int index = 0; index < Constants.MESSAGE_BUFFER_SIZE; index++)
        {
            int messageIndex = index + nextMessageIndex;

            if (messageIndex >= Constants.MESSAGE_BUFFER_SIZE)
            {
                messageIndex -= Constants.MESSAGE_BUFFER_SIZE;
            }

            if (messageStatuses[messageIndex] == OutMessageStatus.PendingAcknowledgement)
            {
                messageStatuses[messageIndex] = OutMessageStatus.PendingAcknowledgementPickup;
                queueLength--;
                this.notifyAll();
                break;
            }
        }
    }

    @Override
    public synchronized void send(int messageType, boolean requiresAcknowledgement, int[] argBuffer, int argCount)
    {
        if (isFull())
        {
            getLogger().log(true, Severity.Warning, queueOverflowLogMessage);
            reset();
        }

        messageTypes[nextMessageIndex] = messageType;
        messageRequiresAcknowledgments[nextMessageIndex] = requiresAcknowledgement;
        messageArgCounts[nextMessageIndex] = argCount;
        messageStatuses[nextMessageIndex] = OutMessageStatus.PendingSend;
        System.arraycopy(argBuffer, 0, messageArgBuffers[nextMessageIndex], 0, argCount);
        getLogger().log(false, Severity.Debug, messageQueuedLogMessage, nextMessageIndex);
        int messageIndex = nextMessageIndex;
        queueLength++;

        if (nextMessageIndex == (Constants.MESSAGE_BUFFER_SIZE - 1))
        {
            nextMessageIndex = 0;
        }
        else
        {
            nextMessageIndex++;
        }

        notifyAll();

        if (requiresAcknowledgement)
        {
            while (messageStatuses[messageIndex] == OutMessageStatus.PendingSend
                   || messageStatuses[messageIndex] == OutMessageStatus.Sending
                   || messageStatuses[messageIndex] == OutMessageStatus.PendingAcknowledgement)
            {
                if (!waitCheckRunning())
                {
                    return;
                }
            }

            if (messageStatuses[messageIndex] == OutMessageStatus.PendingAcknowledgementPickup)
            {
                messageStatuses[messageIndex] = OutMessageStatus.Sent;
                queueLength--;
                getLogger().log(false, Severity.Debug, messageSentLogMessage, messageIndex);
                notifyAll();
            }
        }
    }

    @Override
    public synchronized boolean isFull()
    {
        return queueLength >= Constants.MESSAGE_BUFFER_SIZE || messageStatuses[nextMessageIndex]
                                                               != OutMessageStatus.Sent;
    }

    @Override
    protected boolean onException(Exception exception)
    {
        communicationsBase.disconnect();
        return false;
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

            if (messageStatuses[messageIndex] == OutMessageStatus.PendingSend)
            {
                if (updateStatus)
                {
                    messageStatuses[messageIndex] = OutMessageStatus.Sending;
                }

                return messageIndex;
            }
        }

        return -1;
    }

    @Override
    protected boolean shouldWait() throws Exception
    {
        return checkState(false) < 0 || !communicationsBase.canStartSend();
    }

    @Override
    public boolean execute() throws Exception
    {
        if (!communicationsBase.canStartSend())
        {
            return true;
        }

        int messageIndex = checkState(true);

        if (messageIndex < 0)
        {
            return true;
        }

        if (!communicationsBase.startSend())
        {
            return true;
        }

        communicationsBase.sendInt(messageTypes[messageIndex]);
        communicationsBase.sendInt(messageArgCounts[messageIndex]);

        for (int index = 0; index < messageArgCounts[messageIndex]; index++)
        {
            communicationsBase.sendInt(messageArgBuffers[messageIndex][index]);
        }

        communicationsBase.sendEnd();
        communicationsBase.completeSend();

        synchronized (this)
        {
            if (messageStatuses[messageIndex] == OutMessageStatus.Sending)
            {
                if (messageRequiresAcknowledgments[messageIndex])
                {
                    messageStatuses[messageIndex] = OutMessageStatus.PendingAcknowledgement;
                }
                else
                {
                    messageStatuses[messageIndex] = OutMessageStatus.Sent;
                    queueLength--;
                    getLogger().log(false, Severity.Debug, messageSentLogMessage, messageIndex);
                }

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
