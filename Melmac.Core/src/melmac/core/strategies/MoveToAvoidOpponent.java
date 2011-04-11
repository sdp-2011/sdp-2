/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.utils.Path;
import melmac.core.world.BallInfo;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

/**
 *
 * @author s0830457
 */
public class MoveToAvoidOpponent implements Strategy {

    @Override
    public double getUtility(WorldState state) {
        
        //check if opponent is in the way
        if (!state.isOppInWayToBall()) {
            return 0;
        }
        if (state.getDistanceOppToBall() < BallInfo.DIAMETER)
        {
            return 0;
        }

        double utility = 0.75;

        return utility;
    }

    @Override
    public Action getNextAction(WorldState state) {
        RobotInfo opp = state.getOpponent();
        RobotInfo self = state.getSelf();
        Point ball = state.getBall().getPosition();
        int neededDistance = 2*RobotInfo.ROBOT_SIZE;
        boolean goBelow = false; // fasle by default

        // TODO: check distance/position and plot first point

        if (self.getPosition().getY() < opp.getPosition().getY())
        {   // but not enough space
            if (opp.getPosition().getY() - neededDistance < 0)
            {
                goBelow = true; // go below
            }
        }
        else // if we are below, and there's enough space, than go below
           if (opp.getPosition().getY() + neededDistance < state.getPitch().getDimension().getHeight())
                goBelow = true;

        /*
         * Define a path of 2 points
         * the two points are on a path above/below the opponent so
         * that we don't hit it there is no need for a 3rd point
         * (behind the opponent), because the strategy will be
         * overwritten by MoveStraightToBall or MoveBehindBall.
         * Return 1st point untill it's reached,
         * then return 2nd point until it's overwritten.
         */
        int offsetX = RobotInfo.ROBOT_SIZE * 3 / 2;
        int offsetY = RobotInfo.ROBOT_SIZE * 3 / 2;
        if (!goBelow)
        {
            offsetY *= -1;
        }
        /*
         * Define a point behind the ball, so that the points do not invert,
         * once our robot is behind the ball. Check if the opponent's centroid
         * is between that point and the ball along the X axis. If it is, the
         * points will again be inverted so assign the point to be behind the
         * robot. This is for cases where the opponent is below or above the ball.
         * 
         */
        Point targetPoint;
        if (state.isTargetRight())
        {
            targetPoint = ball.translate(-offsetX, 0);
            if (Path.isBetween(ball.getX(), targetPoint.getX(), opp.getPosition().getX()))
            {
                targetPoint = opp.getPosition().translate(-offsetX, 0);
            }
        } else {
            targetPoint = ball.translate(offsetX, 0);
            if (Path.isBetween(ball.getX(), targetPoint.getX(), opp.getPosition().getX()))
            {
                targetPoint = opp.getPosition().translate(offsetX, 0);
            }
        }

        if (self.getPosition().getX() < targetPoint.getX())
        {
            offsetX *= -1;
        }
        /*
         * The offset for Y is the same for the 2 points,
         * the offset for X for the 2nd point is the opposite of the one for the 1st point
         */
        Point firstPoint = opp.getPosition().translate(offsetX, offsetY);
        Point secondPoint = opp.getPosition().translate(-offsetX, offsetY);
        Point thirdPoint = opp.getPosition().translate(-offsetX * 3 / 2, 0);
        if (Path.isBetween(self.getPosition().getX(), secondPoint.getX(), firstPoint.getX()))
        {
            /*
             * robot has not reached the first point yet,
             * so return first point
             */
            return Move.To(firstPoint, self, state.getMaxSpeed());
        }
        if (Path.isBetween(self.getPosition().getX(), thirdPoint.getX(), secondPoint.getX()))
        {
            /*
             * robot has not reached the second point yet,
             * so return second point
             */
            return Move.To(secondPoint, self, state.getMaxSpeed());
        }
        return Move.To(thirdPoint, self, state.getMaxSpeed());
    }
}
