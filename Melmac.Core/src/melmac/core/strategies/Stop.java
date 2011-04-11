/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.world.WorldState;

/**
 *
 * @author s0838375
 */
public class Stop implements Strategy {

    @Override
    public double getUtility(WorldState state) {
        if (state.areWeBehindBall() && state.areWeNextToBall() && state.areWeMovingSideways())
        {
            return 0.99;
        }
        return 0.01;
    }
    
    @Override
    public Action getNextAction(WorldState state) {
        return melmac.core.actions.Stop.SINGLETON;
    }
}
