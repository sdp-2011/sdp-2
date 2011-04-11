package melmac.core.comms;

import java.net.SocketTimeoutException;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class PcDataInputStreamReceiver extends DataInputStreamReceiver
{
    public PcDataInputStreamReceiver(Logger logger, LogMessages logMessages, String threadNamePrefix,
                                     DataInputStreamCommunications dataInputStreamCommunications)
    {
        super(logger, logMessages, threadNamePrefix, dataInputStreamCommunications);
    }

    @Override
    protected boolean onException(Exception exception)
    {
        if (!(exception instanceof SocketTimeoutException))
        {
            dataInputStreamCommunications.disconnect();
        }

        return false;
    }
}
