package melmac.core.logging;

import melmac.core.utils.Constants;

public abstract class LoggerBase implements Logger
{

    private final int source;
    private final int[] argBuffer = new int[1];

    protected LoggerBase(int source)
    {
        this.source = source;
    }

    @Override
    public void log(boolean broadcast, int severity, int logMessage)
    {
        log(broadcast, severity, logMessage, Constants.EMPTY_BUFFER, 0, source);
    }

    @Override
    public synchronized void log(boolean broadcast, int severity, int logMessage, int arg0)
    {
        argBuffer[0] = arg0;
        log(broadcast, severity, logMessage, argBuffer, 1, source);
    }

    @Override
    public void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount)
    {
        log(broadcast, severity, logMessage, argBuffer, argCount, source);
    }

    @Override
    public abstract void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount, int source);
}
