package melmac.core.world;

import java.util.Set;
import melmac.core.strategies.Strategy;

public final class UiInfo
{

    private final boolean stopRequested;
    private final boolean areWeBlue;
    private final boolean isTargetRight;
    private Set<Strategy> strategy;

    public UiInfo(boolean stopRequested, boolean areWeBlue, boolean isTargetRight, Set<Strategy> strategy)
    {
        this.stopRequested = stopRequested;
        this.areWeBlue = areWeBlue;
        this.isTargetRight = isTargetRight;
        this.strategy = strategy;
    }

    public boolean isStopRequested()
    {
        return stopRequested;
    }

    public boolean areWeBlue()
    {
        return areWeBlue;
    }

    public boolean isTargetRight()
    {
        return isTargetRight;
    }

    public Set<Strategy> getStrategies()
    {
        //Strategy temp = strategy;
        //strategy = null;
        return strategy;
    }
}
