package melmac.slaveblock;

import josx.platform.rcx.Motor;
import josx.platform.rcx.Sensor;
import melmac.slaveblock.comms.NxtCommunications;
import melmac.core.comms.MessageType;
import melmac.core.comms.handlers.ResetMessageHandler;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.slaveblock.comms.handlers.KickMessageHandler;
import melmac.slaveblock.control.CollisionHandler;
import melmac.slaveblock.control.Controller;
import melmac.slaveblock.logging.NxtLogger;
import melmac.slaveblock.logging.ScreenLogger;

public final class Program
{
    public static void main(String[] args) throws Exception
    {
        Logger logger = new ScreenLogger();
        NxtCommunications nxtCommunications = new NxtCommunications(logger, Sensor.S1);
        logger = new NxtLogger(nxtCommunications, logger);
        Controller controller = new Controller(Motor.A, Motor.B);
        CollisionHandler collisionHandler = new CollisionHandler(logger, new Sensor[]
            {
                Sensor.S2,
                Sensor.S3
            }, nxtCommunications);
        nxtCommunications.setMessageHandler(MessageType.NxtToRcx_Kick, new KickMessageHandler(controller));
        ResetMessageHandler resetMessageHandler = new ResetMessageHandler();
        nxtCommunications.setMessageHandler(MessageType.NxtToRcx_Reset, resetMessageHandler);

        nxtCommunications.start();
        collisionHandler.start();

        logger.log(true, Severity.Info, LogMessage.RcxStarted);

        while (true)
        {
            resetMessageHandler.waitForReset();
            nxtCommunications.reset();

            logger.log(true, Severity.Info, LogMessage.Rcx__Reset);
        }
    }

    private Program()
    {
    }
}
