/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.utils.Path;
import melmac.core.utils.PointOfIntersection;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

/**
 *
 * @author s0830457
 */
public class Goalkeeper implements Strategy {

    @Override
    public double getUtility(WorldState state) {

        if(!Path.areWeGoalkeeper(state))
            return 0;

        return 0.5;
    }

    @Override
    public Action getNextAction(WorldState state) {
        Point velocity = state.getOpponent().getVelocity();
        Point opp = state.getOpponent().getPosition();
        Point self = state.getSelf().getPosition();
        //calculating another point on the same line as the our robot
       //the x coordinate remains the same, only y varies along the same vertical line as the goal center
        Point anotherPoint = self.translate(0,5);

        //predicting the point where the opponent might score a goal.
         Point target = PointOfIntersection.intersectionOfTwoLines(velocity, opp, self, anotherPoint);
        // = new Point((int)t[0],(int)t[1]);

        //moving to the point inorder to defend the goal.
        return Move.To(target,state.getSelf());
        
    }

}
