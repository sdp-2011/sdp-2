package melmac.core.world;

import melmac.core.utils.Angle;
import melmac.core.utils.Path;
import melmac.core.utils.Distance;
import melmac.core.utils.Vector;

public final class WorldState
{

    private final VisionInfo visionInfo;
    private final UiInfo uiInfo;
    private PitchInfo pitch;
    private BallInfo ball;
    private RobotInfo self;
    private RobotInfo opponent;
    private Point rightGoalTop;
    private Point rightGoalBottom;
    private Point leftGoalTop;
    private Point leftGoalBottom;
    private Point ownGoalTop;
    private Point targetGoalTop;
    private Point ownGoalBottom;
    private Point targetGoalBottom;
    private Point leftGoalCenter;
    private Point rightGoalCenter;
    private Point targetGoalCenter;
    private Point ownGoalCenter;
    // variables describing the world
    // booleans are set to false by default
    private boolean areWeNextToBall;
    private boolean isAreWeNextToBallSet;
    private boolean areWeAboveBall;
    private boolean isAreWeAboveBallSet;
    private boolean areWeBehindBall;
    private boolean isAreWeBehindBallSet;
    private boolean areWeLeftOfBall;
    private boolean isAreWeLeftOfBallSet;
    private boolean areWeFacingGoal;
    private boolean isAreWeFacingGoalSet;
    private boolean isOppInWayToBall;
    private boolean isOppInWayToBallSet;
    private boolean isBallInPossession;
    private boolean isBallInPossessionSet;
    private boolean isBallInOppPossession;
    private boolean isBallInOppPossessionSet;
    private boolean isOppNextToBall;
    private boolean isOppNextToBallSet;
    private boolean isShotPossible;
    private boolean isShotPossibleSet;
    private boolean isClearShot;
    private boolean isClearShotSet;
    private boolean isBallMoving;
    private boolean isBallMovingSet;
    private boolean areWeMoving;
    private boolean areWeMovingSet;
    private boolean areWeMovingSideways;
    private boolean areWeMovingSidewaysSet;
    private boolean isBallApprOurGoal;
    private boolean isBallApprOurGoalSet;
    private boolean isBallNearWall;
    private boolean isBallNearWallSet;
    private boolean areWeFacingStraight;
    private boolean areWeFacingStraightSet;
    //private volatile boolean isBallBtwSelfAndTargetGoal;
    //private volatile boolean isBallBtwSelfAndTargetGoalSet;
    private double relativeAngleSelfToBall;
    private boolean isRelativeAngleSelfToBallSet;
    private double relativeAngleOppToBall;
    private boolean isRelativeAngleOppToBallSet;
    private Point ballToSelfVector;
    private boolean isBallToSelfVectorSet;
    private double distanceSelfToBall;
    private boolean isDistanceSelfToBallSet;
    private double distanceOppToBall;
    private boolean isDistanceOppToBallSet;
    private double distanceOppToGoalCenter;
    private boolean isDistanceOppToGoalCenterSet;
    private double distanceSelfToGoalCenter;
    private boolean isDistanceSelfToGoalCenterSet;
    private double distanceBallToGoalCenter;
    private boolean isDistanceBallToGoalCenterSet;

