/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Kick;
import melmac.core.world.WorldState;

/**
 *
 * @author s0827480
 */
public class JustKick implements Strategy{

    @Override
    public double getUtility(WorldState state) {
        return 0.10;
    }

    @Override
    public Action getNextAction(WorldState state) {
        return Kick.SINGLETON;
    }

}
