package melmac.simulator.comms.nxthandlers;

import melmac.core.comms.MessageHandler;
import melmac.simulator.Player;
import melmac.simulator.bodies.Pitch;
import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;

public final class MoveMessageHandler implements MessageHandler
{
    private final Player player;

    public MoveMessageHandler(Player player)
    {
        this.player = player;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        float rotation = player.getRobot().getRotation();
        int delta = argBuffer[0];
        int power = argBuffer[1];

        double direction = rotation + Math.toRadians(delta);
        float x = (float)Math.cos(direction - Math.PI / 2f);
        float y = (float)Math.sin(direction - Math.PI / 2f);
        float speed = player.getRobot().getMaxMovementSpeed() * Pitch.PIXELS_PER_CM * power / 100f;

        ROVector2f currentVelocity = player.getRobot().getVelocity();
        Vector2f velocityDelta = new Vector2f(x * speed - currentVelocity.getX(), y * speed - currentVelocity.getY());
        player.getRobot().adjustVelocity(velocityDelta);
        player.getRobot().adjustAngularVelocity(-player.getRobot().getAngularVelocity());
        return false;
    }
}
