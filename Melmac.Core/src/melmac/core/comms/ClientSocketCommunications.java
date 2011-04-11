package melmac.core.comms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;
import melmac.core.utils.Constants;

public abstract class ClientSocketCommunications extends ControllingDataInputStreamCommunications
{
    private final String host;
    private final int port;
    private final MessageProcessor messageProcessor;
    private final MessageSender messageSender;
    private volatile Socket socket;

    protected ClientSocketCommunications(Logger logger, String host, int port, LogMessages logMessages,
                                         int inAcknowledgementMessageType, int resetMessageType,
                                         int outAcknowledgementMessageType, int messageTypeCount,
                                         String threadNamePrefix)
    {
        super(logger, logMessages, inAcknowledgementMessageType, resetMessageType, threadNamePrefix);
        this.host = host;
        this.port = port;
        messageProcessor = new AsyncMessageProcessor(logger, logMessages, threadNamePrefix, this,
                                                     outAcknowledgementMessageType, messageTypeCount);
        messageSender = new AsyncMessageSender(logger, logMessages, threadNamePrefix, this);
    }

    @Override
    protected DataStreams getDataStreams()
    {
        try
        {
            Socket socketAttempt = new Socket(host, port);
            socketAttempt.setSoTimeout(Constants.SOCKET_ACCEPT_TIMEOUT);

            synchronized (this)
            {
                socket = socketAttempt;
                return new DataStreams(new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.
                    getOutputStream()));
            }
        }
        catch (Exception exception)
        {
        }

        return null;
    }

    @Override
    protected void onDisconnect() throws Exception
    {
        super.onDisconnect();

        synchronized (this)
        {
            if (socket != null)
            {
                socket.close();
                socket = null;
            }
        }
    }

    @Override
    protected MessageProcessor getMessageProcessor()
    {
        return messageProcessor;
    }

    @Override
    protected MessageSender getMessageSender()
    {
        return messageSender;
    }
}
