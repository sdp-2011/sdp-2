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
//public class DribbleAndKickAction extends Action {
//
//    private final Point destination;
//
//    private final int RADIUS = 850; //20px
//    private final int DELTA = 2;
//
//    public DribbleAndKickAction(Point destination) {
//        this.destination = destination;
//    }
//
//    @Override
//    public String toString() {
//        return "DribbleAndKick: " + destination.toString();
//    }
//
//    public Point getDestination() {
//        return destination;
//    }
//
//    private double lastAngle = 9999;
//    private int lastPower = 0;
//    private int power = 12;
//
//    @Override
//    public boolean step(Controller controller, WorldState state)
//    {
//        int ballDistance = (int)Distance.euclidean(state.getSelf().getPose().getPosition(), state.getBall().getPosition());
//        if (Distance.squaredEuclidean(destination, state.getSelf().getPose().getPosition()) <= RADIUS) {
//            Logger.getAnonymousLogger().log(Level.INFO, "Kicking!: " + state.getSelf().getPose().getPosition() + ", " + destination.toString());
//            controller.kick();
//            controller.stop();
//            return true;
//        } else {
//
//            ballDistance = (int)Distance.euclidean(state.getSelf().getPose().getPosition(), state.getBall().getPosition());
//            if (ballDistance > 10) {
//                power =(int) (power *1.7);
//                if (power > 90)
//                    power = 90;
//            }
//
//            double angle = state.getSelf().getPose().getRelativeAngleTo(destination);
//
//            if (lastPower != power || Math.abs(angle - lastAngle) > DELTA) {
//                controller.move((int)angle, power);
//                Logger.getAnonymousLogger().log(Level.INFO, "Dribbling: " + state.getSelf().getPose().getPosition() + ", " + destination.toString() + ", " + String.valueOf(angle) + ", " + power);
//                Logger.getAnonymousLogger().log(Level.INFO, "Robot is facing: " + String.valueOf(state.getSelf().getPose().getAbsoluteAngle()));
//                lastPower = power;
//                lastAngle = angle;
//            }
//            return false;
//        }
//    }
//}
