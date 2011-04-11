package melmac.core.utils;

import melmac.core.world.BallInfo;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

public final class Path
{

    /**
     * Check if an obstacle is in a way between source and destination. Width
     * defines the width around the vector of the path that you want to check.
     * If width is 0 it will check if the centroid of the obstacle is exactly
     * on the vector between the source and destination.
     */
    public static boolean isInAWay(Point source, Point destination, Point obstacle, int width)
    {
        /*
         * perform simple check for the X values of the Points. If the X value
         * of the obstacle is not between the X values for source and
         * destination then the obstacle is not between the source and the
         * destination
         */
        /*if (!isBetween(source.getX(), destination.getX(), obstacle.getX()))
        {
            return false;
        }*/

        /*
         * perform the same simple check for the Y values of the Points
         */
        /*if (!isBetween(source.getY(), destination.getY(), obstacle.getY()))
        {
            return false;
        }*/

        Point v = source.getDirectionTo(destination);
        double m = (double) v.getY() / v.getX();
        double b = source.getY() - m * source.getX();
        double x = (m * obstacle.getY() + obstacle.getX() - m * b) / (m * m + 1);
        double y = (m * m * obstacle.getY() + m * obstacle.getX() + b) / (m * m + 1);

        /*
         * calculate the Euclidean distance between the obstacle and the vector
         * between the source and the destination
         */
        double xDiff = obstacle.getX() - x;
        double yDiff = obstacle.getY() - y;
        double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

        /**
         * compare the distance with the maximum width. If the distance is
         * smaller, then the obstacle is in the way, otherwise it is not.
         */
        if (distance > width)
        {
            return false;
        }
        if (!isBetween(source.getX(), destination.getX(), x))
        {
            return false;
        }

        return true;
    }

    /**
     * Checks if the value of a double X is between the values of integers
     * A and B
     */
    public static boolean isBetween(int a, int b, double x)
    {
        if (a < b)
        {
            return a < x && x < b;
        }
        else
        {
            return b < x && x < a;
        }
    }

    /**
     * Checks whether the opponent is in the way between the start point
     * and the destination point.
     */
    public static boolean isOpponentInTheWay(Point start, Point destination, Point opponent)
    {
        return isInAWay(start, destination, opponent, RobotInfo.ROBOT_SIZE);
    }

    /**
     * Checks whether the ball is in the way between the start point
     * and the destination point.
     */
    public static boolean isBallInTheWay(WorldState state, Point target)
    {
        return isInAWay(state.getSelf().getPosition(), target, state.getBall().getPosition(), RobotInfo.ROBOT_SIZE / 2);
    }

    /**
     * Checks whether the ball is in our possession (in possession means
     * close to our robot and in front of our kicker)
     */
    public static boolean isBallInPossession(WorldState state)
    {
        // ball should be somewhat in front and
        if (Math.abs(state.getSelf().getRelativeAngleTo(state.getBall().getPosition())) < 45
            && state.getDistanceSelfToBall() <= RobotInfo.ROBOT_SIZE / 2 + BallInfo.DIAMETER * 2)
        {
            return true;
        }
        return false;
    }

    /**
     * Checks whether the ball is in opponent's possession (in possession
     * in this case means close to the opponent robot. We do not know where
     * the opponent's kicker is located)
     */
    public static boolean isBallInOppPossession(WorldState state)
    {
        //TODO: Consider making this work in the same way as isBallInPossession!
        //note: we should assumbe the robot has to be facing the ball, we don't know if it has the kicker in front or the T is facing forward. (OR DO WE???)
        //allow up to half a robot size to be the distance between to robot and us
        if (Distance.euclidean(state.getSelf().getPosition(), state.getBall().getPosition()) > RobotInfo.ROBOT_SIZE / 2 + BallInfo.DIAMETER * 2)
        {
            return false;
        }

        return true;
    }

