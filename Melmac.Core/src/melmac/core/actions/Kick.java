package melmac.core.actions;

import melmac.core.interfaces.Controller;

public class Kick implements Action
{
    public static final Kick SINGLETON = new Kick();

    private Kick()
    {
    }

    @Override
    public String toString()
    {
        return "Kick";
    }

    @Override
    public void execute(Controller controller)
    {
        controller.kick();
    }
}
