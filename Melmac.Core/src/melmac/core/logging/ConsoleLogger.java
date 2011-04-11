package melmac.core.logging;

public final class ConsoleLogger extends LoggerBase
{

    public ConsoleLogger(int source)
    {
        super(source);
    }

    @Override
    public synchronized void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount, int source)
    {
        System.out.print(System.currentTimeMillis());
        System.out.print(" : ");
        System.out.print(SourceExt.toString(source));
        System.out.print(" : ");
        System.out.print(SeverityExt.toString(severity));
        System.out.print(" : ");
        System.out.print(LogMessageExt.toString(logMessage, false));

        for (int index = 0; index < argCount; index++)
        {
            System.out.print(" : ");
            System.out.print(argBuffer[index]);
        }

        System.out.println();
    }
}
