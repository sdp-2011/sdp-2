package melmac.simulator.comms.nxthandlers;

import melmac.core.comms.MessageHandler;
import melmac.simulator.Player;
import melmac.simulator.bodies.Ball;
import melmac.simulator.bodies.Pitch;
import melmac.simulator.bodies.Robot;
import net.phys2d.math.Vector2f;

public final class KickMessageHandler implements MessageHandler
{
    private final Player player;
    private final Ball ball;

    public KickMessageHandler(Player player, Ball ball)
    {
        this.player = player;
        this.ball = ball;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        Thread.sleep(1000);

        if (ball.getPosition().distance(player.getRobot().getPosition()) < Robot.LENGTH)
        {
            float direction = player.getRobot().getRotation() - (float) Math.PI / 2;
            int x = (int) (10 * Math.cos(direction));
            int y = (int) (10 * Math.sin(direction));
            float speed = (float) (15 * Pitch.PIXELS_PER_CM);
            ball.adjustVelocity(new Vector2f(x * speed, y * speed));
        }

        Thread.sleep(200);
        return true;
    }
}
