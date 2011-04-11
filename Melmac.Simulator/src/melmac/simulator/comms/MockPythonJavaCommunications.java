package melmac.simulator.comms;

import java.io.IOException;
import melmac.core.comms.DataInputStreamReceiver;
import melmac.core.comms.MessageType;
import melmac.core.comms.PcDataInputStreamReceiver;
import melmac.core.comms.ServerSocketCommunications;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class MockPythonJavaCommunications extends ServerSocketCommunications
{
    private static final String threadNamePrefix = "MockPythonJavaComms";
    private final DataInputStreamReceiver dataInputStreamReceiver;

    public MockPythonJavaCommunications(Logger logger, int port) throws IOException
    {
        super(logger, port, LogMessages.JavaCommunications, MessageType.JavaToPython_Acknowledgement,
              MessageType.PythonToJava_Acknowledgement, MessageType.JavaToPythonCount, threadNamePrefix);
        this.dataInputStreamReceiver = new PcDataInputStreamReceiver(logger, LogMessages.JavaCommunications,
                                                                     threadNamePrefix, this);
    }

    @Override
    protected DataInputStreamReceiver getDataInputStreamReceiver()
    {
        return dataInputStreamReceiver;
    }
}
