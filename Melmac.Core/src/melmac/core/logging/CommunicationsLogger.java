package melmac.core.logging;

import melmac.core.comms.CommunicationsBase;
import melmac.core.utils.Constants;

public abstract class CommunicationsLogger extends LoggerBase
{
    private final int[] argBuffer = new int[Constants.ARGUMENT_BUFFER_SIZE];
    private final CommunicationsBase communicationsBase;
    private final int logMessageType;
    private final Logger backoffLogger;

    protected CommunicationsLogger(int source, CommunicationsBase communicationsBase, int logMessageType,
                                   Logger backoffLogger)
    {
        super(source);
        this.communicationsBase = communicationsBase;
        this.logMessageType = logMessageType;
        this.backoffLogger = backoffLogger;
    }

    @Override
    public synchronized void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount,
                                 int source)
    {
        if (broadcast && communicationsBase.isClearToSend())
        {
            try
            {
                this.argBuffer[0] = source;
                this.argBuffer[1] = severity;
                this.argBuffer[2] = logMessage;

                // NOTE: System.arraycopy not available on RCX
                for (int index = 0; index < argCount; index++)
                {
                    this.argBuffer[index + 3] = argBuffer[index];
                }

                communicationsBase.sendAsync(logMessageType, this.argBuffer, argCount + 3);
            }
            catch (Exception sendException)
            {
            }
        }

        backoffLogger.log(broadcast, severity, logMessage, argBuffer, argCount, source);
    }
}