    public WorldState(VisionInfo visionInfo, UiInfo uiInfo)
    {
        this.visionInfo = visionInfo;
        this.uiInfo = uiInfo;

        // initialise some instance variables
        pitch = this.visionInfo.getPitchInfo();
        ball = this.visionInfo.getPositionInfo().getBall();

        if (uiInfo.areWeBlue())
        {
            self = visionInfo.getPositionInfo().getBlueRobot();
            opponent = visionInfo.getPositionInfo().getYellowRobot();
        }
        else
        {
            opponent = visionInfo.getPositionInfo().getBlueRobot();
            self = visionInfo.getPositionInfo().getYellowRobot();
        }

        leftGoalTop = new Point(0, getPitch().getLeftGoalTop());
        leftGoalBottom = new Point(0, getPitch().getLeftGoalBottom());
        rightGoalTop = new Point(pitch.getDimension().getWidth(), getPitch().getRightGoalTop());
        rightGoalBottom = new Point(pitch.getDimension().getWidth(), getPitch().getRightGoalBottom());

        leftGoalCenter = Vector.getMidPoint(leftGoalTop, leftGoalBottom);
        rightGoalCenter = Vector.getMidPoint(rightGoalTop, rightGoalBottom);

        if (this.uiInfo.isTargetRight())
        {       // target is right
            ownGoalCenter = leftGoalCenter;
            targetGoalCenter = rightGoalCenter;

            ownGoalTop = leftGoalTop;
            ownGoalBottom = leftGoalBottom;
            targetGoalTop = rightGoalTop;
            targetGoalBottom = rightGoalBottom;
        }
        else    // target is left
        {
            ownGoalCenter = rightGoalCenter;
            targetGoalCenter = leftGoalCenter;

            ownGoalTop = rightGoalTop;
            ownGoalBottom = rightGoalBottom;
            targetGoalTop = leftGoalTop;
            targetGoalBottom = leftGoalBottom;
        }
    }

    public VisionInfo getVisionInfo()
    {
        return visionInfo;
    }

    public UiInfo getUiInfo()
    {
        return uiInfo;
    }

    public PitchInfo getPitch()
    {
        return pitch;
    }

    public BallInfo getBall()
    {
        return ball;
    }

    public RobotInfo getSelf()
    {
        return self;
    }

    public RobotInfo getOpponent()
    {
        return opponent;
    }

    private Point getLeftGoalTop()
    {
        return leftGoalTop;
    }

    private Point getLeftGoalBottom()
    {
        return leftGoalBottom;
    }

    private Point getRightGoalTop()
    {
        return rightGoalTop;
    }

    private Point getRighttGoalBottom()
    {
        return rightGoalBottom;
    }

    public Point getOwnGoalTop()
    {
        return ownGoalTop;
    }

    public Point getOwnGoalBottom()
    {
        return ownGoalBottom;
    }

    public Point getTargetGoalTop()
    {
        return targetGoalTop;
    }

    public Point getTargetGoalBottom()
    {
        return targetGoalBottom;
    }

    private Point getLeftGoalCentre()
    {
        return leftGoalCenter;
    }

    private Point getRightGoalCentre()
    {
        return rightGoalCenter;
    }

    public Point getOwnGoalCenter()
    {
        return ownGoalCenter;
    }

    public Point getTargetGoalCenter()
    {
        return targetGoalCenter;
    }

    /**
     * Calculates and returns the Euclidean distance
     * between our robot and the ball.
     */
    public double getDistanceSelfToBall()
    {
        if (!isDistanceSelfToBallSet)
        {
            distanceSelfToBall = Distance.euclidean(ball.getPosition(), self.getPosition());
            isDistanceSelfToBallSet = true;
        }
        return distanceSelfToBall;
    }

    /**
     * Calculates and returns the distance of the opponent to the ball.
     * @return the euclidian distance of the opponent to the ball.
     */
    public double getDistanceOppToBall()
    {
        if (!isDistanceOppToBallSet)
        {
            distanceOppToBall = Distance.euclidean(ball.getPosition(), opponent.getPosition());
            isDistanceOppToBallSet = true;
        }
        return distanceOppToBall;
    }

    /**
     * @return 'true' if robot's Y coordinate is less than ball's Y coordinate.
     */
    public boolean areWeAboveBall()
    {
        if (!isAreWeAboveBallSet)
        {
            areWeAboveBall = self.getPosition().getY() < ball.getPosition().getY();
            isAreWeAboveBallSet = true;
        }
        return areWeAboveBall;
    }

    /**
     * @return 'true' if robot's Y coordinate is greater than ball's Y coordinate.
     */
    public boolean areWeBelowBall()
    {
        return !areWeAboveBall();
    }

