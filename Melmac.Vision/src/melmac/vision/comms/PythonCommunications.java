package melmac.vision.comms;

import melmac.core.comms.ClientSocketCommunications;
import melmac.core.comms.DataInputStreamReceiver;
import melmac.core.comms.MessageType;
import melmac.core.comms.PcDataInputStreamReceiver;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class PythonCommunications extends ClientSocketCommunications
{
    private static final String threadNamePrefix = "PythonComms";
    private final DataInputStreamReceiver dataInputStreamReceiver;

    public PythonCommunications(Logger logger, String host, int port)
    {
        super(logger, host, port, LogMessages.PythonCommunications, MessageType.PythonToJava_Acknowledgement,
              MessageType.JavaToPython_Reset, MessageType.JavaToPython_Acknowledgement, MessageType.PythonToJavaCount,
              threadNamePrefix);
        dataInputStreamReceiver = new PcDataInputStreamReceiver(getLogger(), LogMessages.PythonCommunications,
                                                                threadNamePrefix, this);
    }

    @Override
    protected DataInputStreamReceiver getDataInputStreamReceiver()
    {
        return dataInputStreamReceiver;
    }

    @Override
    protected boolean onConnect() throws Exception
    {
        if (super.onConnect())
        {
            sendAsync(MessageType.JavaToPython_PitchReq);
            return true;
        }

        return false;
    }
}
