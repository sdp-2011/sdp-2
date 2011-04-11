///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import melmac.core.utils.Path;
//import melmac.core.world.Point;
//import melmac.core.world.RobotInfo;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author pesho
// */
//public class BlockOpponentStrategy implements IStrategy{
//
//    @Override
//    public double getUtility(WorldState state) {
//        double utility = 0;
//        // TODO: create more complex utility calculation algorithm
//        if (Path.isBallInPossession(state)){
//            return utility;
//        } else if (!Path.isBallInOppPossession(state)){
//            return utility;
//        } else if (!Path.areWeBehindBall(state)){
//            return utility;
//        }  else {
//            utility = 0.99;
//            return utility;
//        }
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        System.out.println("Block opponent");
//        int power = 100;
//
//        Plan plan = new Plan();
//
//        Point opp = state.getOpponent().getPose().getPosition();
//        Point ownGoal = state.getOwnGoalCenter();
//        Point target;
//
//        if (opp.getX() < ownGoal.getX()){
//            target = new Point(opp.getX() + RobotInfo.ROBOT_SIZE, opp.getY());
//        } else {
//            target = new Point(opp.getX() - RobotInfo.ROBOT_SIZE, opp.getY());
//        }
//
//        Action block = new MoveToAction(target, power, true);
//        plan.getActions().add(block);
//
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
//        return getUtility(state) > 0.0;
//    }
//
//    @Override
//    public boolean feasible(WorldState state) {
//        return getUtility(state) > 0.5;
//    }
//
//    @Override
//    public boolean isPlanSound(WorldState state, Plan plan) {
//        if (plan.getActions().size() > 1)
//            return false;
//
//        Action a = plan.getActions().peek();
//        if (a instanceof MoveToAction) {
//            if (!Path.isBallInOppPossession(state)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
