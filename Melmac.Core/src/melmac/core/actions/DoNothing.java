package melmac.core.actions;

import melmac.core.interfaces.Controller;

public final class DoNothing implements Action
{
    public static final DoNothing SINGLETON = new DoNothing();

    private DoNothing()
    {
    }

    @Override
    public String toString()
    {
        return "DoNothing";
    }

    @Override
    public void execute(Controller controller)
    {
    }
}
