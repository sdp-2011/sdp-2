package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.utils.Path;
import melmac.core.world.BallInfo;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

public class MoveBehindBall implements Strategy
{
    // if opponent is closer to ball than we are with more than this advantage
    // this strategy will give up
    private int MAX_ADVANTAGE = 100;

    @Override
    public double getUtility(WorldState state)
    {
        // TODO: Add more checks here

        /*// Return 0 if we are already behind the ball
        if (state.areWeBehindBall())
            return 0;*/

        //this strategy will only deal with moving around the ball to face the goal
        if (state.areWeBehindBall())
        {
            return 0;
        }

        //no opponents in the way please!
        if (state.isOppInWayToBall()) {
            return 0;
        }

        return 0.90;

        /*
        double ourDistance = state.getDistanceSelfToBall();
        double theirDistance = state.getDistanceOppToBall();
        double utility = 0.30;

        if (theirDistance / ourDistance > 1) {
            utility += 0.30;
            if (theirDistance / ourDistance > 2)
                utility += 0.40;
        } else if (theirDistance - ourDistance < MAX_ADVANTAGE) {
            double portion = (theirDistance - ourDistance) / MAX_ADVANTAGE;
            utility += 0.7 * portion;
        }
        return utility;
         */
    }

    @Override
    public Action getNextAction(WorldState state)
    {
        Point ball = state.getBall().getPosition();
        RobotInfo self = state.getSelf();
        int offsetX = RobotInfo.ROBOT_SIZE;
        int offsetY = RobotInfo.ROBOT_SIZE;
        if (state.isTargetLeft())
        {
            offsetX *= -1;
        }
        boolean isBallNearGoal = false;

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
                target = state.getOwnGoalTop().translate(offsetX / 2, 0);
                return Move.To(target, self, state.getMaxSpeed());
            }
            else if (ball.getY() > state.getOwnGoalBottom().getY())
            {
                /*
                 * if it is at the bottom corner set target point infront of the
                 * bottom end of our goal
                 */
                target = state.getOwnGoalBottom().translate(offsetX / 2, 0);
                return Move.To(target, self, state.getMaxSpeed());
            }
            isBallNearGoal = true;
        }

        int neededDistance = RobotInfo.ROBOT_SIZE + BallInfo.RADIUS;
        boolean goBelow = false; // fasle by default

        if (state.areWeAboveBall())
        {   // but not enough space
            if (ball.getY() - neededDistance < 0)
            {
                goBelow = true; // go below
            }
        }
        else // if we are below, and there's enough space, than go below
           if (ball.getY() + neededDistance < state.getPitch().getDimension().getHeight())
                goBelow = true;

        /*
         * Define a path of 2 points
         * the two points are on a path above/below the ball so that we don't hit it
         * there is no need for a 3rd point (behind the ball), because
         * the strategy will be overwritten by MoveStraightToBall
         * return 1st point untill it's reached,
         * then return 2nd point until it's overwritten.
         */
        if (!goBelow)
        {
            offsetY *= -1;
        }
        /*
         * The offset for Y is the same for the 2 points,
         * the offset for X for the 2nd point is the opposite of the one for the 1st point
         */
        Point firstPoint = ball.translate(offsetX, offsetY);
        Point secondPoint = ball.translate(-offsetX, offsetY);
        if (Path.isInAWay(self.getPosition(), secondPoint, ball, RobotInfo.ROBOT_SIZE))
        {
            /*
             * second point cannot be reached directly without hitting the ball
             * so try to go to the first point
             */
            if (Path.isBetween(self.getPosition().getX(), secondPoint.getX(), firstPoint.getX()))
            {
                /*
                * robot has not reached the first point yet,
                * so return first point
                */
                return Move.To(firstPoint, self, state.getMaxSpeed());
            }
        }
        /*
         * if ball is near our goal, try to get it out of there.
         */
        if (isBallNearGoal)
        {
            return Move.To(ball.translate(-offsetX/2, offsetY/2), self, state.getMaxSpeed());
        }
        /*
         * otherwise move to second point
         */
        return Move.To(secondPoint, self, state.getMaxSpeed());
        
        /*
        // If right goal is oppnt's goal, plot final point to the left
        if (state.getUiInfo().isTargetRight())
            ballOffset *= -1;

        Point destination = ball.translate(ballOffset, 0); // calculate final point
        --------------
        if (goBottom)
            p.getActions().add(new MoveToAction(destination.translate(0, RobotInfo.ROBOT_SIZE), 100, false));
        else
            p.getActions().add(new MoveToAction(destination.translate(0, -RobotInfo.ROBOT_SIZE), 100, false));

        p.getActions().add(new MoveToAction(destination, 100, true));
        */
    }
}
