/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;


import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.actions.Spin;
import melmac.core.utils.Distance;
import melmac.core.utils.Path;
import melmac.core.world.Dimension;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;


/**
 *
 * @author s08383COLLISION_DISTANCE
 */
public class CollisionStrategy implements Strategy
{

    private int likeliestSource = 15;
    private double distanceToCollisionObject = Double.MAX_VALUE;
    private double utility = 0.0;
    private int lastSensorHit = 15; //not used at the moment
    private Point collisionObjectPoint;

    private final int MAX_COLL_SIZE = melmac.core.world.RobotInfo.ROBOT_SIZE;


    @Override
    public Action getNextAction(WorldState state) {

        Point ball = state.getBall().getPosition();
        RobotInfo self = state.getSelf();
        Point selfP = self.getPosition();
        
        //where is the object which is colliding, relative to me
        Point aimedVector = new Point(
                collisionObjectPoint.getX() - selfP.getX(),
                collisionObjectPoint.getY() - selfP.getY());

        //move in the opposite direction of the aimed direction vector
        Point movingToPoint = new Point(selfP.getX() - aimedVector.getX(),
                selfP.getY() - aimedVector.getY());

        Point centrePoint = new Point(
                (state.getPitch().getDimension().getWidth() / 2),
                (state.getPitch().getDimension().getHeight() / 2));

        if (likeliestSource!=0 && !Path.isOpponentInTheWay(selfP,
                movingToPoint, state.getOpponent().getPosition())
                && pointOutOfBounds(movingToPoint, state))
        {
            return Move.To(movingToPoint, self);
        }

        else if (likeliestSource!=0 && !Path.isOpponentInTheWay(selfP,
                movingToPoint, state.getOpponent().getPosition())
                && !pointOutOfBounds(movingToPoint, state))
        {
            return Move.To(centrePoint, self);
        }
        
        else
        {
            return Spin.ToFace(ball, self);
        }

    }

    @Override
    public double getUtility(WorldState state)
    {
        setAllDistances(state);
        
        if (likeliestSource != 0 && utility > 0.05)
        {
            double temp = utility;
            utility -= 0.05;
            return temp;
        }
        else
        {
            return 0;
        }   
    }

    public void setCollision(int sensorHit)
    {
        Integer inta = new Integer(sensorHit);
        if (!inta.equals(null))
        {
            this.utility = 0.99;
            this.lastSensorHit = sensorHit;
        }

    }

    private void setAllDistances(WorldState state)
    {

        distanceToCollisionObject = Double.MAX_VALUE;
        //check each source to determine what hit us.
        Point selfT = state.getSelf().getPosition();
        Point ballPos = state.getBall().getPosition();
        double ballDist = Distance.euclidean(selfT, ballPos);

        //checking ball
        if (ballDist < distanceToCollisionObject) {
            distanceToCollisionObject = ballDist;
            likeliestSource = 0;
            collisionObjectPoint = ballPos;

        }
        //checking other robot

        Point opp = state.getOpponent().getPosition();
        double otherRobotDist = Distance.euclidean(selfT, opp);

        if (otherRobotDist < distanceToCollisionObject) {
            distanceToCollisionObject = otherRobotDist;
            likeliestSource = 1;
            collisionObjectPoint = opp;
        }

        //checking wall

        Dimension fieldDimension = state.getVisionInfo().getPitchInfo().getDimension();


        Point topPoint = new Point(selfT.getX(), 0);
        double topDist = Distance.euclidean(selfT, topPoint);

        if (topDist < distanceToCollisionObject)
        {
            distanceToCollisionObject = topDist;
            likeliestSource = 2;
            collisionObjectPoint = topPoint;
        }

        Point leftPoint = new Point(0, selfT.getY());
        double leftDist = Distance.euclidean(selfT, leftPoint);

        if (leftDist < distanceToCollisionObject)
        {
            distanceToCollisionObject = leftDist;
            likeliestSource = 3;
            collisionObjectPoint = leftPoint;
        }

        Point bottomPoint = new Point(selfT.getX(), fieldDimension.getHeight());
        double bottomDist = Distance.euclidean(selfT, bottomPoint);

        if (bottomDist < distanceToCollisionObject)
        {
            distanceToCollisionObject = topDist;
            likeliestSource = 4;
            collisionObjectPoint = bottomPoint;
        }

        Point rightPoint = new Point(fieldDimension.getWidth(), selfT.getY());
        double rightDist = Distance.euclidean(selfT, rightPoint);

        if (rightDist < distanceToCollisionObject)
        {
            distanceToCollisionObject = topDist;
            likeliestSource = 5;
            collisionObjectPoint = rightPoint;
        }
    } 

    /**
    private double utilityDistanceMap()
    {
        if (distanceToCollisionObject > COLLISION_DISTANCE
                && distanceToCollisionObject <= 3 * COLLISION_DISTANCE)
        {
            double temp = distanceToCollisionObject - COLLISION_DISTANCE + 1;
            utility = (1 / temp);
            return utility;

        } else if (distanceToCollisionObject <= COLLISION_DISTANCE)
        {
            utility = 0.99;
            return 0.99;
        } else
        {
            utility = 0.0;
            return 0.0;
        }
    }
 */

    private boolean pointOutOfBounds(Point p, WorldState state)
    {
        int maxX = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
        int maxY = state.getVisionInfo().getPitchInfo().getDimension().getHeight();

        if (p.getX() < MAX_COLL_SIZE || p.getY() < MAX_COLL_SIZE || p.getX() > maxX || p.getY() > maxY)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

}