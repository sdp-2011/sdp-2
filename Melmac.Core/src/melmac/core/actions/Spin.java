/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.actions;

import melmac.core.interfaces.Controller;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;

/**
 *
 * @author karel_evzen
 */
public class Spin implements Action {

    private final int relativeAngle;
    private final int power;

    public Spin(int relativeAngle, int power) {
        this.relativeAngle = relativeAngle;
        this.power = power;
    }

    public Spin(int relativeAngle) {
        this(relativeAngle, 100);
    }

    public static Spin ToFace(Point target, RobotInfo self)
    {
        int relativeAngle = (int)self.getRelativeAngleTo(target);
        return new Spin(relativeAngle);
    }

    @Override
    public String toString() {
        return "Spin: " + relativeAngle;
    }

    @Override
    public void execute(Controller controller)
    {
        controller.spin(relativeAngle, power);
    }
}