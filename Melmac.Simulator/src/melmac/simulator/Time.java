package melmac.simulator;

import java.util.Queue;
import java.util.LinkedList;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.threading.AsyncProcess;
import melmac.core.world.BallInfo;
import melmac.core.world.PositionInfo;
import melmac.core.world.RobotInfo;
import melmac.simulator.bodies.Ball;
import melmac.simulator.bodies.Pitch;
import melmac.simulator.graphics.Frame;

public final class Time extends AsyncProcess
{
    private static final int VISION_DELAY_FRAMES = 5;
    private static final float MIN_FRAME_SLEEP_TIME = 1f;
    private final Queue<PositionInfo> visionQueue;
    private final Pitch pitch;
    private final Player yellowPlayer;
    private final Player bluePlayer;
    private final Ball ball;
    private final Frame frame;
    private volatile float framesPerSecond = 25f;
    private volatile long lastExecutionTime;

    public Time(Logger logger, Pitch pitch, Player yellowPlayer, Player bluePlayer, Ball ball, Frame frame)
    {
        super(logger, LogMessage.TimStarted, LogMessage.TimStopped, LogMessage.Time__Died, LogMessage.TimSgnlled, "Time");
        this.visionQueue = new LinkedList<PositionInfo>();
        this.pitch = pitch;
        this.yellowPlayer = yellowPlayer;
        this.bluePlayer = bluePlayer;
        this.ball = ball;
        this.frame = frame;
    }

    @Override
    protected boolean execute() throws Exception
    {
        pitch.step();
        PositionInfo newPositionInfo = new PositionInfo(
            new RobotInfo(bluePlayer.getRobot().getPositionPoint(), bluePlayer.getRobot().getVelocityPoint(), true, bluePlayer.getRobot().getDirectionPoint()),
            new RobotInfo(yellowPlayer.getRobot().getPositionPoint(), yellowPlayer.getRobot().getVelocityPoint(), true, yellowPlayer.getRobot().getDirectionPoint()),
            new BallInfo(ball.getPositionPoint(), ball.getVelocityPoint(), true));
        visionQueue.add(newPositionInfo);
        frame.repaint();

        if (visionQueue.size() > VISION_DELAY_FRAMES)
        {
            PositionInfo oldPositionInfo = visionQueue.remove();
            bluePlayer.sendFrame(oldPositionInfo);
            yellowPlayer.sendFrame(oldPositionInfo);
        }

        // Limit frame rate
        int sleepTime;

        synchronized (this)
        {
            float lastFrameDurationMs = System.currentTimeMillis() - lastExecutionTime;
            sleepTime = (int) Math.max(MIN_FRAME_SLEEP_TIME, 1000f / framesPerSecond - lastFrameDurationMs);
        }

        Thread.sleep(sleepTime);

        synchronized (this)
        {
            lastExecutionTime = System.currentTimeMillis();
        }

        return true;
    }

    @Override
    protected void onStart() throws Exception
    {
        super.onStart();

        synchronized (this)
        {
            lastExecutionTime = System.currentTimeMillis();
        }
    }

    @Override
    protected boolean shouldWait() throws Exception
    {
        return false;
    }

    public synchronized void setFramesPerSecond(float value)
    {
        framesPerSecond = value;
    }
}
