package melmac.simulator.comms.nxthandlers;

import melmac.core.comms.MessageHandler;
import melmac.simulator.Player;
import net.phys2d.math.Vector2f;

public final class SpinMessageHandler implements MessageHandler
{

    private final float THRESHOLD = (float) Math.PI / 64f;
    private final Player player;

    public SpinMessageHandler(Player player)
    {
        this.player = player;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
		// Negate linear velocity to stop movement.
        Vector2f invertedVelocity = new Vector2f(- player.getRobot().getVelocity().getX(),
            - player.getRobot().getVelocity().getY());
        player.getRobot().adjustVelocity(invertedVelocity); // zero linear velocity when spinning

		// calculate desored velocity
		int deltaDeg = argBuffer[0];	// in degrees
        int power = argBuffer[1];		// from 0% to 160%
        float maxDegreesPerSecond = player.getRobot().getMaxRotationSpeed() * power / 100f;
		float sign = Math.signum(deltaDeg);	// determine direction
        float desiredAngularVelocity = (float) Math.toRadians(sign * maxDegreesPerSecond);
        //calculate desired about of rotation
		float desiredRotation = player.getRobot().getRotation() + (float) Math.toRadians(deltaDeg);
		// converted to millis (* 1000), increased by a fifth to allow delays from obsticles (* 1.2f)
		float expectedRotationTime = (Math.abs(deltaDeg) / maxDegreesPerSecond) * 1000 * 1.5f;

		// spin untill desired orientation reached or timeout has passed
		player.getRobot().adjustAngularVelocity(desiredAngularVelocity - player.getRobot().getAngularVelocity());
		float passed = 0;
		while (true)
        {
            if (Math.abs(player.getRobot().getRotation() - desiredRotation) < THRESHOLD ||
					passed > expectedRotationTime)
            {
                break;	// stop spinning
            }
			Thread.currentThread().sleep(10);
			passed += 10;
        }

		// Stop angular velosity to stop spin.
		player.getRobot().adjustAngularVelocity(-player.getRobot().getAngularVelocity());
        //player.getRobot().setRotation(desiredRotation);
        return true;
    }
}
