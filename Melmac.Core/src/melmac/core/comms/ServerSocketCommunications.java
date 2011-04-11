package melmac.core.comms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;
import melmac.core.utils.Constants;

public abstract class ServerSocketCommunications extends DataInputStreamCommunications
{
    private final MessageProcessor messageProcessor;
    private final MessageSender messageSender;
    private final ServerSocket serverSocket;
    private volatile Socket socket;

    protected ServerSocketCommunications(Logger logger, int port, LogMessages logMessages,
                                         int inAcknowledgementMessageType, int outAcknowledgementMessageType,
                                         int messageTypeCount, String threadNamePrefix) throws IOException
    {
        super(logger, logMessages, inAcknowledgementMessageType, threadNamePrefix);
        messageProcessor = new AsyncMessageProcessor(logger, logMessages, threadNamePrefix, this,
                                                     outAcknowledgementMessageType, messageTypeCount);
        messageSender = new AsyncMessageSender(logger, logMessages, threadNamePrefix, this);
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(Constants.SOCKET_ACCEPT_TIMEOUT);
    }

    @Override
    protected DataStreams getDataStreams()
    {
        try
        {
            Socket socketAttempt = serverSocket.accept();

            if (socketAttempt != null)
            {
                synchronized (this)
                {
                    socket = socketAttempt;
                    return new DataStreams(new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.
                        getOutputStream()));
                }
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
