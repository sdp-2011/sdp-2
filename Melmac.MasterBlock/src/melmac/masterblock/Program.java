package melmac.masterblock;

import melmac.masterblock.control.CollisionHandler;
import melmac.masterblock.control.Controller;
import melmac.masterblock.comms.handlers.SpinMessageHandler;
import melmac.masterblock.comms.handlers.MoveMessageHandler;
import melmac.masterblock.comms.handlers.StopMessageHandler;
import melmac.core.logging.LogMessage;
import melmac.masterblock.comms.JavaCommunications;
import melmac.masterblock.logging.JavaLogger;
import melmac.masterblock.logging.ScreenLogger;
import lejos.nxt.SensorPort;
import melmac.core.comms.MessageType;
import melmac.core.comms.handlers.LogMessageHandler;
import melmac.core.comms.handlers.ResetMessageHandler;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.masterblock.comms.RcxCommunications;
import melmac.masterblock.comms.handlers.CollisionMessageHandler;
import melmac.masterblock.comms.handlers.CurvedMoveMessageHandler;
import melmac.masterblock.comms.handlers.KickMessageHandler;

public final class Program
{

    public static void main(String[] args) throws Exception
    {
        Logger logger = new ScreenLogger(0, 7);
        JavaCommunications javaCommunications = new JavaCommunications(logger);
        logger = new JavaLogger(javaCommunications, MessageType.NxtToJava_Log, logger);
        RcxCommunications rcxCommunications = new RcxCommunications(logger, SensorPort.S1);
        Controller controller = new Controller();
        CollisionHandler collisionHandler = new CollisionHandler(logger, new SensorPort[]
            {
                SensorPort.S2,
                SensorPort.S3,
                SensorPort.S4
            }, controller, javaCommunications);
        rcxCommunications.setMessageHandler(MessageType.RcxToNxt_Collision,
                                            new CollisionMessageHandler(collisionHandler));
        rcxCommunications.setMessageHandler(MessageType.RcxToNxt_Log, new LogMessageHandler(logger));
        ResetMessageHandler resetMessageHandler = new ResetMessageHandler();
        javaCommunications.setMessageHandler(MessageType.JavaToNxt_Kick, new KickMessageHandler(rcxCommunications));
        javaCommunications.setMessageHandler(MessageType.JavaToNxt_Move, new MoveMessageHandler(controller));
        javaCommunications.setMessageHandler(MessageType.JavaToNxt_Reset, resetMessageHandler);
        javaCommunications.setMessageHandler(MessageType.JavaToNxt_Spin, new SpinMessageHandler(controller));
        javaCommunications.setMessageHandler(MessageType.JavaToNxt_Stop, new StopMessageHandler(controller));
        javaCommunications.setMessageHandler(MessageType.JavaToNxt_CurvedMove, new CurvedMoveMessageHandler(controller));

        javaCommunications.start();
        rcxCommunications.start();
        collisionHandler.start();

        logger.log(true, Severity.Info, LogMessage.NxtStarted);

        while (true)
        {
            resetMessageHandler.waitForReset();

            controller.stop();

            rcxCommunications.reset();
            javaCommunications.reset();

            logger.log(true, Severity.Info, LogMessage.Nxt__Reset);
        }
    }

    private Program()
    {
    }
}
