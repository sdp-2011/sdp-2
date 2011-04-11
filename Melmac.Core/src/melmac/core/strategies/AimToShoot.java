package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Spin;
import melmac.core.utils.Path;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

/**
 * This is not finished.. Don't bother even to read.
 */
public class AimToShoot implements Strategy
{

    @Override
    public double getUtility(WorldState state)
    {
        /*if (!state.isBallInPossession())
        {
            return 0;
        }*/
        if (state.isBallInOppPossession())
        {
            return 0;
        }

        if (!state.isShotPossible())
        {
            return 0;
        }

        double utility = 0.47;
        /*
         *return 0 if we are too far away from the ball
         */
        if (!state.areWeNextToBall())
        {
            return 0;
        }
        
        /*
         * check if we are already facing the goal
         */
        double ang = state.getSelf().getRelativeAngleTo(Path.getClearShotPosition(state));

        if (ang > -10 && ang < 10)
        {
            /*
             * we are not facing the goal
             */
            return 0;
        }

        /*
         * check if the ball is not near a wall
         */
        if (state.isBallNearWall())
        {
            /*
             * ball is near a wall, check if we are already facing the ball
             * and if we are not, increase the utility, so we can turn to
             * face the ball.
             */
            if (!state.areWeFacingStraight())
            {
                utility += 0.4;
                return utility;
            }
            return 0;
        }

        if (Math.abs(state.getBall().getPosition().getX() - state.getTargetGoalCenter().getX()) < RobotInfo.ROBOT_SIZE
                 && (state.getBall().getPosition().getY() < state.getTargetGoalTop().getY()
             || state.getBall().getPosition().getY() > state.getTargetGoalBottom().getY()))
        {
            /*
             * ball is near a wall, check if we are already facing the ball
             * and if we are not, increase the utility, so we can turn to
             * face the ball.
             */
            if (!state.areWeFacingStraight())
            {
                utility += 0.4;
                return utility;
            }
            return 0;
        }

        utility += 0.4;
        /* double speed = Vector2D.getLen(state.getBall().getVelocity());
        int moving = 10*60; // convert 10pix/s to 10pix/m
        if (speed > moving) utility -= 0.2;
        else utility += 0.2;
         */
        /*if (state.getDistanceOppToGoalCenter() > state.getDistanceSelfToGoalCenter())
        {
            utility += 0.2;
        }
        else
        {
            utility -= 0.2;
        }*/

        return utility;
    }

    @Override
    public Action getNextAction(WorldState state)
    {
        RobotInfo self = state.getSelf();
        if (state.isBallNearWall())
        {
            /*
             * ball is near the wall, so turn to face towards the front,
             * not towards the ball, because if the ball is exactly next
             * to the wall, we will not be able to kick.
             */
            return Spin.ToFace(new Point(state.getTargetGoalCenter().getX(), self.getPosition().getY()), self);
        }
        Point ball = state.getBall().getPosition();
        if (Math.abs(ball.getX() - state.getTargetGoalCenter().getX()) < RobotInfo.ROBOT_SIZE
                 && (ball.getY() < state.getTargetGoalTop().getY()
             || ball.getY() > state.getTargetGoalBottom().getY()))
        {
            /*
             * ball is in a corner near the opponent goal, so turn to face
             * towards the front, not towards the ball, because if the ball
             * is exactly next to the wall, we will not be able to kick.
             */
            return Spin.ToFace(new Point(state.getTargetGoalCenter().getX(), self.getPosition().getY()), self);
        }
        return Spin.ToFace(Path.getClearShotPosition(state), self);
    }
}
