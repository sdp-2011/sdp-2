/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.control;

import java.util.logging.Level;
import java.util.logging.Logger;
import melmac.core.interfaces.Controller;

/**
 *
 * @author karel_evzen
 */
public class VoidController implements Controller {

    @Override
    public void move(int angle, int power)
    {
        Logger.getLogger(VoidController.class.getName()).log(Level.INFO, "move", new Object[] {angle, power});
    }

    @Override
    public void spin(int angle, int power)
    {
        Logger.getLogger(VoidController.class.getName()).log(Level.INFO, "spin", new Object[] {angle, power});
    }

    @Override
    public void stop()
    {
        Logger.getLogger(VoidController.class.getName()).log(Level.INFO, "stop");
    }

    @Override
    public void kick()
    {
        Logger.getLogger(VoidController.class.getName()).log(Level.INFO, "kick");
    }

    @Override
    public void curvedMove(int directionAngle, int Xdistance,int Ydistance, int spinAngle, int power)
    {
        Logger.getLogger(VoidController.class.getName()).log(Level.INFO, "curvedMove", new Object[] {directionAngle, Xdistance,Ydistance, spinAngle, power});
    }

}
