///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import melmac.core.utils.Path;
//import melmac.core.world.Point;
//import melmac.core.world.RobotInfo;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author pesho
// */
//public class DribbleAndKickStrategy implements IStrategy{
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
//        } else if (Path.isInAWay(self, targetGoal, opp, RobotInfo.ROBOT_SIZE)){
//            return utility;
//        } else {
//            utility = 0.99;
//            return utility;
//        }
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        System.out.println("Dribble and kick");
//        int power = 100;
//
//
//    	Point self = state.getSelf().getPose().getPosition();
//        Point ball = state.getBall().getPosition();
//        int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
//        Point targetGoal = state.getTargetGoalCenter();
//
//        Plan plan = new Plan();
//
//        Action faceBall = new FaceAction(self, targetGoal);
//        plan.getActions().add(faceBall);
//
//        int aTargetX;
//        int aTargetY;
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
//
//        Point aTarget = new Point(aTargetX, aTargetY);
//        System.out.println("Going to aTarget");
//
//        Action a = new DribbleAndKickAction(aTarget);
//    	plan.getActions().add(a);
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
//        Action[] actions =  plan.getActions().toArray(new Action[0]);
//        for (int i = 0; i < actions.length; i++){
//            if (actions[i] instanceof MoveToAction){
//                Point robot = state.getSelf().getPose().getPosition();
//                Point ball = state.getBall().getPosition();
//                if ((ball.getY() < robot.getY() - RobotInfo.ROBOT_SIZE) || (ball.getY() > robot.getY() + RobotInfo.ROBOT_SIZE)){
//                    Logger.getAnonymousLogger().log(Level.INFO, "Ball missed: " + robot.toString() + ", " + ball.toString());
//                    //if (Distance.euclidean(robot, ball) > RobotInfo.ROBOT_SIZE * 2)
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//}
