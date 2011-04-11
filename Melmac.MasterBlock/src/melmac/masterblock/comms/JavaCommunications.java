package melmac.masterblock.comms;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import melmac.core.comms.AsyncMessageProcessor;
import melmac.core.comms.AsyncMessageSender;
import melmac.core.comms.DataInputStreamCommunications;
import melmac.core.comms.DataInputStreamReceiver;
import melmac.core.comms.MessageProcessor;
import melmac.core.comms.MessageSender;
import melmac.core.comms.MessageType;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class JavaCommunications extends DataInputStreamCommunications
{
    private static final String threadNamePrefix = "JavaComms";
    private final DataInputStreamReceiver dataInputStreamReceiver;
    private final MessageProcessor messageProcessor;
    private final MessageSender messageSender;
    private volatile NXTConnection connection;

    public JavaCommunications(Logger logger)
    {
        super(logger, LogMessages.JavaCommunications, MessageType.JavaToNxt_Acknowledgement, threadNamePrefix);
        dataInputStreamReceiver = new NxtDataInputStreamReceiver(logger, LogMessages.JavaCommunications,
                                                                 threadNamePrefix, this);
        messageProcessor = new AsyncMessageProcessor(logger, LogMessages.JavaCommunications, threadNamePrefix, this,
                                                     MessageType.NxtToJava_Acknowledgement, MessageType.JavaToNxtCount);
        messageSender = new AsyncMessageSender(logger, LogMessages.JavaCommunications, threadNamePrefix, this);
    }

    @Override
    protected DataStreams getDataStreams()
    {
        NXTConnection connectionAttempt = Bluetooth.waitForConnection();

        if (connectionAttempt == null)
        {
            return null;
        }

        synchronized (this)
        {
            connection = connectionAttempt;
            return new DataStreams(connection.openDataInputStream(), connection.openDataOutputStream());
        }
    }

    @Override
    protected void onDisconnect() throws Exception
    {
        super.onDisconnect();

        synchronized (this)
        {
            if (connection != null)
            {
                connection.close();
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

    @Override
    protected DataInputStreamReceiver getDataInputStreamReceiver()
    {
        return dataInputStreamReceiver;
    }
}
