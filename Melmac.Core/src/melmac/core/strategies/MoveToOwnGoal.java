package melmac.core.strategies;

import melmac.core.world.Point;
import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

public final class MoveToOwnGoal implements Strategy
{

    @Override
    public double getUtility(WorldState state)
    {
        /*int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
        if(!Path.isBallInOppPossession(state))
        return 0;
        double utility = 0.50;
        if(state.getDistanceOppToGoalCenter() <= 1/2 *pitchWidth )
        utility += 0.20;

        return utility;*/

        /*
         * logic below is only for milestone 4, it needs to be changed afterwards
         */
        /*if (!state.isBallApprOurGoal() || !state.areWeBehindBall()){
        return 0;
        }*/
        if (!state.areWeBehindBall() && state.isBallApprOurGoal())
        {
            return 0.9;
        }
        return 0;

    }

    @Override
    public Action getNextAction(WorldState state)
    {
        Point target = state.getOwnGoalCenter();
        Point ball = state.getBall().getPosition();
        if (Math.abs(ball.getX() - target.getX()) < RobotInfo.ROBOT_SIZE)
        {
            /*
             * if the ball is near our own goal, check if it is in a corner
             */
            if (ball.getY() < state.getOwnGoalTop().getY())
            {
                /*
                 * if it is at the top corner set target point infront of the
                 * top end of our goal
                 */
                target = state.getOwnGoalTop();
            }
            else if (ball.getY() > state.getOwnGoalBottom().getY())
            {
                /*
                 * if it is at the bottom corner set target point infront of the
                 * bottom end of our goal
                 */
                target = state.getOwnGoalBottom();
            }
        }
        int offsetX = RobotInfo.ROBOT_SIZE;

        if (state.isTargetLeft())
        {
            offsetX *= -1;
        }

        target = target.translate(offsetX, 0);
        return Move.To(target, state.getSelf(), state.getMaxSpeed());

    }
}
