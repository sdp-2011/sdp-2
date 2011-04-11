package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.utils.Path;
import melmac.core.world.WorldState;

/**
 *
 * @author s0827480
 */
public class ApproachBall implements Strategy
{

    @Override
    public double getUtility(WorldState state) {
        if (state.isBallInOppPossession())
        {
            return 0;
        }

        double utility = 0.47;
        /*
         *return 0 if facing too far away (this case should be covered by
         * another strategy - e.g. dribble and kick) or almost exaclty twards
         * the ball
         */

        if (state.isBallInPossession())
        {
            return 0;
        }

        if (!state.areWeNextToBall())
        {
            return 0;
        }
        if (!state.areWeBehindBall())
        {
            return 0;
        }

        utility += 0.4;

        return utility;
    }

    @Override
    public Action getNextAction(WorldState state) {
        return Move.To(state.getBall().getPosition(), state.getSelf(), 50);
    }
    

}