    /**
     * @return 'true' if robot's X coordinate is less than ball's X coordinate.
     */
    public boolean areWeLeftOfBall()
    {
        if (!isAreWeLeftOfBallSet)
        {
            areWeLeftOfBall = self.getPosition().getX() < ball.getPosition().getX();
            isAreWeLeftOfBallSet = true;
        }
        return areWeLeftOfBall;
    }

    /**
     * @return 'true' if robot's X coordinate is greater than ball's X coordinate.
     */
    public boolean areWeRightOfBall()
    {
        return !areWeLeftOfBall();
    }

    /* TODO: Finish these
    public boolean canWeGoAboveBall(){}

    public boolean canWeGoBelowBall(){}
     */
    /**
     * Checks whether the opponent is in the way between
     * our robot and a point behind the ball.
     */
    public boolean isOppInWayToBall()
    {
        if (!isOppInWayToBallSet)
        {
            int offset = RobotInfo.ROBOT_SIZE + BallInfo.DIAMETER * 2;
            if (isTargetRight())
            {
                offset *= -1;
            }
            Point targetPoint = ball.getPosition().translate(offset, 0);
            isOppInWayToBall = Path.isInAWay(self.getPosition(), targetPoint,
                                             opponent.getPosition(), RobotInfo.ROBOT_SIZE);
            if (getDistanceOppToBall() < BallInfo.DIAMETER)
            {
                /* this check is only for when the plate is not on the pitch
                 * and the position of the opponent is said to be on the ball.
                 */
                isOppInWayToBall = false;
            }
            isOppInWayToBallSet = true;
        }
        return isOppInWayToBall;
    }

    /**
     * Calculates and returns the angle between our robot's orientation vector
     * and the vector from our robot to the ball.
     */
    public double getRelativeAngleSelfToBall()
    {
        if (!isRelativeAngleSelfToBallSet)
        {
            relativeAngleSelfToBall = self.getRelativeAngleTo(ball.getPosition());
            isRelativeAngleSelfToBallSet = true;
        }
        return relativeAngleSelfToBall;
    }

    /**
     * Calculates and returns the angle between the opponent's orientation
     * vector and the vector from the opponent to the ball.
     */
    public double getRelativeAngleOppToBall()
    {
        if (!isRelativeAngleOppToBallSet)
        {
            relativeAngleOppToBall = opponent.getRelativeAngleTo(ball.getPosition());
            isRelativeAngleOppToBallSet = true;
        }
        return relativeAngleOppToBall;
    }

    /**
     * Checks whether the ball is in our possesssion.
     * @return 'true' if the ball is close to our robot
     * and is in front of our kicker.
     */
    public boolean isBallInPossession()
    {
        if (!isBallInPossessionSet)
        {
            double selfToBallAngle = Angle.getAbsoluteAngle(Vector.subtract(ball.getPosition(), self.getPosition()));
            double selfAngle = Angle.getAbsoluteAngle(getSelf().getDirection());
            if (Math.abs(selfToBallAngle - selfAngle) < 35
                && getDistanceSelfToBall() <= RobotInfo.ROBOT_SIZE / 2 + BallInfo.DIAMETER * 3 / 2)
            {
                isBallInPossession = true;
            }
            isBallInPossessionSet = true;
        }
        return isBallInPossession;
    }

    /**
     * Checks whether the ball is in the opponent's possesssion.
     * @return 'true' if the ball is close to the opponent
     * and is in front of the opponent's kicker.
     */
    public boolean isBallInOppPossession()
    {
        if (!isBallInOppPossessionSet)
        {
            /*
             * the last check below is included, because when the blue plate
             * is removed from the pitch, the blue robot is found on the ball.
             */
            if (Math.abs(getRelativeAngleOppToBall()) < 45
                && getDistanceOppToBall() <= RobotInfo.ROBOT_SIZE / 2 + BallInfo.DIAMETER * 2
                && getDistanceOppToBall() > BallInfo.DIAMETER)
            {
                isBallInOppPossession = true;
            }
            isBallInOppPossessionSet = true;
        }
        return isBallInOppPossession;
    }

