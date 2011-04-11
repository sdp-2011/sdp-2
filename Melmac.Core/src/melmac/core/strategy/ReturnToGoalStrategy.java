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
//import melmac.core.world.RobotInfo;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author pesho
// */
//public class ReturnToGoalStrategy implements IStrategy{
//
//    @Override
//    public double getUtility(WorldState state) {
//        double utility = 0;
//        // TODO: create more complex utility calculation algorithm
//        if (Path.isBallInPossession(state)){
//            return utility;
//        } else if (!Path.isBallInOppPossession(state)){
//            return utility;
//        } else if (Path.areWeBehindBall(state)){
//            return utility;
//        }  else {
//            utility = 0.99;
//            return utility;
//        }
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        System.out.println("Return to goal");
//        int power = 100;
//
//        Plan plan = new Plan();
//
//        Point ownGoal = state.getOwnGoalCenter();
//        Point goaliePosition;
//        if (ownGoal.getX() < 50){
//            goaliePosition = new Point(ownGoal.getX() + RobotInfo.ROBOT_SIZE, ownGoal.getY());
//        } else {
//            goaliePosition = new Point(ownGoal.getX() - RobotInfo.ROBOT_SIZE, ownGoal.getY());
//        }
//        Action returnToGoal = new MoveToAction(goaliePosition, power, true);
//        plan.getActions().add(returnToGoal);
//
//        return plan;
//    }
//
//    @Override
//    public boolean achieved(WorldState state) {
//        return Distance.euclidean(state.getSelf().getPose().getPosition(), state.getBall().getPosition()) < RobotInfo.ROBOT_SIZE;
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
//        // TODO: add conditions
//        if (!Path.isBallInOppPossession(state)){
//            return false;
//        }
//        //if lines below are included the robot will never reach the goalline.
//        // This may be used for calling BlockOpponentStrategy
//        // (if we are blocking and the ball appears behind us, go back to the goal)
//        /*Point self = state.getSelf().getPose().getPosition();
//        Point ball = state.getVisionInfo().getPositionInfo().getBallPosition();
//        Point ownGoal = state.getOwnGoal();
//        if (Distance.euclidean(ball, ownGoal) > Distance.euclidean(self, ownGoal)){
//            return false;
//        } */
//        return true;
//    }
//
//}
