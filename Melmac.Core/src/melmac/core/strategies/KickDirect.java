package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Kick;
import melmac.core.utils.Path;
import melmac.core.world.WorldState;

public class KickDirect implements Strategy
{

    // TODO: Test utility with other strategies, might need tuning!
    @Override
    public double getUtility(WorldState state)
    {
        if (!state.isShotPossible())
        {
            return 0;
        }

        if (!(state.isBallInPossession()))
        {
            return 0;
        }

        if (!(state.areWeFacingGoal() && state.isClearShot(Path.getClearShotPosition(state))))
        {
            if (state.isBallNearWall() && state.areWeFacingStraight())
            {
                return 0.98;
            }
            return 0;
        }

        double utility = 0.98;
        
        /*double maxDist = Distance.euclidean(new Point(0,0), new Point(state.getVisionInfo().getPitchInfo().getDimension().getWidth(), state.getVisionInfo().getPitchInfo().getDimension().getHeight()));
        // the closer we are to the opponent goal - the higher the utility gets
        utility += (1 - state.getDistanceSelfToGoalCenter()/maxDist)*0.5;*/

        return utility;

        //return 0;
    }

    @Override
    public Action getNextAction(WorldState state)
    {
        return Kick.SINGLETON;
    }
}