    /**
     * Checks whether our robot is located infront
     * of the center of our own goal.
     */
    public static boolean areWeGoalkeeper(WorldState state)
    {
        Point self = state.getSelf().getPosition();
        Point ownGoal = state.getOwnGoalCenter();
        double distToOwnGoal = Distance.euclidean(self, ownGoal);

        if (isBallInOppPossession(state) && distToOwnGoal < 2 * RobotInfo.ROBOT_SIZE)
        {
            return true;
        }

        return false;
    }

    /**
     * Checks whether our robot is behind the ball. This means, whether the
     * X value of our robot is between our own goal and the ball. This does
     * NOT mean that we are EXACTLY behind the ball. We might be far behind it.
     */
    public static boolean areWeBehindBall(WorldState state)
    {
        Point self = state.getSelf().getPosition();
        Point ownGoal = state.getOwnGoalCenter();
        Point ball = state.getBall().getPosition();
        return isBetween(ownGoal.getX(), ball.getX(), self.getX());
    }

    /**
     * Returns one of two points on a path around the opponent.
     * If the opponent is in the way to the second point, the first point
     * is returned until it has been reached. Once it has been reached, or
     * if the opponent is not in the way to the second point,
     * then the second point is returned.
     */
    public static Point getPathAroundOpponent(WorldState state)
    {
        Point ball = state.getBall().getPosition();
        Point self = state.getSelf().getPosition();
        Point opp = state.getOpponent().getPosition();
        Point point;
        Point point2;
        Point target;
        int pitchWidth = state.getPitch().getDimension().getWidth();
        int pitchHeight = state.getPitch().getDimension().getHeight();
        if (Math.abs(ball.getX() - self.getX()) < Math.abs(ball.getY() - self.getY()))
        {
            // Find minimum X between opp and walls
            double leftDistance = Distance.squaredEuclidean(opp, new Point(0, opp.getY()));
            double rightDistance = Distance.squaredEuclidean(opp, new Point(pitchWidth, opp.getY()));

            if (leftDistance < rightDistance)
            {
                if (ball.getY() < opp.getY() && opp.getY() < self.getY())
                {
                    // objects are arranged from top to bottom: ball, opponent, self
                    point = new Point(opp.getX() + RobotInfo.ROBOT_SIZE, Math.min(opp.getY() + RobotInfo.ROBOT_SIZE, pitchHeight - RobotInfo.ROBOT_SIZE * 2 / 3));
                    point2 = new Point(opp.getX() + RobotInfo.ROBOT_SIZE, Math.max(opp.getY() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE * 2 / 3));
                }
                else
                {
                    // objects are arranged from top to bottom: self, opponent, ball
                    point = new Point(opp.getX() + RobotInfo.ROBOT_SIZE, Math.max(opp.getY() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE * 2 / 3));
                    point2 = new Point(opp.getX() + RobotInfo.ROBOT_SIZE, Math.min(opp.getY() + RobotInfo.ROBOT_SIZE, pitchHeight - RobotInfo.ROBOT_SIZE * 2 / 3));
                }
            }
            else
            {
                if (ball.getY() < opp.getY() && opp.getY() < self.getY())
                {
                    // objects are arranged from top to bottom: ball, opponent, self
                    point = new Point(opp.getX() - RobotInfo.ROBOT_SIZE, Math.min(opp.getY() + RobotInfo.ROBOT_SIZE, pitchHeight - RobotInfo.ROBOT_SIZE * 2 / 3));
                    point2 = new Point(opp.getX() - RobotInfo.ROBOT_SIZE, Math.max(opp.getY() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE * 2 / 3));
                }
                else
                {
                    // objects are arranged from top to bottom: self, opponent, ball
                    point = new Point(opp.getX() - RobotInfo.ROBOT_SIZE, Math.max(opp.getY() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE * 2 / 3));
                    point2 = new Point(opp.getX() - RobotInfo.ROBOT_SIZE, Math.min(opp.getY() + RobotInfo.ROBOT_SIZE, pitchHeight - RobotInfo.ROBOT_SIZE * 2 / 3));
                }
            }
            //if the robot is near the first point, or is between both points, then go to the second one
            if (Distance.euclidean(self, point) < 7 || isBetween(point.getY(), point2.getY(), self.getY()))
            {
                target = point2;
            }
            else
            {
                target = point;
            }
        }
        else
        {
            // Find minimum Y between opp and walls
            double topDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), 0));
            double bottomDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), pitchHeight));

            if (topDistance > bottomDistance)
            {
                if (ball.getX() < opp.getX() && opp.getX() < self.getX())
                {
                    // objects are arranged from left to right: ball, opponent, self
                    point = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE * 2 / 3), opp.getY() - RobotInfo.ROBOT_SIZE);
                    point2 = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE * 2 / 3), opp.getY() - RobotInfo.ROBOT_SIZE);
                }
                else
                {
                    // objects are arranged from left to right: self, opponent, all
                    point = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE * 2 / 3), opp.getY() - RobotInfo.ROBOT_SIZE);
                    point2 = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE * 2 / 3), opp.getY() - RobotInfo.ROBOT_SIZE);
                }
            }
            else
            {
                if (ball.getX() < opp.getX() && opp.getX() < self.getX())
                {
                    // objects are arranged from left to right: ball, opponent, self
                    point = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE * 2 / 3), opp.getY() + RobotInfo.ROBOT_SIZE);
                    point2 = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE * 2 / 3), opp.getY() + RobotInfo.ROBOT_SIZE);
                }
                else
                {
                    // objects are arranged from left to right: self, opponent, ball
                    point = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE * 2 / 3), opp.getY() + RobotInfo.ROBOT_SIZE);
                    point2 = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE * 2 / 3), opp.getY() + RobotInfo.ROBOT_SIZE);
                }
            }
            //if the robot is near the first point, or is between both points, then go to the second one
            if (Distance.euclidean(self, point) < 7 || isBetween(point.getX(), point2.getX(), self.getX()))
            {
                target = point2;
            }
            else
            {
                target = point;
            }
        }
        return target;
    }

    /**
     * This method should only be used after we have made sure that at
     * least one clear shot is possible! (either center, top or bottom
     * of goal)
     * @return Preferably the center point of the goal. If the opponent
     * is blocking it it checks which point we are closer to (bottom or top
     * of goal). If the nearer point is not blocked as well, we face it,
     * otherwise we face the farther point.
     */
    public static Point getClearShotPosition(WorldState state)
    {
        Point self = state.getSelf().getPosition();
        Point opp = state.getOpponent().getPosition();
        boolean isCentralShotClear = !isInAWay(self, state.getTargetGoalCenter(), opp, RobotInfo.ROBOT_SIZE * 2 / 3);
        if (isCentralShotClear)
        {
            return state.getTargetGoalCenter();
        }
        boolean isTopShotClear = !isInAWay(self, state.getTargetGoalTop(), opp, RobotInfo.ROBOT_SIZE * 2 / 3);
        boolean isBottomShotClear = !isInAWay(self, state.getTargetGoalBottom(), opp, RobotInfo.ROBOT_SIZE * 2 / 3);
        if (self.getX() < state.getPitch().getDimension().getHeight()/2)
        {
            /*
             * if we are in the top half of the pitch try to face
             * the top corner
             */
            if (isTopShotClear)
            {
                return state.getTargetGoalTop().translate(0, 15);
            }
            /*
             * top corner is not free so face bottom corner
             */
            return state.getTargetGoalBottom();
        } else
        {
            /*
             * we are on the bottom half of the pitch so try to face
             * the bottom corner
             */
            if (isBottomShotClear)
            {
                return state.getTargetGoalBottom().translate(0, -15);
            }
            /*
             * bottom corner is not free so face top corner
             */
            return state.getTargetGoalTop();

        }
    }
}
