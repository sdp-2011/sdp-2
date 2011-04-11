package melmac.masterblock.comms;

import melmac.core.comms.DataInputStreamCommunications;
import melmac.core.comms.DataInputStreamReceiver;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class NxtDataInputStreamReceiver extends DataInputStreamReceiver
{
    public NxtDataInputStreamReceiver(Logger logger, LogMessages logMessages, String threadNamePrefix,
                                      DataInputStreamCommunications dataInputStreamCommunications)
    {
        super(logger, logMessages, threadNamePrefix, dataInputStreamCommunications);
    }

    @Override
    protected boolean onException(Exception exception)
    {
        dataInputStreamCommunications.disconnect();
        return false;
    }
}
