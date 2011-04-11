package melmac.slaveblock.logging;

import josx.platform.rcx.LCD;
import josx.platform.rcx.Sound;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.logging.VoidLogger;
import melmac.core.threading.AsyncProcess;

public final class AsyncScreenLogger extends AsyncProcess implements Logger
{
    private volatile boolean valueWaiting;
    private volatile int severity;
    private volatile int logMessage;

    public AsyncScreenLogger()
    {
        // TODO: Better log message types
        super(new VoidLogger(), LogMessage.AgtStarted, LogMessage.AgtStopped, LogMessage.Agent_Died,
              LogMessage.AgtSgnlled, null);
    }

    protected synchronized boolean execute() throws Exception
    {
        if (!valueWaiting)
        {
            return true;
        }

        if (severity == Severity.Severe || severity == Severity.Warning || severity == Severity.Exception)
        {
            Sound.beepSequence();
        }

        LCD.showNumber(logMessage);
        valueWaiting = false;
        this.notifyAll();
        return true;
    }

    protected synchronized boolean shouldWait() throws Exception
    {
        return !valueWaiting;
    }

    private synchronized void log(int severity, int logMessage)
    {
        while (valueWaiting)
        {
            if (!waitCheckRunning())
            {
                return;
            }
        }

        this.logMessage = logMessage;
        this.severity = severity;
        this.valueWaiting = true;
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
