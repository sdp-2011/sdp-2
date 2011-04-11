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
//public class KickAndAvoidStrategy implements IStrategy {
//
//    @Override
//    public double getUtility(WorldState state) {
//        double utility = 0;
//        Point ball = state.getVisionInfo().getPositionInfo().getBallPosition();
//        Point self = state.getSelf().getPose().getPosition();
//        Point opp = state.getOpponent().getPose().getPosition();
//        Point targetGoal = state.getTargetGoalCenter();
//        // TODO: create more complex utility calculation algorithm
//        if (Path.isBallInOppPossession(state)){
//            return utility;
//        } else if (!Path.isBallInPossession(state)){
//            return utility;
//        } else if (!Path.isInAWay(self, targetGoal, opp, RobotInfo.ROBOT_SIZE)){
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
//        System.out.println("Kick and avoid");
//        int power = 100;
//
//
//    	Point opp = state.getOpponent().getPose().getPosition();
//        Point self = state.getSelf().getPose().getPosition();
//        int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
//        int pitchHeight = state.getVisionInfo().getPitchInfo().getDimension().getHeight();
//        Point targetGoal = state.getTargetGoalCenter();
//
//        Plan plan = new Plan();
//
//        Point shootingTarget;
//        Point targetPoint;
//        //TODO: find a better targetPoint, rethink the logic below once movement predition for the ball is done
//
//            // TODO: test if better shootingTarget Point needs to be picked!
//        if (opp.getY() > self.getY()){
//            if (targetGoal.getX() < self.getX()){
//                shootingTarget = new Point (opp.getX(), pitchHeight);
//                targetPoint = new Point (opp.getX() + (self.getX() - opp.getX())*2/3, self.getY() + (pitchHeight - self.getY())*2/3);
//            } else {
//                shootingTarget = new Point (opp.getX(), pitchHeight);
//                targetPoint = new Point (self.getX() + (opp.getX() - self.getX())*2/3, self.getY() + (pitchHeight - self.getY())*2/3);
//            }
//        } else {
//            if (targetGoal.getX() < self.getX()){
//                shootingTarget = new Point (opp.getX(), 0);
//                targetPoint = new Point (opp.getX() + (self.getX() - opp.getX())*2/3, self.getY()*2/3);
//            } else {
//                shootingTarget = new Point (opp.getX(), 0);
//                targetPoint = new Point (self.getX() + (opp.getX() - self.getX())*2/3, self.getY()*2/3);//TODO: check whether self.getY()/2 is better!!
//            }
//        }
//
//        Action faceBall = new FaceAction(self, shootingTarget);
//        plan.getActions().add(faceBall);
//        Action kick = new KickAction();
//        plan.getActions().add(kick);
//        Action goAfterBall = new MoveToAction(targetPoint, power, false);
//        plan.getActions().add(goAfterBall);
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
//        Point targetGoal = state.getTargetGoalCenter();
//        if (!Path.isCloserTo(state.getVisionInfo().getPositionInfo().getBallPosition(), targetGoal, state.getSelf().getPose().getPosition())){
//            return false;
//        }
//        if (Path.isBallInOppPossession(state)){
//            return false;
//        }
//        return true;
//    }
//
//}
