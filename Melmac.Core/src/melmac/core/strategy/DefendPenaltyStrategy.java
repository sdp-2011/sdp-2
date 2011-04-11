///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import melmac.core.world.Point;
//import melmac.core.world.WorldState;
//import melmac.core.world.UiInfo;
//
///**
// *
// * @author pesho
// */
//public class DefendPenaltyStrategy implements IStrategy {
//
//    @Override
//    public double getUtility(WorldState state) {
//        return 0;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        // TODO: re-execute this plan until the ball is kicked by the opponent
//        // to do this you ned to keep the areWeDefendingPenalty to true, untill the ball has been kicked
//        System.out.println("Defend penalty");
//        int power = 100;
//
//        Plan plan = new Plan();
//        Point target;
//        int xDist = Math.abs(state.getSelf().getPose().getPosition().getX() - state.getOpponent().getPose().getPosition().getX());
//        int yDist = xDist*state.getOpponent().getPose().getDirectionVector().getX()/state.getOpponent().getPose().getDirectionVector().getY();
//        target = new Point(state.getSelf().getPose().getPosition().getX(), state.getOpponent().getPose().getPosition().getY() + yDist);//TODO: TEST THIS!!!
//        Action moveToAction = new MoveToAction(target, power, true);
//        plan.getActions().add(moveToAction);
//        return plan;
//    }
//
//    @Override
//    public boolean achieved(WorldState state) {
//        return false;
//    }
//
//    @Override
//    public boolean achievable(WorldState state) {
//        return true;
//    }
//
//    @Override
//    public boolean feasible(WorldState state) {
//        return true;
//    }
//
//    @Override
//    public boolean isPlanSound(WorldState state, Plan plan) {
//        // might need more complex method
//        return true;
//    }
//
//}
