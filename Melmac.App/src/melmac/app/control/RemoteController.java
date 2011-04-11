package melmac.app.control;

import melmac.core.comms.Communications;
import melmac.core.comms.MessageType;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.interfaces.Controller;

public final class RemoteController implements Controller
{

    private final Communications nxtCommunications;
    private final Logger logger;

    public RemoteController(Logger logger, Communications nxtCommunications)
    {
        this.logger = logger;
        this.nxtCommunications = nxtCommunications;
    }

    @Override
    public void move(int angle, int power)
    {
        try
        {
            nxtCommunications.sendAsync(MessageType.JavaToNxt_Move, new int[]
                {
                    angle, power
                }, 2);
        }
        catch (Exception e)
        {
            logger.log(true, Severity.Severe, LogMessage.ECmdSndFai, 0);
        }
    }

    @Override
    public void spin(int angle, int power)
    {
        try
        {
            nxtCommunications.sendSync(MessageType.JavaToNxt_Spin, new int[]
                {
                    angle, power
                }, 2);
        }
        catch (Exception e)
        {
            logger.log(true, Severity.Severe, LogMessage.ECmdSndFai, 0);
        }
    }

    @Override
    public void stop()
    {
        try
        {
            nxtCommunications.sendSync(MessageType.JavaToNxt_Stop);
        }
        catch (Exception e)
        {
            logger.log(true, Severity.Severe, LogMessage.ECmdSndFai, 0);
        }
    }

    @Override
    public void kick()
    {
        try
        {
            nxtCommunications.sendSync(MessageType.JavaToNxt_Kick);
        }
        catch (Exception e)
        {
            logger.log(true, Severity.Severe, LogMessage.ECmdSndFai, 0);
        }
    }

    @Override
    public void curvedMove(int directionAngle, int Xdistance, int Ydistance, int spinAngle, int power)
    {
        try
        {
            nxtCommunications.sendSync(MessageType.JavaToNxt_CurvedMove, new int[]
                {
                    directionAngle, Xdistance, Ydistance, spinAngle, power
                }, 3);
        }
        catch (Exception e)
        {
            logger.log(true, Severity.Severe, LogMessage.ECmdSndFai, 0);
        }
    }
}
