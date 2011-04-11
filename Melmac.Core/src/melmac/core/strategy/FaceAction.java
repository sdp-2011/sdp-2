///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import melmac.core.interfaces.Controller;
//import melmac.core.utils.Angle;
//import melmac.core.world.Point;
//import melmac.core.world.Pose;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author karel_evzen
// */
//public class FaceAction extends Action {
//
//    private final int angle;
//
//    public FaceAction(int angle) {
//        this.angle = angle;
//    }
//
//    public FaceAction(Point self, Point target) {
//    	Point vector = self.getDirectionTo(target);
//    	int a = (int)Angle.getAbsoluteAngle(vector);
//        this.angle = a;
//    }
//
//    @Override
//    public String toString() {
//        return "Face: " + angle;
//    }
//
//    @Override
//    public boolean step(Controller controller, WorldState state)
//    {
//        Pose pose = state.getSelf().getPose();
//        int currentAngle = (int)pose.getAbsoluteAngle();
//        int relative = angle - currentAngle;
//        Logger.getAnonymousLogger().log(Level.INFO, "Rotating: " + String.valueOf(relative));
//        controller.spin(relative, 100);
//        return true;
//    }
//}
