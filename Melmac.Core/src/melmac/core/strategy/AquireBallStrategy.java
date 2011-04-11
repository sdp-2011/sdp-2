//package melmac.core.strategy;
//
//import melmac.core.utils.Distance;
//import melmac.core.utils.Angle;
//import melmac.core.world.Point;
//import melmac.core.world.WorldState;
//
//public final class AquireBallStrategy implements IStrategy
//{
//    private static final int CLOSE_DISTANCE = 35 * 35;
//    private static final int CLOSE_ANGLE = 10;
//
//    @Override
//    public boolean achievable(WorldState state)
//    {
//        // TODO
//
//        // Is the ball too close to a corner?
//
//        // Is the opponent blocking the ball near a wall?
//
//        return true;
//    }
//
//    @Override
//    public boolean achieved(WorldState state)
//    {
//        // Am I close to the ball?
//        double distance = Distance.squaredEuclidean(state.getSelf().getPose().getPosition(), state.getBall().getPosition());
//
//        if (distance > CLOSE_DISTANCE)
//        {
//            return false;
//        }
//
//        // Am I facing the ball
//        double ballAngle = state.getSelf().getPose().getRelativeAngleTo(state.getBall().getPosition());
//
//        if (Math.abs(ballAngle) > CLOSE_ANGLE)
//        {
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean feasible(WorldState state)
//    {
//        return true;
//    }
//
//    @Override
//    public double getUtility(WorldState state)
//    {
//        // Are we already in possession of the ball?
//        // Is the opponent in possession of the ball?
//
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean isPlanSound(WorldState state, Plan plan)
//    {
//
//        //shouldnt this check whether the distance between the ball the current
//        //position of the robot is above a threshold limit
//        return true;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan)
//    {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}
