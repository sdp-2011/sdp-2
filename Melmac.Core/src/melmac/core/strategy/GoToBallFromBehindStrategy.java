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
// * @author karel_evzen
// */
//public class GoToBallFromBehindStrategy implements IStrategy {
//
//    private int MAX_ADVANTAGE = 100;
//
//    @Override
//    public double getUtility(WorldState state)
//    {
//        Point self = state.getSelf().getPose().getPosition();
//        Point ball = state.getVisionInfo().getPositionInfo().getBallPosition();
//        Point target = state.getTargetGoalCenter();
//
//        if (Path.isBallInPossession(state))
//            return 0;
//
//        //this strategy will only deal with moving around the ball to face the goal
//        if (Math.signum(self.getDirectionTo(ball).getX()) == Math.signum(self.getDirectionTo(target).getX()))
//            return 0;
//
//        //no opponents in the way please!
//        if (Path.isInAWay(self, ball, state.getOpponent().getPose().getPosition(), RobotInfo.ROBOT_SIZE)) {
//            return 0;
//        }
//
//        double ourDistance = Distance.euclidean(ball, self);
//        double theirDistance = Distance.euclidean(ball, state.getOpponent().getPose().getPosition());
//
//        double utility = 0.30;
//
//        if (ourDistance < theirDistance) {
//            utility += 0.30;
//            if (ourDistance * 2 < theirDistance)
//                utility += 0.40;
//        } else if (theirDistance - ourDistance < MAX_ADVANTAGE) {
//            double portion = (theirDistance - ourDistance) / MAX_ADVANTAGE;
//            utility += 0.7 * portion;
//        }
//
//        return utility;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan)
//    {
//        Plan p = new Plan();
//
//        Point ball = state.getVisionInfo().getPositionInfo().getBallPosition();
//        int ballOffset = RobotInfo.ROBOT_SIZE;
//        if (state.getUiInfo().isTargetRight()) {
//            ballOffset *= -1;
//        }
//        Point destination = ball.translate(ballOffset, 0);
//
//        //should we go bottom?
//        boolean goBottom;
//
//        //are we above the ball?
//        if (state.getSelf().getPose().getPosition().getY() < ball.getY()) {
//            if (ball.getY() - RobotInfo.ROBOT_SIZE < 0) {
//                goBottom = true; //not enough space above
//            } else {
//                goBottom = false;
//            }
//        } else {
//            if (ball.getY() + RobotInfo.ROBOT_SIZE > state.getVisionInfo().getPitchInfo().getDimension().getHeight()) {
//                goBottom = false; //not enough space bellow
//            } else {
//                goBottom = true;
//            }
//        }
//
//        if (goBottom)
//            p.getActions().add(new MoveToAction(destination.translate(0, RobotInfo.ROBOT_SIZE), 100, false));
//        else
//            p.getActions().add(new MoveToAction(destination.translate(0, -RobotInfo.ROBOT_SIZE), 100, false));
//
//        p.getActions().add(new MoveToAction(destination, 100, true));
//
//        return p;
//    }
//
//    @Override
//    public boolean achieved(WorldState state)
//    {
//        return Path.isBallInPossession(state);
//    }
//
//    @Override
//    public boolean achievable(WorldState state)
//    {
//        return getUtility(state) > 0;
//    }
//
//    @Override
//    public boolean feasible(WorldState state)
//    {
//        return getUtility(state) > 50;
//    }
//
//    @Override
//    public boolean isPlanSound(WorldState state, Plan plan)
//    {
//        //too complicated plan, this strategy can be achieved easily
//        if (plan.getActions().size() > 2)
//            return false;
//
//        Action a = plan.getActions().peek();
//        if (a instanceof MoveToAction) {
//            MoveToAction mta = (MoveToAction)a;
//            if (!Path.isCloserTo(state.getVisionInfo().getPositionInfo().getBallPosition(), state.getTargetGoalCenter(), mta.getTarget()))
//                return false;
//
//            if (Distance.euclidean(mta.getTarget(), state.getVisionInfo().getPositionInfo().getBallPosition()) < RobotInfo.ROBOT_SIZE)
//                return true;
//        }
//        return false;
//    }
//}
