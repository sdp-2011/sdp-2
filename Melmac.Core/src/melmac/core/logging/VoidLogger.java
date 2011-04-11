package melmac.core.logging;

public final class VoidLogger implements Logger
{

    @Override
    public void log(boolean broadcast, int severity, int logMessage)
    {
    }

    @Override
    public void log(boolean broadcast, int severity, int logMessage, int arg0)
    {
    }

    @Override
    public void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount)
    {
    }

    @Override
    public void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount, int source)
    {
    }
}
