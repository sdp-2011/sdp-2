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
//import melmac.core.utils.Path;
//import melmac.core.world.Point;
//import melmac.core.world.RobotInfo;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author pesho
// */
//public class DribbleAvoidAndKickStrategy implements IStrategy{
//
//    @Override
//    public double getUtility(WorldState state) {
//        double utility = 0;
//        Point self = state.getSelf().getPose().getPosition();
//        Point opp = state.getOpponent().getPose().getPosition();
//        Point targetGoal = state.getTargetGoalCenter();
//        // TODO: create more complex utility calculation algorithm
//        if (Path.isBallInOppPossession(state)){
//            return utility;
//        } else if (!Path.areWeBehindBall(state)){
//            return utility;
//        } else if (!Path.isInAWay(self, targetGoal, opp, RobotInfo.ROBOT_SIZE)){
//            return utility;
//        } else {
//            utility = 0.99;
//            return utility;
//        }
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        System.out.println("Dribble, avoid and kick");
//        int power = 100;
//
//
//    	Point opp = state.getOpponent().getPose().getPosition();
//        Point self = state.getSelf().getPose().getPosition();
//        Point ball = state.getBall().getPosition();
//        int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
//        int pitchHeight = state.getVisionInfo().getPitchInfo().getDimension().getHeight();
//        Point targetGoal = state.getTargetGoalCenter();
//
//        Plan plan = new Plan();
//
//        Action faceBall = new FaceAction(self, targetGoal);
//        plan.getActions().add(faceBall);
//
//        Point aPoint;
//        Point aPoint2;
//        int aTargetX = 0;
//        int aTargetY = 0;
//
//        if (targetGoal.getX() < 10)
//        {
//        	aTargetX = Math.min(ball.getX()*2/3, pitchWidth/4) + RobotInfo.ROBOT_SIZE;
//
//        	if (targetGoal.getY() >= ball.getY()){
//        		aTargetY = targetGoal.getY() - (targetGoal.getY() - ball.getY())/2;
//        	} else {
//        		aTargetY = targetGoal.getY() + (ball.getY() - targetGoal.getY())/2;
//        	}
//        } else {
//        	aTargetX = Math.max((ball.getX() + (targetGoal.getX() - ball.getX())/3) , pitchWidth*3/4) -RobotInfo.ROBOT_SIZE;
//        	if (targetGoal.getY() >= ball.getY()){
//        		aTargetY = targetGoal.getY() - (targetGoal.getY() - ball.getY())/2;
//        	} else {
//        		aTargetY = targetGoal.getY() + (ball.getY() - targetGoal.getY())/2;
//        	}
//        }
//        Point aTarget = new Point(aTargetX, aTargetY);
//
//        // Find minimum Y between opp and walls
//        double topDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), 0));
//        double bottomDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), pitchHeight));
//
//        if (topDistance > bottomDistance)
//        {
//            System.out.println("Going up");
//            if (aTarget.getX() < opp.getX() && opp.getX() < self.getX())
//            {
//             	// objects are arranged from left to right: ball, opponent, self
//               	aPoint = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), opp.getY() / 2);
//                aPoint2 = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), opp.getY() / 2);
//            } else {
//               	// objects are arranged from left to right: self, opponent, all
//               	aPoint = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), opp.getY() / 2);
//                aPoint2 = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), opp.getY() / 2);
//            }
//        }
//        else
//        {
//            System.out.println("Going down");
//            if (aTarget.getX() < opp.getX() && opp.getX() < self.getX())
//            {
//              	// objects are arranged from left to right: ball, opponent, self
//               	aPoint = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), (opp.getY() + pitchHeight) / 2);
//                aPoint2 = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), (opp.getY() + pitchHeight) / 2);
//            } else {
//               	// objects are arranged from left to right: self, opponent, ball
//               	aPoint = new Point(Math.max(opp.getX() - RobotInfo.ROBOT_SIZE, RobotInfo.ROBOT_SIZE*2/3), (opp.getY() + pitchHeight) / 2);
//                aPoint2 = new Point(Math.min(opp.getX() + RobotInfo.ROBOT_SIZE, pitchWidth - RobotInfo.ROBOT_SIZE*2/3), (opp.getY() + pitchHeight) / 2);
//            }
//        }
//
//        Action moveToAPoint = new MoveToAction(aPoint, power, false);
//    	plan.getActions().add(moveToAPoint);
//
//        Action moveToAPoint2 = new MoveToAction(aPoint2, power, false);
//        plan.getActions().add(moveToAPoint2);
//
//        System.out.println("Going to aTarget");
//
//
//        Action a = new DribbleAndKickAction(aTarget);
//    	plan.getActions().add(a);
//
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
//        //TODO: if opponent is no longer in the way we can just kick and score
//        Action[] actions =  plan.getActions().toArray(new Action[0]);
//        Point self = state.getSelf().getPose().getPosition();
//        Point prevTarget = self;
//        Point ball = state.getBall().getPosition();
//        Point opp = state.getOpponent().getPose().getPosition();
//        for (int i = 0; i < actions.length; i++){
//            if (actions[i] instanceof MoveToAction){
//                MoveToAction currentAction = (MoveToAction)actions[i];
//                Point target = currentAction.getTarget();
//                if (!Path.isInAWay(prevTarget, target, ball, RobotInfo.ROBOT_SIZE)){
//                    Logger.getAnonymousLogger().log(Level.INFO, "Ball missed: " + self.toString() + ", " + ball.toString());
//                    //if (Distance.euclidean(robot, ball) > RobotInfo.ROBOT_SIZE * 2)
//                    prevTarget = target;
//                    return false;
//                }
//                if (Path.isInAWay(prevTarget, target, opp, RobotInfo.ROBOT_SIZE)){
//                    Logger.getAnonymousLogger().log(Level.INFO, "Opponent in the way: " + self.toString() + ", " + opp.toString() + ", " + target.toString());
//                    return false;
//                }
//                prevTarget = target;
//            }
//            return true;
//        }
//        return true;
//    }
//
//}
