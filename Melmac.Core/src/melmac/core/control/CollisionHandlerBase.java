package melmac.core.control;

import melmac.core.comms.Communications;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.threading.ProcessBase;

public abstract class CollisionHandlerBase extends ProcessBase
{
    private final int[] argBuffer = new int[1];
    private final Communications communications;
    private final int collisionMessageType;

    protected CollisionHandlerBase(Logger logger, Communications communications, int collisionMessageType)
    {
        super(logger, LogMessage.CHrStarted, LogMessage.CHrStopped);
        this.communications = communications;
        this.collisionMessageType = collisionMessageType;
    }

    public synchronized void handleCollision(int sensorId)
    {
        if (!isRunning())
        {
            return;
        }

        try
        {
            argBuffer[0] = sensorId;
            communications.sendAsync(collisionMessageType, argBuffer, 1);
        }
        catch (Exception exception)
        {
            getLogger().log(true, Severity.Exception, LogMessage.EColHlrSnd);
        }
    }
}
