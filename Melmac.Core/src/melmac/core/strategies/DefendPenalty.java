/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.utils.Distance;
import melmac.core.utils.PointOfIntersection;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

/**
 *
 * @author s0830457
 */
public class DefendPenalty implements Strategy
{
    
    @Override
    public double getUtility(WorldState state)
    {
        if (state.isBallMoving())
        {
            if (state.getDistanceSelfToBall() < RobotInfo.ROBOT_SIZE /2 ||
                    state.getDistanceOppToBall() < RobotInfo.ROBOT_SIZE /2)
            {
                /*
                 * This handles the case, where the ball is not visible
                 * from above, and it is mistakenly located on some
                 * part of either our robot or the opponent.
                 */
                return 1;
            }
            return 0;
        }
        return 1;
    }

    @Override
    public Action getNextAction(WorldState state)
    {
        RobotInfo self = state.getSelf();
        //Point ball = state.getBall().getPosition();
        RobotInfo opponent = state.getOpponent();
        Point centerOfGoal = state.getOwnGoalCenter();
        int offset = RobotInfo.ROBOT_SIZE/6; //Pitch 1 = /2; Pitch 2 = /6;
        if (!state.isTargetRight())
        {
            offset *= -1;
        }
        Point goalkeeper = centerOfGoal.translate(offset, 0);
        Point likely = PointOfIntersection.intersectionOfTwoLines(
                goalkeeper, goalkeeper.translate(0, 10), opponent.getPosition(),
                opponent.getPosition().translate(opponent.getDirection().getX(),
                 opponent.getDirection().getY()));

        if (Distance.euclidean(self.getPosition(), likely) < RobotInfo.ROBOT_SIZE / 3)
        {
            return melmac.core.actions.Stop.SINGLETON;
        }
        return Move.To(likely, self, state.getMaxSpeed());
    }
}
