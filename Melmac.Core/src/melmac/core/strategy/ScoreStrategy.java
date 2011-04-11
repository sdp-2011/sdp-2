///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import melmac.core.utils.Distance;
//import melmac.core.world.Point;
//import melmac.core.world.RobotInfo;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author karel_evzen
// */
//public class ScoreStrategy implements IStrategy {
//
//    private final int MAX_DISTANCE = RobotInfo.ROBOT_SIZE * 2;
//    private final int FACING_DELTA = 40;
//
//    @Override
//    public double getUtility(WorldState state)
//    {
//        Point ball = state.getBall().getPosition();
//        RobotInfo self = state.getSelf();
//
//        double distance = Distance.euclidean(self.getPose().getPosition(), ball);
//        if (distance > MAX_DISTANCE)
//            return 0;
//
//        double angleDelta = Math.abs(self.getPose().getRelativeAngleTo(ball));
//        if (angleDelta > FACING_DELTA)
//            return 0;
//
//        //if we are closer to the goal than the ball we need to navigate to a proper scoring position
//        if (Distance.euclidean(state.getTargetGoalCenter(), self.getPose().getPosition()) < Distance.euclidean(state.getTargetGoalCenter(), ball))
//            return 0;
//
//        //TODO: take into account the position of the opponent
//        return 0.50 + (0.25 * (MAX_DISTANCE - distance) * (1 / MAX_DISTANCE)) + (0.25 * (FACING_DELTA - angleDelta) * (1 / FACING_DELTA));
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan)
//    {
//        Point ball = state.getBall().getPosition();
//        Point goal = state.getTargetGoalCenter();
//
//        Action spin = new FaceAction(ball, goal);
//
//        int ballOffset = RobotInfo.ROBOT_SIZE / 2;
//        if (state.getUiInfo().isTargetRight()) {
//            ballOffset *= -1;
//        }
//
//        Action move = new MoveToAction(ball.translate(ballOffset, 0), 100, false);
//
//        Plan p = new Plan(spin, move, new KickAction(), new StopAction());
//        return p;
//    }
//
//    @Override
//    public boolean achieved(WorldState state)
//    {
//        return false;
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
//        return getUtility(state) > 60;
//    }
//
//    @Override
//    public boolean isPlanSound(WorldState state, Plan plan)
//    {
//        Action[] actions  = plan.getActions().toArray(new Action[0]);
//        //too complicated plan, can be achieved easily
//        if (actions.length > 5)
//            return false;
//
//        for(int i = actions.length -1; i >= 0 ; i--) {
//            if (actions[i] instanceof KickAction) {
//                Point beforeKick = null;
//                for(int j = i; j >= 0; j--) {
//                    if (actions[j] instanceof MoveToAction) {
//                        beforeKick = ((MoveToAction)actions[j]).getTarget();
//                    }
//                }
//                if (beforeKick == null)
//                    beforeKick = state.getSelf().getPose().getPosition();
//                if (Distance.euclidean(beforeKick, state.getBall().getPosition()) < 40)
//                    return true;
//                break;
//            }
//        }
//        return false;
//    }
//}
