///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import melmac.core.utils.Distance;
//import melmac.core.interfaces.Controller;
//import melmac.core.world.Point;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author karel_evzen
// */
//public class MoveToAction extends Action {
//
//    private final int RADIUS = 400; //20px
//    private final int DELTA = 2;
//
//    private final Point target;
//    private final int power;
//    private final boolean stop;
//
//    public MoveToAction(Point target, int power, boolean stop) {
//        this.target = target;
//        this.power = power;
//        this.stop = stop;
//    }
//
//    public Point getTarget() {
//        return target;
//    }
//
//    //TODO: unsafe if the same action instance reused in a plan
//    private double lastAngle = 9999;
//
//    @Override
//    public boolean step(Controller controller, WorldState state)
//    {
//        if (Distance.squaredEuclidean(target, state.getSelf().getPose().getPosition()) <= RADIUS) {
//            if (stop) {
//                controller.stop();
//            }
//            return true;
//        } else {
//            double angle = state.getSelf().getPose().getRelativeAngleTo(target);
//            if (Math.abs(angle - lastAngle) > DELTA) {
//                Logger.getAnonymousLogger().log(Level.INFO, state.getSelf().getPose().getPosition() + ", " + target + ", " + String.valueOf(angle));
//                Logger.getAnonymousLogger().log(Level.INFO, "Robot is facing: " + String.valueOf(state.getSelf().getPose().getAbsoluteAngle()));
//                controller.move((int)angle, power);
//                lastAngle = angle;
//            }
//            return false;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "MoveTo: " + target.toString();
//    }
//}