    /**
     * Checks whether the opponent is located close to the ball.
     * @return 'true' if the opponent is right next to the ball.
     */
    public boolean isOppNextToBall()
    {
        if (!isOppNextToBallSet)
        {
            /*
             * the last check below is included, because when the blue plate
             * is removed from the pitch, the blue robot is found on the ball.
             */
            isOppNextToBall = (getDistanceOppToBall() < RobotInfo.ROBOT_SIZE
                    && getDistanceOppToBall() > BallInfo.DIAMETER);
            isOppNextToBallSet = true;
        }
        return isOppNextToBall;
    }

    /**
     * Checks whether the goal where we need to score is located on the right
     * side of the pitch.
     */
    public boolean isTargetRight()
    {
        return uiInfo.isTargetRight();
    }

    /**
     * Checks whether the goal where we need to score is located on the left
     * side of the pitch.
     */
    public boolean isTargetLeft()
    {
        return !isTargetRight();
    }

    /**
     * Calculates and returns the distance between the opponent and our own goal.
     * (our own goal is the goal which we need to defend)
     * @return the euclidian distance between the opponent and the center of our goal.
     */
    public double getDistanceOppToGoalCenter()
    {
        if (!isDistanceOppToGoalCenterSet)
        {
            distanceOppToGoalCenter = Distance.euclidean(
                getOpponent().getPosition(),
                getOwnGoalCenter());
            isDistanceOppToGoalCenterSet = true;
        }
        return distanceOppToGoalCenter;
    }

    /**
     * Calculates and returns the distance betweent our robot and the
     * opponent's goal. (opponent's goal is the goal where we need to score)
     * @return the euclidian distance between us and opponent goal.
     */
    public double getDistanceSelfToGoalCenter()
    {
        if (!isDistanceSelfToGoalCenterSet)
        {
            distanceSelfToGoalCenter = Distance.euclidean(
                getSelf().getPosition(),
                getTargetGoalCenter());
            isDistanceSelfToGoalCenterSet = true;
        }
        return distanceSelfToGoalCenter;
    }

    /**
     * Calculates and returns the distance between the ball and our own goal.
     * (our own goal is the goal which we need to defend)
     * @return the euclidian distance between the ball and the center of our goal.
     */
    public double getDistanceBallToGoalCenter()
    {
        if (!isDistanceBallToGoalCenterSet)
        {
            distanceBallToGoalCenter = Distance.euclidean(
                getBall().getPosition(),
                getTargetGoalCenter());
            isDistanceBallToGoalCenterSet = true;
        }
        return distanceBallToGoalCenter;
    }

    /**
     * Calculates and returns the vector between our robot's centroid
     * and the ball's centroid.
     */
    public Point getBallToSelfVector()
    {
        if (!isBallToSelfVectorSet)
        {
            ballToSelfVector = Vector.subtract(getSelf().getPosition(),
                                                 getBall().getPosition());
            isBallToSelfVectorSet = true;
        }
        return ballToSelfVector;
    }

    /**
     * Checks whether our robot is located close to the ball.
     * @return true if we are next to the ball(orientation does not matter)
     */
    public boolean areWeNextToBall()
    {
        if (!isAreWeNextToBallSet)
        {
            double selfAngle = Angle.getAbsoluteAngle(getSelf().getDirection());
            //double selfToBallAngle = Angle.getAbsoluteAngle(Vector.subtract(ball.getPosition(), self.getPosition()));
            int distance = RobotInfo.ROBOT_SIZE / 2 + BallInfo.DIAMETER * 2;
            int angle; // this is the angle to the X axis, but only such that we are facing the goal.
            if (isTargetRight())
            {
                angle = 90;
            } else
            {
                angle = 270;
            }
            if (Math.abs(selfAngle - angle) > 60)
            {
                distance = RobotInfo.ROBOT_SIZE * 2;
            }
            areWeNextToBall = Distance.euclidean(
                getSelf().getPosition(), getBall().getPosition())
                              < distance;
            isAreWeNextToBallSet = true;
        }
        return areWeNextToBall;
    }

