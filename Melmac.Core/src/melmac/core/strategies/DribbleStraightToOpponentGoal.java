/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.utils.Path;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

/**
 *
 * @author s0830457
 */
public class DribbleStraightToOpponentGoal implements Strategy {
   

    @Override
    public double getUtility(WorldState state) {
        Point self = state.getSelf().getPosition();
        Point opp =  state.getOpponent().getPosition();
        Point target = state.getTargetGoalCenter();

        
        if(!Path.isBallInPossession(state))
            return 0;
    
        if( Path.isOpponentInTheWay(self, target, opp))
            return 0;
        
        return 0.5;

    }

    @Override
    public Action getNextAction(WorldState state) {
        int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
        Point goalCenter = state.getTargetGoalCenter();

        //the offset value added to the center depends on the power of the kicker, as in
        //from what distance fromt the goal center would te robot be able to score a goal.
        int offsetX;
        if(state.isTargetRight()){
            offsetX = (goalCenter.getX() - RobotInfo.ROBOT_SIZE);
        } else {
            offsetX = (goalCenter.getX() + RobotInfo.ROBOT_SIZE);
        }

        Point target = goalCenter.translate(offsetX, 0);

        //The coefficients for power are subjective to the kicking power of robot and
        //require a lot of testing before fixing their values.
        //These are just rough estimates based on the distance of the  robot from the goal

       //ToDo: A lot of testing
        if(state.getDistanceSelfToGoalCenter() < 1/3 * pitchWidth)
            return Move.To(target, state.getSelf(), state.getMaxSpeed());
        else
            return Move.To(target, state.getSelf(), state.getMaxSpeed());
    }

}
