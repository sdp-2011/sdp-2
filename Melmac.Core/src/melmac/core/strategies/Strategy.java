/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.world.WorldState;

/**
 *
 * @author karel_evzen
 */
public interface Strategy {
    public double getUtility(WorldState state);
    public Action getNextAction(WorldState state);
}
