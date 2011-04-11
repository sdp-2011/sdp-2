package melmac.core.comms;

import melmac.core.logging.LogMessage;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.threading.ProcessBase;
import melmac.core.utils.Constants;

public abstract class LightReceiver extends ProcessBase
{
    private static final int SINGLE_DOUBLE_TIME_BOUNDARY = Constants.LIGHT_TIME_BASE * 3 / 2;
    private static final int DOUBLE_TRIPLE_TIME_BOUNDARY = Constants.LIGHT_TIME_BASE * 5 / 2;
    private final int lightOnLevel;
    private final int lightLevelMargin;
    private final LightCommunications lightCommunications;
    private final int[] argBuffer = new int[Constants.ARGUMENT_BUFFER_SIZE];
    private volatile int lastEventStart;
    private volatile int multiple;
    private volatile int currentValue;
    private volatile int messageType;
    private volatile boolean messageTypeSet;
    private volatile int argCount;
    private volatile int argIndex;

    protected LightReceiver(Logger logger, LogMessages logMessages, int lightOnLevel, int lightLevelMargin,
                            LightCommunications lightCommunications)
    {
        super(logger, logMessages.RecStrt, logMessages.RecStpd);
        this.lightOnLevel = lightOnLevel;
        this.lightLevelMargin = lightLevelMargin;
        this.lightCommunications = lightCommunications;
    }

    private boolean isLightOn(int value)
    {
        return value >= (lightOnLevel - lightLevelMargin) && value <= (lightOnLevel + lightLevelMargin);
    }

    public void stateChanged(int oldValue, int newValue)
    {
        try
        {
            // Ignore the initial value assignment when the listener starts up
            if (oldValue == 0 || !isRunning())
            {
                return;
            }

            // NOTE: long arithmetic not supported on RCX
            final int nextEventStart = (int) System.currentTimeMillis();

            synchronized (this)
            {
                if (!isLightOn(oldValue) && isLightOn(newValue))
                {
                    lightCommunications.otherSeen();

                    if (lightCommunications.isReceiving())
                    {
                        final int lastEventDuration = nextEventStart - lastEventStart;
                        lastEventStart = nextEventStart;

                        if (lastEventDuration < SINGLE_DOUBLE_TIME_BOUNDARY)
                        {
                            // Next bit
                        }
                        else if (lastEventDuration < DOUBLE_TRIPLE_TIME_BOUNDARY)
                        {
                            // Next value
                            if (!messageTypeSet)
                            {
                                // Message type
                                messageType = currentValue;
                                messageTypeSet = true;
                            }
                            else if (argCount == -1)
                            {
                                // Number of arguments
                                if (argCount > Constants.ARGUMENT_BUFFER_SIZE)
                                {
                                    getLogger().log(false, Severity.Severe, LogMessage.ETooMnyArg);
                                    throw new Exception();
                                }

                                argCount = currentValue;
                            }
                            else
                            {
                                // Argument value
                                argBuffer[argIndex] = currentValue;
                                argIndex++;
                            }

                            multiple = 1;
                            currentValue = 0;
                        }
                        else
                        {
                            getLogger().log(false, Severity.Severe, LogMessage.EBadOffDur);
                            throw new Exception();
                        }
                    }
                    else if (lightCommunications.startReceive())
                    {
                        lastEventStart = nextEventStart;
                        multiple = 1;
                        currentValue = 0;
                        messageType = -1;
                        messageTypeSet = false;
                        argCount = -1;
                        argIndex = 0;
                    }
                }
                else if (isLightOn(oldValue) && !isLightOn(newValue))
                {
                    final int lastEventDuration = nextEventStart - lastEventStart;
                    lastEventStart = nextEventStart;

                    if (lastEventDuration < SINGLE_DOUBLE_TIME_BOUNDARY)
                    {
                        if (argIndex == argCount)
                        {
                            // End of message
                            lightCommunications.completeReceive(messageType, argBuffer, argCount);
                        }
                        else
                        {
                            // Bit 0
                        }
                    }
                    else if (lastEventDuration < DOUBLE_TRIPLE_TIME_BOUNDARY)
                    {
                        // Bit 1
                        currentValue += multiple;
                    }
                    else
                    {
                        getLogger().log(false, Severity.Severe, LogMessage.EBadOnDura);
                        throw new Exception();
                    }

                    multiple *= 2;
                }
            }
        }
        catch (Exception exception)
        {
            lightCommunications.disconnect();
        }
    }

    @Override
    protected void onStart() throws Exception
    {
        lastEventStart = 0;
        multiple = 0;
        currentValue = 0;
        messageType = 0;
        messageTypeSet = false;
        argCount = 0;
        argIndex = 0;
        super.onStart();
    }
}
