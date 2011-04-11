/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Kick;
import melmac.core.actions.Spin;
import melmac.core.utils.Path;
import melmac.core.utils.PointOfIntersection;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

/**
 *
 * @author s0830457
 */
public class TakePenalty implements Strategy {


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
         Point self = state.getSelf().getPosition();
         Point directionOfSelf= state.getSelf().getDirection();
         Point targetGoal = state.getTargetGoalCenter();
         Point direction = self.translate(directionOfSelf.getX(),directionOfSelf.getY());
         Point opponent = state.getOpponent().getPosition();
         Point topCorner = new Point(targetGoal.getX(), targetGoal.getY()/2 + 30);
         Point bottomCorner = new Point(targetGoal.getX(), targetGoal.getY()*3/2 - 30);
         Point target = PointOfIntersection.intersectionOfTwoLines(self, direction, topCorner, bottomCorner);
         //if the opponent is in the direction we want to kick then the penalty
         //kick cannot be taken.
         if(!Path.isInAWay(self, target, opponent, RobotInfo.ROBOT_SIZE/2)) {
              // the kick action is executed
             return Kick.SINGLETON;
         }
         //moves to face another point in the goal
         if((Math.abs(opponent.getY()-topCorner.getY())) < (Math.abs(opponent.getY()-bottomCorner.getY()))) {
                     return Spin.ToFace(bottomCorner, state.getSelf());
         }
         return Spin.ToFace(topCorner, state.getSelf());

         
       
        
        
    }
}
