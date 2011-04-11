package melmac.core.comms;

import melmac.core.logging.LogMessages;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import melmac.core.logging.Logger;

public abstract class DataInputStreamCommunications extends CommunicationsBase
{
    protected final class DataStreams
    {
        public final DataInputStream dataInputStream;
        public final DataOutputStream dataOutputStream;

        public DataStreams(DataInputStream dataInputStream, DataOutputStream dataOutputStream)
        {
            this.dataInputStream = dataInputStream;
            this.dataOutputStream = dataOutputStream;
        }
    }
    private volatile DataOutputStream dataOutputStream;

    public DataInputStreamCommunications(Logger logger, LogMessages logMessages, int inAcknowledgementMessageType,
                                         String threadNamePrefix)
    {
        super(logger, logMessages, inAcknowledgementMessageType, threadNamePrefix, true);
    }

    protected abstract DataStreams getDataStreams();

    @Override
    protected boolean onConnect() throws Exception
    {
        DataStreams dataStreams = getDataStreams();

        if (dataStreams == null)
        {
            return false;
        }

        getDataInputStreamReceiver().setDataInputStream(dataStreams.dataInputStream);

        synchronized (this)
        {
            dataOutputStream = dataStreams.dataOutputStream;
        }

        return true;
    }

    @Override
    protected void onDisconnect() throws Exception
    {
        getDataInputStreamReceiver().closeDataInputStream();

        synchronized (this)
        {
            if (dataOutputStream != null)
            {
                dataOutputStream.close();
            }
        }
    }

    @Override
    protected synchronized void sendEnd() throws Exception
    {
        dataOutputStream.flush();
    }

    @Override
    protected synchronized void sendInt(int value) throws Exception
    {
        dataOutputStream.writeInt(value);
    }

    @Override
    protected void onStart() throws Exception
    {
        getDataInputStreamReceiver().start();
        super.onStart();
    }

    @Override
    protected void onStop() throws Exception
    {
        getDataInputStreamReceiver().stop();
        super.onStop();
    }

    protected abstract DataInputStreamReceiver getDataInputStreamReceiver();
}
