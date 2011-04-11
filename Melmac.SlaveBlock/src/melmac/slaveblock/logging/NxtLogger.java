package melmac.slaveblock.logging;

import melmac.core.comms.CommunicationsBase;
import melmac.core.comms.MessageType;
import melmac.core.logging.CommunicationsLogger;
import melmac.core.logging.Logger;
import melmac.core.logging.Source;

public final class NxtLogger extends CommunicationsLogger
{

    public NxtLogger(CommunicationsBase communicationsBase, Logger backoffLogger)
    {
        super(Source.Rcx, communicationsBase, MessageType.RcxToNxt_Log, backoffLogger);
    }
}
