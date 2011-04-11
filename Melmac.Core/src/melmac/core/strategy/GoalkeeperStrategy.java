///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import melmac.core.utils.Distance;
//import melmac.core.utils.Path;
//import melmac.core.world.Point;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author pesho
// */
//public class GoalkeeperStrategy implements IStrategy{
//
//    @Override
//    public double getUtility(WorldState state) {
//        double utility = 0;
//        if (Path.isBallInPossession(state)){
//            return utility;
//        } else {
//            utility = utility + 0.25;
//            if (!Path.isBallInOppPossession(state)){
//                return utility;
//            } else {
//                utility = utility + 0.25;
//                if (!Path.areWeGoalkeeper(state)){
//                    return utility;
//                } else {
//                    utility = utility + 0.49;
//                    return utility;
//                }
//            }
//        }
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        int power = 100;
//
//        Plan plan = new Plan();
//
//
//        if (Path.isBallInOppPossession(state)){
//            int xDist = Math.abs(state.getSelf().getPose().getPosition().getX() - state.getOpponent().getPose().getPosition().getX());
//            int yDist = xDist*state.getOpponent().getPose().getDirectionVector().getX()/state.getOpponent().getPose().getDirectionVector().getY();
//            Point target = new Point(state.getSelf().getPose().getPosition().getX(), state.getOpponent().getPose().getPosition().getY() + yDist);//TODO: TEST THIS!!!
//
//            Action moveToAction = new MoveToAction(target, power, true);
//            plan.getActions().add(moveToAction);
//        } else {
//            int xDirection = state.getOpponent().getPose().getPosition().getX() - state.getVisionInfo().getPositionInfo().getBallPosition().getX();
//            int yDirection = state.getOpponent().getPose().getPosition().getY() - state.getVisionInfo().getPositionInfo().getBallPosition().getY();
//            int xDist = Math.abs(state.getSelf().getPose().getPosition().getX() - state.getVisionInfo().getPositionInfo().getBallPosition().getX());
//            int yDist = xDist*xDirection/yDirection;
//            Point target = new Point(state.getSelf().getPose().getPosition().getX(), state.getVisionInfo().getPositionInfo().getBallPosition().getY() + yDist);//TODO: TEST THIS!!!
//
//            Action moveToAction = new MoveToAction(target, power, true);
//            plan.getActions().add(moveToAction);
//        }
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
//        return getUtility(state) > 0;
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
//            MoveToAction mta = (MoveToAction)a;
//            if (Path.isBallInOppPossession(state)){
//                return true;
//            } else {
//                Point opp = state.getOpponent().getPose().getPosition();
//                if (Path.isCloserTo(opp, state.getVisionInfo().getPositionInfo().getBallPosition(), state.getSelf().getPose().getPosition())) {
//                    Point target = mta.getTarget();
//                    Point self = state.getSelf().getPose().getPosition();
//                    Point ball = state.getVisionInfo().getPositionInfo().getBallPosition();
//                    if ((self.getY() < target.getY() && target.getY() - 30 < ball.getY()) ||
//                            (self.getY() > target.getY() && target.getY() + 30 > ball.getY())){
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//}
