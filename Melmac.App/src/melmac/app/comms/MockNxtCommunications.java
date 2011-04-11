package melmac.app.comms;

import melmac.core.comms.ClientSocketCommunications;
import melmac.core.comms.DataInputStreamReceiver;
import melmac.core.comms.MessageType;
import melmac.core.comms.PcDataInputStreamReceiver;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class MockNxtCommunications extends ClientSocketCommunications
{
    private static final String threadNamePrefix = "MockNxtComms";
    private final DataInputStreamReceiver dataInputStreamReceiver;

    public MockNxtCommunications(Logger logger, String host, int port)
    {
        super(logger, host, port, LogMessages.NxtCommunications, MessageType.NxtToJava_Acknowledgement,
              MessageType.JavaToNxt_Reset, MessageType.JavaToNxt_Acknowledgement, MessageType.NxtToJavaCount,
              threadNamePrefix);
        dataInputStreamReceiver = new PcDataInputStreamReceiver(logger, LogMessages.NxtCommunications, threadNamePrefix,
                                                                this);
    }

    @Override
    protected DataInputStreamReceiver getDataInputStreamReceiver()
    {
        return dataInputStreamReceiver;
    }
}
