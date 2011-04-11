package melmac.core.actions;

import melmac.core.interfaces.Controller;

/**
 Implementations of this interface are basic actions that can be selected by strategies as steps towards their goals.
 */
public interface Action
{

    /**
     Must be a single step operation (e.g. "start moving", not "move to"
     */
    public void execute(Controller controller);
}
