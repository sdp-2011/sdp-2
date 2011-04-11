package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Spin;
import melmac.core.world.WorldState;

/**
 * This strategy should be chosen when the robot is not facing the ball.
 * NOTE: Stupidly implemented just for testing. Only turns towards the ball.
 *
 * This is not finished.. Don't even bother to read.
 */
public class FaceBall implements Strategy
{

    @Override
    public double getUtility(WorldState state)
    {
        if (!state.areWeNextToBall())
        {
            return 0;
        }

        double ang = state.getSelf().getRelativeAngleTo(
            state.getBall().getPosition());
        // return 0 if already facing the ball
        if (ang > - 50 && ang < 50)
        {
            return 0;
        }

        double utility = 0.29;
        /*double speed = Vector2D.getLen(state.getBall().getVelocity());
        int moving = 10*60; // convert 10pix/s to 10pix/m
        if (speed > moving) utility -= 0.2;
        else utility += 0.2;*/

        if (!state.isBallInOppPossession())
        {
            utility += 0.4;
        }

        if (state.getDistanceOppToGoalCenter() > state.getDistanceSelfToGoalCenter())
        {
            utility += 0.2;
        }
        else
        {
            utility -= 0.2;
        }

        return utility;
    }

    @Override
    public Action getNextAction(WorldState state)
    {
        return Spin.ToFace(state.getBall().getPosition(), state.getSelf());
    }
}
