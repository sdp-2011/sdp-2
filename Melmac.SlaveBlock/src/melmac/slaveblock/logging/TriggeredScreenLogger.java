package melmac.slaveblock.logging;

import josx.platform.rcx.LCD;
import josx.platform.rcx.Sound;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.utils.Constants;

public final class TriggeredScreenLogger implements Logger
{
    private volatile boolean messageWaiting;
    private volatile int severity;
    private volatile int logMessage;

    public synchronized void waitForMessage() throws Exception
    {
        if (!messageWaiting)
        {
            this.wait(Constants.MONITOR_WAIT_TIMEOUT);
        }

        if (messageWaiting)
        {
            if (severity == Severity.Severe || severity == Severity.Warning || severity == Severity.Exception)
            {
                Sound.beepSequence();
            }

            LCD.showNumber(logMessage);
            messageWaiting = false;
        }
    }

    private synchronized void log(int severity, int logMessage)
    {
        // NOTE: This will overwrite any pending message
        this.logMessage = logMessage;
        this.severity = severity;
        this.messageWaiting = true;
        this.notifyAll();
    }

    public void log(boolean broadcast, int severity, int logMessage)
    {
        log(severity, logMessage);
    }

    public void log(boolean broadcast, int severity, int logMessage, int arg0)
    {
        log(severity, logMessage);
    }

    public void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount)
    {
        log(severity, logMessage);
    }

    public void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount, int source)
    {
        log(severity, logMessage);
    }
}
