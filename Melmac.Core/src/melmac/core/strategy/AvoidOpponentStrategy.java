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
//public class AvoidOpponentStrategy implements IStrategy {
//
//    @Override
//    public double getUtility(WorldState state) {
//        double utility = 0;
//        Point ball = state.getBall().getPosition();
//        Point self = state.getSelf().getPose().getPosition();
//        Point opp = state.getOpponent().getPose().getPosition();
//        Point ownGoal = state.getOwnGoalCenter();
//        // TODO: create more complex utility calculation algorithm
//        if (Distance.euclidean(self, ball) < RobotInfo.ROBOT_SIZE *3/ 4){
//            return utility;
//        } else if (Path.isBallInOppPossession(state)){
//            return utility;
//        } else if (Path.isBallInTheWay(state, ownGoal)){
//            return utility;
//        } else if (!Path.isInAWay(self, ball, opp, RobotInfo.ROBOT_SIZE)){
//            return utility;
//        } else {
//            utility = 0.99;
//            return utility;
//        }
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        System.out.println("Avoid opponent");
//        int power = 100;
//        Point opp = state.getOpponent().getPose().getPosition();
//        Point self = state.getSelf().getPose().getPosition();
//        Point ball = state.getBall().getPosition();
//        int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
//        int pitchHeight = state.getVisionInfo().getPitchInfo().getDimension().getHeight();
//        Point targetGoal = state.getTargetGoalCenter();
//        Point point;
//        Point point2;
//
//        Plan plan = new Plan();
//
//
//
//        if (Math.abs(ball.getX() - self.getX()) < Math.abs(ball.getY() - self.getY()))
//        {
//            // Find minimum X between opp and walls
//            double leftDistance = Distance.squaredEuclidean(opp, new Point(0, opp.getY()));
//            double rightDistance = Distance.squaredEuclidean(opp, new Point(pitchWidth, opp.getY()));
//
//            if (leftDistance < rightDistance)
//            {
//                System.out.println("Going right");
//                if (ball.getY() < opp.getY() && opp.getY() < self.getY())
//                {
//                    // objects are arranged from top to bottom: ball, opponent, self
//                    point = new Point((opp.getX() + pitchWidth) / 2, Math.min(opp.getY() + RobotInfo.ROBOT_SIZE, pitchHeight - RobotInfo.ROBOT_SIZE*2/3));
//                    point2 = new Point((opp.getX() + pitchWidth) / 2, Math.max(opp.getY() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3));
//                } else {
//                    // objects are arranged from top to bottom: self, opponent, ball
//                    point = new Point((opp.getX() + pitchWidth) / 2, Math.max(opp.getY() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3));
//                    point2 = new Point((opp.getX() + pitchWidth) / 2, Math.min(opp.getY() + RobotInfo.ROBOT_SIZE, pitchHeight - RobotInfo.ROBOT_SIZE*2/3));
//                }
//            }
//            else
//            {
//                System.out.println("Going left");
//                if (ball.getY() < opp.getY() && opp.getY() < self.getY())
//                {
//                    // objects are arranged from top to bottom: ball, opponent, self
//                    point = new Point(opp.getX() / 2, Math.min(opp.getY() + RobotInfo.ROBOT_SIZE, pitchHeight - RobotInfo.ROBOT_SIZE*2/3));
//                    point2 = new Point(opp.getX() / 2, Math.max(opp.getY() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3));
//                } else {
//                    // objects are arranged from top to bottom: self, opponent, ball
//                    point = new Point(opp.getX() / 2, Math.max(opp.getY() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3));
//                    point2 = new Point(opp.getX() / 2, Math.min(opp.getY() + RobotInfo.ROBOT_SIZE, pitchHeight - RobotInfo.ROBOT_SIZE*2/3));
//                }
//            }
//        }
//        else
//        {
//
//            // Find minimum Y between opp and walls
//            double topDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), 0));
//            double bottomDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), pitchHeight));
//
//            if (topDistance > bottomDistance)
//            {
//                System.out.println("Going up");
//                if (ball.getX() < opp.getX() && opp.getX() < self.getX())
//                {
//                    // objects are arranged from left to right: ball, opponent, self
//                    point = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), opp.getY() / 2);
//                    point2 = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), opp.getY() / 2);
//                } else {
//                    // objects are arranged from left to right: self, opponent, all
//                    point = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), opp.getY() / 2);
//                    point2 = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), opp.getY() / 2);
//                }
//            }
//            else
//            {
//                System.out.println("Going down");
//                if (ball.getX() < opp.getX() && opp.getX() < self.getX())
//                {
//                    // objects are arranged from left to right: ball, opponent, self
//                    point = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), (opp.getY() + pitchHeight) / 2);
//                    point2 = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), (opp.getY() + pitchHeight) / 2);
//                } else {
//                    // objects are arranged from left to right: self, opponent, ball
//                    point = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), (opp.getY() + pitchHeight) / 2);
//                    point2 = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), (opp.getY() + pitchHeight) / 2);
//                }
//            }
//        }
//
//
//        Action moveToPoint = new MoveToAction(point, power, false);
//        plan.getActions().add(moveToPoint);
//        Action moveToPoint2 = new MoveToAction(point2, power, false);
//        plan.getActions().add(moveToPoint2);
//
//    	System.out.println("Going straight to ball");
//        Point target;
//    	if (targetGoal.getX() < ball.getX())
//    	{
//            int targetX = ball.getX() + RobotInfo.ROBOT_SIZE*2/3;
//            int targetY = ball.getY();
//            target = new Point(targetX, targetY);
//    	} else {
//            int targetX = ball.getX() - RobotInfo.ROBOT_SIZE*2/3;
//            int targetY = ball.getY();
//            target = new Point(targetX, targetY);
//        }
//
//        Action moveToTarget = new MoveToAction(target, power, true);
//        plan.getActions().add(moveToTarget);
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
//        //TODO: either change this or add move to ball in the plan
//        Action[] actions =  plan.getActions().toArray(new Action[0]);
//        Action last_action = actions[actions.length - 1];
//        if (last_action instanceof MoveToAction) {
//            MoveToAction mta = (MoveToAction)last_action;
//            if (Distance.euclidean(mta.getTarget(), state.getBall().getPosition()) < RobotInfo.ROBOT_SIZE/3)
//                return true;
//        }
//        return false;
//    }
//
//}
