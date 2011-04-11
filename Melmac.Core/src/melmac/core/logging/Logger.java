package melmac.core.logging;

public interface Logger
{

    void log(boolean broadcast, int severity, int logMessage);

    void log(boolean broadcast, int severity, int logMessage, int arg0);

    void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount);

    void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount, int source);
}
