package melmac.simulator.comms.nxthandlers;

import melmac.core.comms.MessageHandler;
import melmac.simulator.Player;
import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;

public final class StopMessageHandler implements MessageHandler
{
    private final Player player;

    public StopMessageHandler(Player player)
    {
        this.player = player;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        ROVector2f currentVelocity = player.getRobot().getVelocity();
        Vector2f velocityDelta = new Vector2f(-currentVelocity.getX(), -currentVelocity.getY());
        player.getRobot().adjustVelocity(velocityDelta);
        player.getRobot().adjustAngularVelocity(-player.getRobot().getAngularVelocity());
        return true;
    }
}
