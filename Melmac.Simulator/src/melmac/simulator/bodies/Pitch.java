package melmac.simulator.bodies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import melmac.core.logging.Logger;
import melmac.simulator.CollisionHandler;
import melmac.simulator.Player;
import melmac.simulator.graphics.Renderable;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.FixedJoint;
import net.phys2d.raw.World;
import net.phys2d.raw.strategies.BruteCollisionStrategy;

public final class Pitch implements Renderable
{
    public static final Color COLOR = new Color(0, 150, 0);
    public static final float INTERNAL_WIDTH = 580;
    public static final float INTERNAL_HEIGHT = 295;
    public static final float GOAL_TOP = 75;
    public static final float GOAL_BOTTOM = 225;
    public static final float PIXELS_PER_CM = INTERNAL_WIDTH / 250f;
    private static final int ITERATIONS_PER_STEP = 1000;
    private final World world;
    private final List<Renderable> renderables = new ArrayList<Renderable>(9);

    public Pitch(Ball ball, Logger logger, Player bluePlayer, Player yellowPlayer)
    {
        world = new World(new Vector2f(), ITERATIONS_PER_STEP, new BruteCollisionStrategy());
        world.addListener(new CollisionHandler(logger, bluePlayer, yellowPlayer));
        world.disableRestingBodyDetection();
        // world.setDamping(1.0f); // Amount of "air friction"

        Robot yellowRobot = yellowPlayer.getRobot();
        Robot blueRobot = bluePlayer.getRobot();

        KickerProng yellowLeftProng = new KickerProng(yellowRobot, true, false);
        KickerProng yellowRightProng = new KickerProng(yellowRobot, false, false);
        KickerProng blueLeftProng = new KickerProng(blueRobot, true, true);
        KickerProng blueRightProng = new KickerProng(blueRobot, false, true);

        add(new Wall.TopWall());
        add(new Wall.BottomWall());
        add(new Wall.LeftTopWall());
        add(new Wall.LeftBottomWall());
        add(new Wall.RightTopWall());
        add(new Wall.RightBottomWall());
        add(new Wall.LeftGoal());
        add(new Wall.RightGoal());
        add(yellowRobot);
        add(blueRobot);
        add(ball);
        add(yellowLeftProng);
        add(yellowRightProng);
        add(blueLeftProng);
        add(blueRightProng);

        world.add(new FixedJoint(yellowRobot, yellowLeftProng));
        world.add(new FixedJoint(yellowRobot, yellowRightProng));
        world.add(new FixedJoint(blueRobot, blueLeftProng));
        world.add(new FixedJoint(blueRobot, blueRightProng));
    }

    private void add(Body body)
    {
        world.add(body);

        if (body instanceof Renderable)
        {
            renderables.add((Renderable) body);
        }
    }

    public void render(Graphics2D graphics2D)
    {
        graphics2D.setColor(COLOR);
        graphics2D.fill(new Rectangle2D.Double(0, 0, graphics2D.getClipBounds().getWidth(), graphics2D.getClipBounds().getHeight()));

        for (Renderable renderable : renderables)
        {
            renderable.render(graphics2D);
        }
    }

    public void step()
    {
        world.step();
    }
}
