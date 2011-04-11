package melmac.simulator;

import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.world.Point;
import melmac.simulator.bodies.Ball;
import melmac.simulator.bodies.Robot;
import net.phys2d.math.ROVector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.CollisionListener;

public final class CollisionHandler implements CollisionListener
{
    private final Logger logger;
    private final Player bluePlayer;
    private final Player yellowPlayer;

    public CollisionHandler(Logger logger, Player bluePlayer, Player yellowPlayer)
    {
        this.logger = logger;
        this.bluePlayer = bluePlayer;
        this.yellowPlayer = yellowPlayer;
    }

    private void checkBody(Body body, Body other, ROVector2f point) throws Exception
    {
        if (body instanceof Robot)
        {
            Robot robot = (Robot) body;
            boolean otherIsBall = other instanceof Ball;
            double angle = robot.getRelativeAngleTo(new Point((int) point.getX(), (int) point.getY())) - 90;
            Player player = robot.getPlayerColour() == PlayerColour.Blue ? bluePlayer : yellowPlayer;

            // TODO: Check sensor IDs
            if (angle > -30 && angle <= 0 && !otherIsBall)
            {
                // Front-left collision
                player.sendCollision(0);
            }
            else if (angle > 0 && angle <= 30)
            {
                // Front-right collision
                player.sendCollision(1);
            }
            else if (angle > 110 && angle <= 160)
            {
                // Back-right collision
                player.sendCollision(2);
            }
            else if (angle > 160 && angle <= 200)
            {
                // Back collision
                player.sendCollision(3);
            }
            else if (angle > 200 && angle <= 250)
            {
                // Back-left collision
                player.sendCollision(4);
            }
        }
    }

    public void collisionOccured(CollisionEvent collisionEvent)
    {
        try
        {
            ROVector2f point = collisionEvent.getPoint();
            Body bodyA = collisionEvent.getBodyA();
            Body bodyB = collisionEvent.getBodyB();
            checkBody(bodyA, bodyB, point);
            checkBody(bodyB, bodyA, point);
        }
        catch (Exception exception)
        {
            logger.log(true, Severity.Exception, LogMessage.ESimColHdr);
        }
    }
}
