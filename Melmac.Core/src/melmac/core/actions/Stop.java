package melmac.core.actions;
import melmac.core.interfaces.Controller;

public class Stop implements Action
{
    public static final Stop SINGLETON = new Stop();

    private Stop()
    {
    }

    @Override
    public void execute(Controller controller)
    {
        controller.stop();
    }

    @Override
    public String toString()
    {
        return "Stopping";
    }
}
