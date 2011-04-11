package melmac.core.comms;

import melmac.core.logging.Logger;
import melmac.core.logging.LogMessages;
import melmac.core.threading.AsyncProcess;

public final class Watchdog extends AsyncProcess
{
    private final CommunicationsBase communicationsBase;

    public Watchdog(Logger logger, LogMessages logMessages, String threadNamePrefix,
                    CommunicationsBase communicationsBase)
    {
        super(logger, logMessages.WdgStrt, logMessages.WdgStpd, logMessages.WdgDied, logMessages.WdgSgnd, "Watchdog");
        this.communicationsBase = communicationsBase;
    }

    @Override
    protected boolean shouldWait() throws Exception
    {
        // Calls to connect() are always speculative so should wait every time
        return true;
    }

    @Override
    protected boolean execute() throws Exception
    {
        communicationsBase.connect();
        return true;
    }
}
