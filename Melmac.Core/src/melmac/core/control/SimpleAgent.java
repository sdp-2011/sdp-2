package melmac.core.control;

import java.util.Set;
import java.util.logging.Level;
import melmac.core.strategies.Strategy;
import melmac.core.interfaces.Controller;
import melmac.core.actions.Action;
import melmac.core.logging.LogMessage;
import melmac.core.logging.Logger;
import melmac.core.threading.AsyncProcess;
import melmac.core.world.WorldState;
import melmac.core.world.WorldStateProvider;

public final class SimpleAgent extends AsyncProcess
{

    private static final String THREAD_NAME = "SimpleAgent";
    private final WorldStateProvider worldStateProvider;
    private final Controller controller;
    private final Strategy stopStrategy;
    private final Set<Strategy> autoStrategies;
    private Action lastAction = null;

    public SimpleAgent(Logger logger, WorldStateProvider worldStateProvider, Controller controller,
                       Strategy stopStrategy, Set<Strategy> autoStrategies)
    {
        super(logger, LogMessage.AgtStarted, LogMessage.AgtStopped, LogMessage.Agent_Died, LogMessage.AgtSgnlled,
              THREAD_NAME);
        this.worldStateProvider = worldStateProvider;
        this.controller = controller;
        this.stopStrategy = stopStrategy;
        this.autoStrategies = autoStrategies;
        worldStateProvider.addSubscriber(this);
    }

    @Override
    protected boolean shouldWait() throws Exception
    {
        // The agent has no way to find out whether the current frame has changed so a wait is always needed
        return true;
    }

    @Override
    protected boolean execute()
    {
        WorldState worldState = worldStateProvider.getWorldState();

        if (worldState == null)
        {
            return true;
        }

        Strategy strategy;

        if (worldState.getUiInfo().getStrategies() != null)
        {
            strategy = pickStrategy(worldState, worldState.getUiInfo().getStrategies());
        }
        else
        {
            strategy = pickStrategy(worldState, autoStrategies);
        }

        java.util.logging.Logger.getAnonymousLogger().log(Level.INFO, strategy.getClass().getSimpleName());
        Action action = strategy.getNextAction(worldState);

        /* THIS CHECK HAS BEEN TEMPORARILY COMMENTED OUT TO ENABLE SIMULATOR TOWORK
         * IT SHOULD BE REMOVED ONCE SIMULATOR IS FIXED
         *
        // if current and next actions are moves and the current action is
        // trying to correct a diviation < 2 degrees, skip current action.
        boolean skip = false;
        if (lastAction instanceof Move && action instanceof Move &&
        Math.abs(((Move) lastAction).getAngle() - ((Move) action).getAngle()) < 2)
        {
        skip = true;
        }
        lastAction = action;
        if (!skip)
        {
         */
        action.execute(controller);
        //}
        return true;
    }

    private Strategy pickStrategy(WorldState state, Set<Strategy> strategies)
    {
        // When the current case is not covered and all strategies
        // return 0, the stopStrategy is picked.
        double bestUtility = 0;
        // Default to doing nothing (also ensures the result is never null)
        Strategy bestStrategy = stopStrategy;

        for (Strategy strategy : strategies)
        {
            double utility = strategy.getUtility(state);

            if (utility > bestUtility)
            {
                bestStrategy = strategy;
                bestUtility = utility;
            }
        }

        if (bestUtility == 0)
        {
            worldStateProvider.getUiInfoProvider().resetStrategySelection();
        }

        java.util.logging.Logger.getAnonymousLogger().log(Level.INFO, bestStrategy.getClass().getSimpleName());
        return bestStrategy;
    }
}
