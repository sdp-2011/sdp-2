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
//public class GoAroundTheBallStrategy implements IStrategy {
//
//    @Override
//    public double getUtility(WorldState state) {
//        double utility = 0;
//        Point ball = state.getBall().getPosition();
//        Point self = state.getSelf().getPose().getPosition();
//        Point opp = state.getOpponent().getPose().getPosition();
//        Point ownGoal = state.getOwnGoalCenter();
//        // TODO: create more complex utility calculation algorithm
//        if (Path.areWeBehindBall(state)){
//            return utility;
//        } else if (Path.isBallInOppPossession(state)){
//            return utility;
//        } else if (Distance.euclidean(self, ball) < RobotInfo.ROBOT_SIZE *3/ 4){
//            return utility;
//        } else if (Path.isInAWay(self, ball, opp, RobotInfo.ROBOT_SIZE)){
//            return utility;
//        } else if (!Path.isBallInTheWay(state, ownGoal)){
//            return utility;
//        } else {
//            utility = 0.99;
//            return utility;
//        }
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        System.out.println("Go around the Ball");
//        Plan plan = new Plan();
//        int power = 100;
//
//        Point ball = state.getBall().getPosition();
//        int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
//        int pitchHeight = state.getVisionInfo().getPitchInfo().getDimension().getHeight();
//        Point targetGoal = state.getTargetGoalCenter();
//        Point point3;
//        Point point4;
//        Point target;
//
//
//    	System.out.println("Going around the ball");
//
//
//        if (targetGoal.getX() < ball.getX())
//    	{
//    		int targetX = ball.getX() + RobotInfo.ROBOT_SIZE*2/3;
//    		int targetY = ball.getY();
//    		target = new Point(targetX, targetY);
//
//                // Find minimum Y between ball and walls
//                double topDistance = Distance.squaredEuclidean(ball, new Point(ball.getX(), 0));
//                double bottomDistance = Distance.squaredEuclidean(ball, new Point(ball.getX(), pitchHeight));
//
//                if (topDistance > bottomDistance)
//                {
//                    System.out.println("Going up");
//                    // objects are arranged from left to right: ball, opponent, self
//                    point3 = new Point(Math.max(ball.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), ball.getY() - RobotInfo.ROBOT_SIZE);
//                    point4 = new Point(Math.min(ball.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), ball.getY() - RobotInfo.ROBOT_SIZE);
//                }
//                else
//                {
//                    System.out.println("Going down");
//                    // objects are arranged from left to right: ball, opponent, self
//                    point3 = new Point(Math.max(ball.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), ball.getY() + RobotInfo.ROBOT_SIZE);
//                    point4 = new Point(Math.min(ball.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), ball.getY() + RobotInfo.ROBOT_SIZE);
//                }
//
//    	} else {
//    		int targetX = ball.getX() - RobotInfo.ROBOT_SIZE*2/3;
//    		int targetY = ball.getY();
//    		target = new Point(targetX, targetY);
//
//                // Find minimum Y between ball and walls
//                double topDistance = Distance.squaredEuclidean(ball, new Point(ball.getX(), 0));
//                double bottomDistance = Distance.squaredEuclidean(ball, new Point(ball.getX(), pitchHeight));
//
//                if (topDistance > bottomDistance)
//                {
//                    System.out.println("Going up");
//                    // objects are arranged from left to right: ball, opponent, self
//                    point3 = new Point(Math.min(ball.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), ball.getY() - RobotInfo.ROBOT_SIZE);
//                    point4 = new Point(Math.max(ball.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), ball.getY() - RobotInfo.ROBOT_SIZE);
//                }
//                else
//                {
//                    System.out.println("Going down");
//                   // objects are arranged from left to right: ball, opponent, self
//                   point3 = new Point(Math.min(ball.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), ball.getY() + RobotInfo.ROBOT_SIZE);
//                   point4 = new Point(Math.max(ball.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), ball.getY() + RobotInfo.ROBOT_SIZE);
//                }
//
//    	}
//    	Action moveToPoint3 = new MoveToAction(point3, power, false);
//        plan.getActions().add(moveToPoint3);
//        Action moveToPoint4 = new MoveToAction(point4, power, false);
//        plan.getActions().add(moveToPoint4);
//
//        System.out.println("Going to target");
//
//        Action moveToTarget = new MoveToAction(target, power, true);
//    	plan.getActions().add(moveToTarget);
//
//        return plan;
//    }
//
//    @Override
//    public boolean achieved(WorldState state) {
//        return false;
//        //return Distance.euclidean(state.getSelf().getPose().getPosition(), state.getVisionInfo().getPositionInfo().getBallPosition()) < RobotInfo.ROBOT_SIZE;
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
//        //TODO: either change this or add move to ball in the plan
//        Action[] actions =  plan.getActions().toArray(new Action[0]);
//        if (actions.length == 0)
//        {
//            return false;
//        }
//        Action last_action = actions[actions.length - 1];
//        if (last_action instanceof MoveToAction) {
//            MoveToAction mta = (MoveToAction)last_action;
//            //TODO: consider soundness with posible previous plans!!!
//            if (Distance.euclidean(mta.getTarget(), state.getBall().getPosition()) < RobotInfo.ROBOT_SIZE/3){
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
