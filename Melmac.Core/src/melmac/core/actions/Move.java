package melmac.core.actions;

import java.util.logging.Level;
import java.util.logging.Logger;
import melmac.core.interfaces.Controller;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;

public final class Move implements Action
{

    private final int angle;
    private final int power;

    public Move(int angle, int power)
    {
        this.angle = angle;
        this.power = power;
    }

    public Move(int angle)
    {
        this(angle, 100);
    }

    public static Move To(Point target, RobotInfo self, int power) {
        int relativeAngle = (int)self.getRelativeAngleTo(target);
        return new Move(relativeAngle, power);
    }

    public static Move To(Point target, RobotInfo self) {
        int relativeAngle = (int)self.getRelativeAngleTo(target);
        return new Move(relativeAngle);
    }

    @Override
    public void execute(Controller controller)
    {
        Logger.getAnonymousLogger().log(Level.INFO, this.toString());
        controller.move(angle, power);
        Logger.getAnonymousLogger().log(Level.INFO, "Move executed");
    }

    @Override
    public String toString() {
        return "Moving to " + angle + " at power " + power;
    }

    public int getAngle() {
        return angle;
    }

}