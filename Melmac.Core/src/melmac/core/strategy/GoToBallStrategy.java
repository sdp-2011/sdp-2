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
//public class GoToBallStrategy implements IStrategy {
//
//    @Override
//    public double getUtility(WorldState state)
//    {
//        Point self = state.getSelf().getPose().getPosition();
//        Point ball = state.getBall().getPosition();
//        Point target = state.getTargetGoalCenter();
//        if (Distance.euclidean(self, ball) < RobotInfo.ROBOT_SIZE)
//            return 0;
//
//        //this strategy will not deal with moving around the ball to face the goal
//        if (Math.signum(self.getDirectionTo(ball).getX()) != Math.signum(self.getDirectionTo(target).getX()))
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
//        double utility = 0.45;
//        if (ourDistance < theirDistance)
//            utility += 0.20;
//
//        if (ourDistance * 2 < theirDistance)
//            utility += 0.20;
//
//        return utility;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan)
//    {
//        Point ball = state.getBall().getPosition();
//        int ballOffset = RobotInfo.ROBOT_SIZE;
//        if (state.getUiInfo().isTargetRight()) {
//            ballOffset *= -1;
//        }
//        Point destination = ball.translate(ballOffset, 0);
//        return new Plan(new MoveToAction(destination, 100, true));
//    }
//
//    @Override
//    public boolean achieved(WorldState state)
//    {
//        return Distance.euclidean(state.getSelf().getPose().getPosition(), state.getBall().getPosition()) < RobotInfo.ROBOT_SIZE;
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
//        if (plan.getActions().size() > 1)
//            return false;
//
//        Action a = plan.getActions().peek();
//        if (a instanceof MoveToAction) {
//            MoveToAction mta = (MoveToAction)a;
//            if (Distance.euclidean(mta.getTarget(), state.getBall().getPosition()) <= RobotInfo.ROBOT_SIZE)
//                return true;
//        }
//        return false;
//    }
//}
