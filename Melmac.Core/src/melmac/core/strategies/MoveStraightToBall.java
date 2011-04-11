package melmac.core.strategies;

import java.util.logging.Level;
import java.util.logging.Logger;
import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.utils.Distance;
import melmac.core.world.BallInfo;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

public final class MoveStraightToBall implements Strategy
{
    @Override
    public double getUtility(WorldState state)
    {        
        //if we are already close to the ball we don't want to move closer
        //if (state.areWeNextToBall() || state.isOppInWayToBall())
          //  return 0;

        if (state.isBallInPossession() || state.isOppInWayToBall())
            return 0;

        //this strategy will not deal with moving around the ball to face target goal
        if (state.isTargetRight() && state.areWeRightOfBall() ||
                state.isTargetLeft() && state.areWeLeftOfBall())
            return 0;

        double ourDistance = state.getDistanceSelfToBall();
        double theirDistance = state.getDistanceOppToBall();

        double utility = 0.45;
        if (ourDistance < theirDistance)
            utility += 0.20;

        if (ourDistance * 2 < theirDistance)
            utility += 0.20;
        return utility;
    }

    @Override
    public Action getNextAction(WorldState state)
    {
        // TODO: remove log message once testing finishes
        Logger.getAnonymousLogger().log(Level.INFO, state.getSelf().getPosition() + " " + state.getBall().getPosition());
        Point ball = state.getBall().getPosition();
        RobotInfo self = state.getSelf();
        /*
         * first check if ball is near our goal or in a corner near our goal
         * this check is needed because of the barrel distortion effect
         */
        int offsetX = RobotInfo.ROBOT_SIZE;
        if (state.isTargetLeft())
        {
            offsetX *= -1;
        }

        /*
         * check if the ball is not near our own goal
         */
        Point target = state.getOwnGoalCenter();
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
                target = state.getOwnGoalTop().translate(offsetX / 3, 0);
                return Move.To(target, self, state.getMaxSpeed());
            }
            else if (ball.getY() > state.getOwnGoalBottom().getY())
            {
                /*
                 * if it is at the bottom corner set target point infront of the
                 * bottom end of our goal
                 */
                target = state.getOwnGoalBottom().translate(offsetX / 3, 0);
                return Move.To(target, self, state.getMaxSpeed());
            }
            /*
             * ball is in front of our goal so try to get it our of there.
             */
            return Move.To(ball.translate(-offsetX / 4, 0), self, state.getMaxSpeed());
        }



        /*
         * check if the ball is in a corner near the opponent goal
         */
        Point target2 = state.getTargetGoalCenter();
        if (Math.abs(ball.getX() - target2.getX()) < RobotInfo.ROBOT_SIZE)
        {
            /*
             * if the ball is near the opponent goal, check if it is in a corner
             */
            if (ball.getY() < state.getTargetGoalTop().getY())
            {
                /*
                 * if it is at the top corner set target point next to the ball
                 */
                target2 = ball.translate(-offsetX / 4, 5);
                return Move.To(target2, self, state.getMaxSpeed());
            }
            else if (ball.getY() > state.getTargetGoalBottom().getY())
            {
                /*
                 * if it is at the bottom corner set target point next to the ball
                 */
                target2 = ball.translate(-offsetX / 4, -5);
                return Move.To(target2, self, state.getMaxSpeed());
            }
        }




        /*
         * Ball is not near our goal or in a corner near any goal.
         */
        int ballOffset = RobotInfo.ROBOT_SIZE / 2 + BallInfo.DIAMETER;
        if (state.getUiInfo().isTargetRight())
        {
            ballOffset *= -1;
        }
        int wallOffset = RobotInfo.ROBOT_SIZE / 2;
        Point destination;
        if (ball.getY() > state.getPitch().getDimension().getHeight() - RobotInfo.ROBOT_SIZE /2)
        {
            /*
             * if ball is too near to bottom wall try to move along the wall
             */
            wallOffset *= -1;
            destination = ball.translate(ballOffset, wallOffset);
        } else if (ball.getY() < RobotInfo.ROBOT_SIZE / 2)
        {
            /*
             * if ball is too near to top wall try to move along the wall
             */
            destination = ball.translate(ballOffset, wallOffset);
        } else {
            /*
             * ball is not near neither top nor bottom wall
             */
            destination = ball.translate(ballOffset, 0);
        }
        /*
         * check if we are near the ball
         */
        if (state.getDistanceSelfToBall() < RobotInfo.ROBOT_SIZE + BallInfo.DIAMETER * 2){
            if (state.getDistanceSelfToBall() < RobotInfo.ROBOT_SIZE){
                /*
                 * we are really near, approach ball really slow
                 */
                return Move.To(ball.translate(ballOffset / 2, 0), state.getSelf(), 50);
            }
            /*
             * we are near, approach ball slower than normaly
             */
            return Move.To(ball.translate(ballOffset / 2, 0), state.getSelf(), 90);
        }
        return Move.To(destination, state.getSelf(), state.getMaxSpeed());
    }
}