    /**
     * @return true if the robot is behind the ball (compared to target goal)
     * or false otherwise.
     */
    public boolean areWeBehindBall()
    {
        if (!isAreWeBehindBallSet)
        {
            areWeBehindBall = Path.isBetween(ball.getPosition().getX(), ownGoalCenter.getX(), self.getPosition().getX());
            /*
            double angleOfBallToRobotVecotr = Angle.getAbsoluteAngle(getBallToSelfVector());
            if (isTargetRight())
            // check if we are to the left of the ball (behind)
            {
                if (angleOfBallToRobotVecotr < 280 || angleOfBallToRobotVecotr > 260)
                {
                    areWeBehindBall = true;
                }
            } else // check if we are to the right of the ball (behind)
                if (angleOfBallToRobotVecotr > 90 || angleOfBallToRobotVecotr < 110)
                {
                    areWeBehindBall = true;
                }
             */
            isAreWeBehindBallSet = true;
        }
        return areWeBehindBall;
    }

    /**
     * @return true if the opponent is not blocking the whole target goal
     */
    public boolean isShotPossible()
    {
        if (!isShotPossibleSet)
        {
            double selfToTargetTopAngle = Angle.getAbsoluteAngle(Vector.subtract(targetGoalTop, self.getPosition()));
            double selfToTargetBottomAngle = Angle.getAbsoluteAngle(Vector.subtract(targetGoalBottom, self.getPosition()));
            int offset = (int) Math.sqrt(((RobotInfo.ROBOT_SIZE/2) * (RobotInfo.ROBOT_SIZE/2))/2);
            Point oppNearTop;
            Point oppNearBottom;
            if (isTargetRight())
            {
                oppNearTop = opponent.getPosition().translate(-offset, -offset);
                oppNearBottom = opponent.getPosition().translate(-offset, offset);
            } else
            {
                oppNearTop = opponent.getPosition().translate(offset, -offset);
                oppNearBottom = opponent.getPosition().translate(offset, offset);
            }
            double selfToOppTopAngle = Angle.getAbsoluteAngle(Vector.subtract(oppNearTop, self.getPosition()));
            double selfToOppBottomAngle = Angle.getAbsoluteAngle(Vector.subtract(oppNearBottom, self.getPosition()));
            if (isTargetRight())
            {
                isShotPossible = !(selfToTargetTopAngle > selfToOppTopAngle && selfToOppBottomAngle > selfToTargetBottomAngle);
            } else
            {
                isShotPossible = !(selfToTargetTopAngle < selfToOppTopAngle && selfToOppBottomAngle < selfToTargetBottomAngle);
            }
            isShotPossibleSet = true;
        }
        return isShotPossible;
    }

    /**
     * @return true if the opponent is not in the way to the point on
     * the target goal, which we are facing (either center of goal or
     * top or bottom corner).
     */
    public boolean isClearShot(Point target)
    {
        if (!isClearShotSet)
        {
            isClearShot = !Path.isInAWay(
                getSelf().getPosition(), target,
                getOpponent().getPosition(), RobotInfo.ROBOT_SIZE);
            isClearShotSet = true;
        }
        return isClearShot;
    }

