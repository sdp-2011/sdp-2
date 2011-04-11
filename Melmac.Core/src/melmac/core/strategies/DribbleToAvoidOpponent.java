/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.utils.Path;
import melmac.core.world.WorldState;
import melmac.core.world.Point;

/**
 *
 * @author s0830457
 */
public class DribbleToAvoidOpponent implements Strategy{

    @Override
    public double getUtility(WorldState state) {
        Point target = state.getTargetGoalCenter();
        Point self = state.getSelf().getPosition();
        Point opp = state.getOpponent().getPosition();
        if(!Path.isBallInPossession(state))
            return 0;
        if(!Path.isOpponentInTheWay(self, target, opp))
            return 0;
        return 0.5;
    }

    @Override
    public Action getNextAction(WorldState state) {
        //ToDo: The actions have to be implemented
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
