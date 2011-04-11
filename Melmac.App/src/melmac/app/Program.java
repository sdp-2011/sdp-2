package melmac.app;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import lejos.pc.comm.NXTCommFactory;
import melmac.app.comms.MockNxtCommunications;
import melmac.app.comms.NxtCommunications;
import melmac.core.comms.Communications;
import melmac.app.comms.handlers.CollisionMessageHandler;
import melmac.core.comms.handlers.LogMessageHandler;
import melmac.app.control.CollisionHandler;
import melmac.app.control.RemoteController;
import melmac.app.ui.HatTrickUi;
import melmac.core.logging.ConsoleLogger;
import melmac.core.control.SimpleAgent;
import melmac.core.comms.MessageType;
import melmac.core.interfaces.Controller;
import melmac.core.interfaces.VisionInfoProvider;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.logging.Source;
import melmac.core.strategies.*;
import melmac.core.threading.NamedThreadFactory;
import melmac.core.threading.ProcessBase;
import melmac.core.threading.ThreadFactory;
import melmac.core.world.WorldStateProvider;
import melmac.vision.Vision;

public final class Program extends ProcessBase
{
    public static void main(String[] args) throws Exception
    {
        ThreadFactory.setFactory(new NamedThreadFactory());
        Logger logger = new ConsoleLogger(Source.Java);

        String visionHost = InetAddress.getLocalHost().getHostName();
        int visionPort;
        boolean defaultToYellow = false;
        Communications nxtCommunications;

        if (args.length > 0)
        {
            defaultToYellow = args[0].equals("Yellow");
            visionPort = defaultToYellow ? 10000 : 20000;
            nxtCommunications = new MockNxtCommunications(logger, InetAddress.getLocalHost().getHostName(), visionPort
                                                                                                            + 1);
        }
        else
        {
            visionHost = "watten";
            visionPort = 5000;
            nxtCommunications = new NxtCommunications(logger, NXTCommFactory.BLUETOOTH, "NXT-G2", "00165308DFAB");
            //nxtCommunications = new NxtCommunications(NXTCommFactory.BLUETOOTH, "NXT-DR", "001653030786");
        }
        VisionInfoProvider vision = new Vision(logger, visionHost, visionPort);
        CollisionStrategy collisionStrategy = new CollisionStrategy();
        CollisionHandler collisionHandler = new CollisionHandler(collisionStrategy);
        nxtCommunications.setMessageHandler(MessageType.NxtToJava_Collision, new CollisionMessageHandler(
            collisionHandler));
        nxtCommunications.setMessageHandler(MessageType.NxtToJava_Log, new LogMessageHandler(logger));
        Controller controller = new RemoteController(logger, nxtCommunications);

        // create strategy list
		// NOTE: DO NOT INCLUDE DoNothing() or Stop() here!!!
        Set<Strategy> strategies = new HashSet<Strategy>();
        strategies.add(new AimToShoot());
        //strategies.add(new ApproachBall());
        strategies.add(new BlockOpponent());
        //strategies.add(new FaceBall());
        strategies.add(new InterceptBall());
        //strategies.add(new JustKick());
        strategies.add(new KickDirect());
        strategies.add(new KickOffWall());
        strategies.add(new MoveBehindBall());
        strategies.add(new MoveStraightToBall());
        strategies.add(new MoveToAvoidOpponent());
        strategies.add(new Stop());
        //strategies.add(new MoveToOwnGoal());
        //strategies.add(new DribbleStraightToOpponentGoal());

        // TODO: Getting selected even if no collision: strategies.add(collisionStrategy);

        final JFrame jFrame = new JFrame("Hat-Trick UI");
        HatTrickUi hatTrickUi = new HatTrickUi(logger, controller, defaultToYellow);
        jFrame.add(hatTrickUi);
        jFrame.setSize(new java.awt.Dimension(600, 480));
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final Object closingLock = new Object();

        WorldStateProvider worldStateProvider = new WorldStateProvider(vision, hatTrickUi);
        SimpleAgent simpleAgent = new SimpleAgent(logger, worldStateProvider, controller, new Stop(), strategies);

        Program program = new Program(logger, nxtCommunications, vision, simpleAgent);
        hatTrickUi.setProgram(program);

        jFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                synchronized (closingLock)
                {
                    closingLock.notifyAll();
                }
            }
        });

        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                jFrame.setVisible(true);
            }
        });

        synchronized (closingLock)
        {
            // Wait for the UI to close
            closingLock.wait();
        }

        program.stop();
    }
    private final Communications nxtCommunications;
    private final VisionInfoProvider vision;
    private final SimpleAgent simpleAgent;

    public Program(Logger logger, Communications nxtCommunications, VisionInfoProvider vision, SimpleAgent simpleAgent)
    {
        super(logger, LogMessage.JavStarted, LogMessage.JavStopped);
        this.nxtCommunications = nxtCommunications;
        this.vision = vision;
        this.simpleAgent = simpleAgent;
    }

    @Override
    protected void onStart() throws Exception
    {
        super.onStart();

        nxtCommunications.start();
        simpleAgent.start();
        vision.start();
    }

    @Override
    protected void onStop() throws Exception
    {
        super.onStop();

        vision.stop();
        simpleAgent.stop();
        nxtCommunications.stop();
    }
}
