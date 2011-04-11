//package melmac.core.strategy;
//
//import melmac.core.world.Point;
//import melmac.core.world.WorldState;
//import melmac.core.utils.Distance;
//
//public class DefenceStrategy implements IStrategy {
//
//	@Override
//    public double getUtility(WorldState state)
//    {
//        //calculate the distance from the robot to the ball to see if Attacking Strategy is a meaningful option in the current state.
//
//
//    	// get positions of the robot and the ball
//    	Point positionOpponent = state.getOpponent().getPose().getPosition();
//        Point positionBall = state.getBall().getPosition();
//        Point targetGoal = state.getTargetGoalCenter();
//        Point ownGoal = state.getOwnGoalCenter();
//
//        // get the dimensions(x_max,y_max) of the pitch and create two points (0,0) and (x_max,y_max)
//        int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
//        int pitchHeight = state.getVisionInfo().getPitchInfo().getDimension().getHeight();
//        Point startOfPitch = new Point(0,0);
//        Point endOfPitch = new Point(pitchWidth, pitchHeight);
//
//
//        // calculate distance to Ball and maximum possible distance (diagonal of the pitch)
//        double distToBall = Distance.squaredEuclidean(positionOpponent, positionBall);
//        double maxDist = Distance.squaredEuclidean(startOfPitch, endOfPitch);
//
//        //calculate utilityValue (range from 0 to 1) where 1 is when the robot is closest to the ball
//        //double utilityValue = 1 - distToBall / maxDist;
//    	double utilityValue = 0; //for milestone 3, remove this afterwards and uncomment the previous line
//
//    	return utilityValue;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan)
//    {
//        Plan p = new Plan();
//        p.getActions().add(new DoNothingAction());
//        return p;
//    }
//
//    @Override
//    public boolean achieved(WorldState state)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean achievable(WorldState state)
//    {
//        return state.getUiInfo().isStopRequested();
//    }
//
//    @Override
//    public boolean feasible(WorldState state)
//    {
//        return state.getUiInfo().isStopRequested();
//    }
//
//    @Override
//    public boolean isPlanSound(WorldState state, Plan plan)
//    {
//        return state.getUiInfo().isStopRequested();
//    }
//}
