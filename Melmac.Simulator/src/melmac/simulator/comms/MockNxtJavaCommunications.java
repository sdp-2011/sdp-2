package melmac.simulator.comms;

import java.io.IOException;
import melmac.core.comms.DataInputStreamReceiver;
import melmac.core.comms.MessageType;
import melmac.core.comms.PcDataInputStreamReceiver;
import melmac.core.comms.ServerSocketCommunications;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class MockNxtJavaCommunications extends ServerSocketCommunications
{
    private static final String threadNamePrefix = "MockNxtJavaComms";
    private final DataInputStreamReceiver dataInputStreamReceiver;

    public MockNxtJavaCommunications(Logger logger, int port) throws IOException
    {
        super(logger, port, LogMessages.JavaCommunications, MessageType.JavaToNxt_Acknowledgement,
              MessageType.NxtToJava_Acknowledgement, MessageType.JavaToNxtCount, threadNamePrefix);
        this.dataInputStreamReceiver = new PcDataInputStreamReceiver(logger, LogMessages.JavaCommunications,
                                                                     threadNamePrefix, this);
    }

    @Override
    protected DataInputStreamReceiver getDataInputStreamReceiver()
    {
        return dataInputStreamReceiver;
    }
}
