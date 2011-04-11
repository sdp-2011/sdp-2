package melmac.simulator;

import java.io.IOException;
import melmac.core.comms.MessageType;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.threading.ProcessBase;
import melmac.core.world.PositionInfo;
import melmac.simulator.bodies.Ball;
import melmac.simulator.bodies.Pitch;
import melmac.simulator.comms.MockNxtJavaCommunications;
import melmac.simulator.comms.MockPythonJavaCommunications;
import melmac.simulator.comms.ResetMessageHandler;
import melmac.simulator.comms.nxthandlers.*;
import melmac.simulator.comms.pythonhandlers.*;
import melmac.simulator.bodies.Robot;

public final class Player extends ProcessBase
{
    private final MockPythonJavaCommunications pythonJavaCommunications;
    private final MockNxtJavaCommunications nxtJavaCommunications;
    private final Robot robot;
    private volatile boolean sendingFrames;

    public Player(Logger logger, int basePort, Robot robot, Ball ball) throws IOException
    {
        super(logger, LogMessage.PlyStarted, LogMessage.PlyStopped);
        this.pythonJavaCommunications = new MockPythonJavaCommunications(logger, basePort);
        this.nxtJavaCommunications = new MockNxtJavaCommunications(logger, basePort + 1);
        this.robot = robot;
        setPythonMessageHandlers(pythonJavaCommunications);
        setNxtMessageHandlers(nxtJavaCommunications, ball);
    }

    private void setPythonMessageHandlers(MockPythonJavaCommunications mockPythonJavaCommunications)
    {
        mockPythonJavaCommunications.setMessageHandler(MessageType.JavaToPython_PitchReq, new PitchReqMessageHandler(
            this));
        mockPythonJavaCommunications.setMessageHandler(MessageType.JavaToPython_Reset, new ResetMessageHandler(this));
        mockPythonJavaCommunications.setMessageHandler(MessageType.JavaToPython_StartFrames, new StartFramesMessageHandler(
            this));
        mockPythonJavaCommunications.setMessageHandler(MessageType.JavaToPython_StopFrames, new StopFramesMessageHandler(
            this));
    }

    private void setNxtMessageHandlers(MockNxtJavaCommunications mockNxtJavaCommunications, Ball ball)
    {
        mockNxtJavaCommunications.setMessageHandler(MessageType.JavaToNxt_CurvedMove, new CurvedMoveMessageHandler(this));
        mockNxtJavaCommunications.setMessageHandler(MessageType.JavaToNxt_Kick, new KickMessageHandler(this, ball));
        mockNxtJavaCommunications.setMessageHandler(MessageType.JavaToNxt_Move, new MoveMessageHandler(this));
        mockNxtJavaCommunications.setMessageHandler(MessageType.JavaToNxt_Reset, new ResetMessageHandler(this));
        mockNxtJavaCommunications.setMessageHandler(MessageType.JavaToNxt_Spin, new SpinMessageHandler(this));
        mockNxtJavaCommunications.setMessageHandler(MessageType.JavaToNxt_Stop, new StopMessageHandler(this));
    }

    @Override
    protected void onStart() throws Exception
    {
        super.onStart();
        pythonJavaCommunications.start();
        nxtJavaCommunications.start();
    }

    @Override
    protected void onStop() throws Exception
    {
        super.onStop();
        pythonJavaCommunications.stop();
        nxtJavaCommunications.stop();
    }

    public Robot getRobot()
    {
        return robot;
    }

    public void sendPitchInfo() throws Exception
    {
        pythonJavaCommunications.sendAsync(MessageType.PythonToJava_Pitch, new int[]
            {
                (int) Pitch.INTERNAL_WIDTH, (int) Pitch.INTERNAL_HEIGHT, (int) Pitch.GOAL_TOP, (int) Pitch.GOAL_BOTTOM,
                (int) Pitch.GOAL_TOP,
                (int) Pitch.GOAL_BOTTOM
            }, 6);
    }

    public void sendCollision(int sensorId) throws Exception
    {
        nxtJavaCommunications.sendAsync(MessageType.NxtToJava_Collision, new int[]
            {
                0
            }, 1);
    }

    public synchronized void setSendingFrames(boolean value)
    {
        sendingFrames = value;
    }

    public void reset() throws Exception
    {
        nxtJavaCommunications.reset();
        pythonJavaCommunications.reset();
    }

    public void sendFrame(PositionInfo positionInfo) throws Exception
    {
        boolean sendMessage;

        synchronized (this)
        {
            sendMessage = sendingFrames;
        }

        if (sendMessage)
        {
            pythonJavaCommunications.sendAsync(MessageType.PythonToJava_Frame, new int[]
                {
                    positionInfo.getBall().getPosition().getX(),
                    positionInfo.getBall().getPosition().getY(),
                    positionInfo.getBlueRobot().getPosition().getX(),
                    positionInfo.getBlueRobot().getPosition().getY(),
                    positionInfo.getYellowRobot().getPosition().getX(),
                    positionInfo.getYellowRobot().getPosition().getY(),
                    positionInfo.getBlueRobot().getDirection().getX(),
                    positionInfo.getBlueRobot().getDirection().getY(),
                    positionInfo.getYellowRobot().getDirection().getX(),
                    positionInfo.getYellowRobot().getDirection().getY(),
                    positionInfo.getBall().isTrusted() ? 0 : 1,
                    positionInfo.getBlueRobot().isTrusted() ? 0 : 1,
                    positionInfo.getYellowRobot().isTrusted() ? 0 : 1,
                    positionInfo.getBall().getVelocity().getX(),
                    positionInfo.getBall().getVelocity().getY(),
                    positionInfo.getBlueRobot().getVelocity().getX(),
                    positionInfo.getBlueRobot().getVelocity().getY(),
                    positionInfo.getYellowRobot().getVelocity().getX(),
                    positionInfo.getYellowRobot().getVelocity().getY()
                }, 19);
        }
    }
}
