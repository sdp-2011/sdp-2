package melmac.core.comms;

import melmac.core.logging.LogMessages;
import java.io.DataInputStream;
import melmac.core.logging.Logger;
import melmac.core.threading.AsyncProcess;

public abstract class DataInputStreamReceiver extends AsyncProcess
{
    protected final DataInputStreamCommunications dataInputStreamCommunications;
    private volatile DataInputStream dataInputStream;

    protected DataInputStreamReceiver(Logger logger, LogMessages logMessages, String threadNamePrefix,
                                      DataInputStreamCommunications dataInputStreamCommunications)
    {
        super(logger, logMessages.RecStrt, logMessages.RecStpd, logMessages.RecDied, logMessages.RecSgnd,
              threadNamePrefix + "-DataInputStreamReceiver");
        this.dataInputStreamCommunications = dataInputStreamCommunications;
    }

    @Override
    protected synchronized boolean shouldWait() throws Exception
    {
        return dataInputStream == null;
    }

    @Override
    protected boolean execute() throws Exception
    {
        int messageType;
        int argCount;

        synchronized (this)
        {
            if (dataInputStream == null)
            {
                return true;
            }
        }

        // Ideally this would be protected inside a sync block but since .readInt() will block indefinitely on a
        // Bluetooth connection, it must be left unprotected and we have to cross our fingers and hope no concurrency
        // problems will occur inside DataInputStream.
        messageType = dataInputStream.readInt();
        int[] argBuffer;

        synchronized (this)
        {
            if (dataInputStream == null)
            {
                return true;
            }

            argCount = dataInputStream.readInt();
            argBuffer = new int[argCount];

            for (int index = 0; index < argCount; index++)
            {
                argBuffer[index] = dataInputStream.readInt();
            }
        }

        if (dataInputStreamCommunications.startReceive())
        {
            dataInputStreamCommunications.completeReceive(messageType, argBuffer, argCount);
        }

        return true;
    }

    public synchronized void setDataInputStream(DataInputStream value)
    {
        this.dataInputStream = value;
        this.notifyAll();
    }

    public synchronized void closeDataInputStream() throws Exception
    {
        if (dataInputStream != null)
        {
            dataInputStream.close();
            dataInputStream = null;
        }
    }
}
