package melmac.app.comms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import melmac.core.comms.AsyncMessageProcessor;
import melmac.core.comms.AsyncMessageSender;
import melmac.core.comms.ControllingDataInputStreamCommunications;
import melmac.core.comms.DataInputStreamReceiver;
import melmac.core.logging.LogMessages;
import melmac.core.comms.MessageProcessor;
import melmac.core.comms.MessageSender;
import melmac.core.comms.MessageType;
import melmac.core.comms.PcDataInputStreamReceiver;
import melmac.core.logging.Logger;

public final class NxtCommunications extends ControllingDataInputStreamCommunications
{
    private static final String threadNamePrefix = "NxtComms";
    private final DataInputStreamReceiver dataInputStreamReceiver;
    private final int protocol;
    private final String name;
    private final String address;
    private final MessageProcessor messageProcessor;
    private final MessageSender messageSender;
    private volatile NXTComm nxtComm;

    public NxtCommunications(Logger logger, int protocol, String name, String address)
    {
        super(logger, LogMessages.NxtCommunications, MessageType.NxtToJava_Acknowledgement, MessageType.JavaToNxt_Reset,
              threadNamePrefix);
        dataInputStreamReceiver = new PcDataInputStreamReceiver(logger, LogMessages.NxtCommunications, threadNamePrefix,
                                                                this);
        this.protocol = protocol;
        this.name = name;
        this.address = address;
        messageProcessor = new AsyncMessageProcessor(logger, LogMessages.NxtCommunications, threadNamePrefix, this,
                                                     MessageType.JavaToNxt_Acknowledgement, MessageType.NxtToJavaCount);
        messageSender = new AsyncMessageSender(logger, LogMessages.NxtCommunications, threadNamePrefix, this);
    }

    @Override
    protected DataStreams getDataStreams()
    {
        try
        {
            NXTComm nxtCommAttempt = NXTCommFactory.createNXTComm(protocol);
            NXTInfo nxtInfo = new NXTInfo(protocol, name, address);

            if (nxtCommAttempt.open(nxtInfo))
            {
                synchronized (this)
                {
                    nxtComm = nxtCommAttempt;
                    return new DataStreams(new DataInputStream(nxtComm.getInputStream()), new DataOutputStream(nxtComm.
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
            if (nxtComm != null)
            {
                nxtComm.close();
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
