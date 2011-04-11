package melmac.masterblock.logging;

import melmac.core.comms.CommunicationsBase;
import melmac.core.logging.Source;
import melmac.core.logging.CommunicationsLogger;
import melmac.core.logging.Logger;

public final class JavaLogger extends CommunicationsLogger
{

    public JavaLogger(CommunicationsBase communicationsBase, int logMessageType, Logger backoffLogger)
    {
        super(Source.Nxt, communicationsBase, logMessageType, backoffLogger);
    }
}
