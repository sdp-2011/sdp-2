///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import melmac.core.world.WorldState;
//import melmac.core.utils.Path;
//import melmac.core.world.Point;
//import melmac.core.utils.Distance;
//import melmac.core.world.RobotInfo;
//
///**
// *
// * @author pesho
// */
//public class GoStraightToBallStrategy implements IStrategy{
//
//    @Override
//    public double getUtility(WorldState state) {
//        double utility = 0;
//        Point ball = state.getBall().getPosition();
//        Point self = state.getSelf().getPose().getPosition();
//        Point opp = state.getOpponent().getPose().getPosition();
//        Point ownGoal = state.getOwnGoalCenter();
//        // TODO: create more complex utility calculation algorithm
//        if (Path.isBallInTheWay(state, ownGoal)){
//            return utility;
//        } else if (Path.isBallInOppPossession(state)){
//            return utility;
//        } else if (Distance.euclidean(self, ball) < RobotInfo.ROBOT_SIZE *3/ 4){
//            return utility;
//        } else if (Path.isInAWay(self, ball, opp, RobotInfo.ROBOT_SIZE)) {
//            return utility;
//        } else if (!Path.areWeBehindBall(state)){
//            return utility;
//        } else {
//            utility = 0.99;
//            return utility;
//        }
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        System.out.println("Go straight to ball");
//        Plan plan = new Plan();
//        int power = 100;
//
//        Point ball = state.getBall().getPosition();
//        Point targetGoal = state.getTargetGoalCenter();
//
//
//
//    	//System.out.println("Going straight to ball");
//        Point target;
//    	if (targetGoal.getX() < ball.getX())
//    	{
//            int targetX = ball.getX() + RobotInfo.ROBOT_SIZE*2/3;
//            int targetY = ball.getY();
//            target = new Point(targetX, targetY);
//
//            Action moveToTarget = new MoveToAction(target, power, true);
//            plan.getActions().add(moveToTarget);
//
//
//
//
//
//    	} else {
//            int targetX = ball.getX() - RobotInfo.ROBOT_SIZE*2/3;
//            int targetY = ball.getY();
//            target = new Point(targetX, targetY);
//
//            Action moveToTarget = new MoveToAction(target, power, true);
//            plan.getActions().add(moveToTarget);
//        }
//        return plan;
//
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
//        //if (plan.getActions().size() > 1)
//          //  return false;
//
//        //TODO: either change this or add move to ball in the plan
//        Action a = plan.getActions().peek();
//        if (a instanceof MoveToAction) {
//            MoveToAction mta = (MoveToAction)a;
//            if (Distance.euclidean(mta.getTarget(), state.getBall().getPosition()) < RobotInfo.ROBOT_SIZE/3){
//                return true;
//            }
//        }
//        return false;
//    }
//}
