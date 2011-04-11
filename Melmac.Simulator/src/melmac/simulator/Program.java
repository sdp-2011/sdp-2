package melmac.simulator;

import java.awt.EventQueue;
import melmac.core.logging.ConsoleLogger;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.logging.Source;
import melmac.core.threading.NamedThreadFactory;
import melmac.core.threading.ThreadFactory;
import melmac.simulator.bodies.Ball;
import melmac.simulator.bodies.Pitch;
import melmac.simulator.bodies.Robot;
import melmac.simulator.graphics.Frame;

// TODO: Send collision messages when a collision happens in the simulator
public final class Program
{

    private Program()
    {
    }

    public static void main(String[] args) throws Exception
    {
        ThreadFactory.setFactory(new NamedThreadFactory());

        Logger logger = new ConsoleLogger(Source.Simulator);

        Robot blueRobot = new Robot(PlayerColour.Blue, Robot.LENGTH / 2f, Pitch.INTERNAL_HEIGHT / 2f, (float) Math.toRadians(90));
        Robot yellowRobot = new Robot(PlayerColour.Yellow, Pitch.INTERNAL_WIDTH - Robot.LENGTH / 2f, Pitch.INTERNAL_HEIGHT / 2f, (float) Math.toRadians(270));

        Ball ball = new Ball(Pitch.INTERNAL_WIDTH / 2f, Pitch.INTERNAL_HEIGHT / 2f);

        Player bluePlayer = new Player(logger, 20000, blueRobot, ball);
        Player yellowPlayer = new Player(logger, 10000, yellowRobot, ball);

        Pitch pitch = new Pitch(ball, logger, bluePlayer, yellowPlayer);

        final Frame frame = new Frame(pitch);

        Time time = new Time(logger, pitch, yellowPlayer, bluePlayer, ball, frame);

        bluePlayer.start();
        yellowPlayer.start();
        time.start();

        logger.log(true, Severity.Info, LogMessage.SimStarted);

        EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                frame.setVisible(true);
            }
        });

        frame.waitForClose();

        time.stop();
        yellowPlayer.stop();
        bluePlayer.stop();

        logger.log(true, Severity.Info, LogMessage.SimStopped);
    }
}
