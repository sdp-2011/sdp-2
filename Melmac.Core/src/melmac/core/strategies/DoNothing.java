package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.world.WorldState;

public final class DoNothing implements Strategy
{
    @Override
    public double getUtility(WorldState state)
    {
        return 1;
    }

    @Override
    public Action getNextAction(WorldState state)
    {
        return melmac.core.actions.DoNothing.SINGLETON;
    }
}