    /**
     * This method will compare the absolute angles of 3 vectors - the direction
     * vector of our robot and the two vectors from our robot to the two points
     * that define the target goal. If the first absolute agle lies between the
     * other two, the function will return true false otherwise.
     */
    //TODO: Method needs testing!
    public boolean areWeFacingGoal()
    {
        if (!isAreWeFacingGoalSet)
        {
            // calculate absolute angles
            double selfAngle = Angle.getAbsoluteAngle(getSelf().getDirection());
            double selfToTargetTopAngle = Angle.getAbsoluteAngle(Vector.subtract(targetGoalTop, self.getPosition()));
            double selfToTargetBottomAngle = Angle.getAbsoluteAngle(Vector.subtract(targetGoalBottom, self.getPosition()));
            // if robot absolute angle is between gaol points absolute angles, than return true
            if ((isTargetRight() && selfToTargetTopAngle < selfAngle && selfAngle < selfToTargetBottomAngle)
                || (selfToTargetTopAngle > selfAngle && selfAngle > selfToTargetBottomAngle))
            {
                areWeFacingGoal = true;
            }
            isAreWeFacingGoalSet = true;
        }
        return areWeFacingGoal;
    }

    /**
     * Checks whether the ball is moving or if it is static.
     * @return 'true' if the ball is moving, 'false' if the ball is static.
     */
    public boolean isBallMoving()
    {
        if (!isBallMovingSet)
        {
            isBallMoving = (Distance.euclidean(new Point(0, 0), getBall().getVelocity()) / 60 > BallInfo.DIAMETER);
            isBallMovingSet = true;
        }
        return isBallMoving;
    }

    /**
     * Checks whether our robot is moving or if it is static.
     * @return 'true' if our robot is moving, 'false' if it is static.
     */
    public boolean areWeMoving()
    {
        if (!areWeMovingSet)
        {
            areWeMoving = (Distance.euclidean(new Point(0, 0), getSelf().getVelocity()) / 60 > BallInfo.DIAMETER);
            areWeMovingSet = true;
        }
        return areWeMoving;
    }

    /**
     * Checks whether our robot is moving more
     * along the Y axis than the X axis.
     */
    public boolean areWeMovingSideways()
    {
        if (!areWeMovingSidewaysSet)
        {
            areWeMovingSideways = (Math.abs(getSelf().getVelocity().getY()) >
                    Math.abs(getSelf().getVelocity().getY()));
            areWeMovingSidewaysSet = true;
        }
        return areWeMovingSideways;
    }

    /**
     * This method checks whether the ball is approaching our goal.
     */
    public boolean isBallApprOurGoal()
    {
        //if (!isBallApprOurGoalSet)
        //{
        if (!isBallMoving)
        {
            isBallApprOurGoal = false;
            isBallApprOurGoalSet = true;
            return isBallApprOurGoal;
        }
        //if our goal is left then x value of velocity should be negative
        //if out goal is right then x value of velocity should be positive
        isBallApprOurGoal = ((isTargetRight() && getBall().getVelocity().getX() < 0) || (!isTargetRight() && getBall().getVelocity().getX() > 0));
        isBallApprOurGoalSet = true;
        //}
        return isBallApprOurGoal;
    }

    /**
     * This method checks whether the ball is near one of the walls.
     */
    public boolean isBallNearWall()
    {
        if (!isBallNearWallSet)
        {
            double distBallToTop = ball.getPosition().getY();
            double distBallToBottom = pitch.getDimension().getHeight() - ball.getPosition().getY();
            isBallNearWall = (distBallToTop < RobotInfo.ROBOT_SIZE / 2 ||
                    distBallToBottom < RobotInfo.ROBOT_SIZE / 2);
            isBallNearWallSet = true;
        }
        return isBallNearWall;
    }

    public boolean areWeFacingStraight()
    {
        if (!areWeFacingStraightSet)
        {
            double angle = self.getRelativeAngleTo(new Point(targetGoalCenter.getX(), self.getPosition().getY()));
            areWeFacingStraight = (angle > -10 && angle < 10);
            areWeFacingStraightSet = true;
        }
        return areWeFacingStraight;
    }

    /**
     * This method returns the maximum speed at which we want the robot to move.
     * @return an integer between 0 and 160.
     */
    public int getMaxSpeed()
    {
        return 140;
    }
}
