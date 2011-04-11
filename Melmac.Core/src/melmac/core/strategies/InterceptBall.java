package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.utils.Distance;
import melmac.core.utils.Path;
import melmac.core.utils.PointOfIntersection;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

public class InterceptBall implements Strategy
{

    @Override
    public double getUtility(WorldState state)
    {
        if (state.areWeNextToBall() || !state.isBallMoving())
        {
            return 0;
        }
        /*
         * TODO: change values AFTER milestone 4
         * utility is 0.25 so that stop can overwrite it if ball is not moving towards our goal 
         */
        double utility = 0.35;
        if (state.isBallApprOurGoal())
        {
            utility += 0.50;
        }
        return utility;
    }

    @Override
    public Action getNextAction(WorldState state)
    {
        Point ball = state.getBall().getPosition();
        Point self = state.getSelf().getPosition();
        Point ownGoal = state.getOwnGoalTop();
        Point ballVelocity = state.getBall().getVelocity();
        double ballSpeed = ballVelocity.getLength();
        double maxRobotSpeed = 63 * 60; //convert from pix/s to pix/m
        double speedRatio = ballSpeed / maxRobotSpeed;

        //get position of ball in 6 seconds
        Point futureBall = new Point(ball.getX() + ballVelocity.getX() / 10, ball.getY() + ballVelocity.getY() / 10);

        //get the nearest point from the robot to the velocity vector of the ball
        double m = (double) futureBall.getY() / futureBall.getX();
        double b = ball.getY() - m * ball.getX();
        double x = (m * self.getY() + self.getX() - m * b) / (m * m + 1);
        double y = (m * m * self.getY() + m * self.getX() + b) / (m * m + 1);
        Point nearestPoint = new Point((int) x, (int) y);

        /*
         * check if the nearest point is between the ball and our goal
         * (if it is between the ball and the opponent goal, there is no
         * point in trying to move to it)
         */
        if (Path.isBetween(ball.getX(), ownGoal.getX(), nearestPoint.getX()))
        {
            /*
             * the point is between the ball and our goal, so check if we
             * can intercept it in time.
             */
            //compare the distances to nearesPoint
            double ballDist = Distance.euclidean(ball, nearestPoint);
            double selfDist = Distance.euclidean(self, nearestPoint);
            double distRatio = ballDist / selfDist;

            /*
             *try to go to nearest point on the velocity vector in time
             *if speedRatio is greater than distRatio, then it is possible to go there on time
             */
            int lowerConstr = RobotInfo.ROBOT_SIZE;
            int upperHeightConstr = state.getPitch().getDimension().getHeight() - RobotInfo.ROBOT_SIZE;
            int upperWidthConstr = state.getPitch().getDimension().getWidth() - RobotInfo.ROBOT_SIZE;
            if (speedRatio < distRatio)
            {
                //TODO: needs improvement, there are some extreme cases where this wont work
                if (lowerConstr > nearestPoint.getX())
                {
                  nearestPoint = new Point(lowerConstr, nearestPoint.getY());
                }
                else if (nearestPoint.getX() > upperWidthConstr)
                {
                    nearestPoint = new Point(upperWidthConstr, nearestPoint.getY());
                }
                else if (lowerConstr > nearestPoint.getY())
                {
                    nearestPoint = new Point(nearestPoint.getX(), lowerConstr);
                }
                else if (nearestPoint.getY() > upperHeightConstr)
                {
                    nearestPoint = new Point(nearestPoint.getX(), upperHeightConstr);
                }
                return Move.To(nearestPoint, state.getSelf(), state.getMaxSpeed());
            }
        }

        /*
         * else go as further back as you can
         * (somewhere infront of our own goal)
         */
        Point keeperPosition;
        if (state.isTargetRight())
        {
            keeperPosition = new Point(ownGoal.getX() + RobotInfo.ROBOT_SIZE, ownGoal.getY());
        }
        else
        {
            keeperPosition = new Point(ownGoal.getX() - RobotInfo.ROBOT_SIZE, ownGoal.getY());
        }
        // furthestPoint is the furthest point on the path of the ball, which the robot can reach
        Point furthestPoint = PointOfIntersection.intersectionOfTwoLines(ball, nearestPoint, keeperPosition, keeperPosition.translate(0, 10));

        return Move.To(furthestPoint, state.getSelf(), state.getMaxSpeed());
    }
}
