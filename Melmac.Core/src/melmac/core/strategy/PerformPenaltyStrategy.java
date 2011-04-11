//package melmac.core.strategy;
//
//import melmac.core.world.Point;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author petar
// */
//public class PerformPenaltyStrategy implements IStrategy{
//
//    @Override
//    public double getUtility(WorldState state) {
//        return 0;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan) {
//        System.out.println("Perform penalty");
//        Plan plan = new Plan();
//
//        // wait to allow the opponent to move
//        Action wait = new WaitAction();
//        plan.getActions().add(wait);
//
//        Point opp = state.getOpponent().getPose().getPosition();
//        Point self = state.getSelf().getPose().getPosition();
//        Point targetGoal = state.getTargetGoalCenter();
//        Point topCorner = new Point(targetGoal.getX(), targetGoal.getY()/2 + 30);
//        Point bottomCorner = new Point(targetGoal.getX(), targetGoal.getY()*3/2 - 30);
//        // check where opponent is and face the other direction
//        if (opp.getY() < targetGoal.getY()){
//            //opp is up, shoot down
//            Action faceBall = new FaceAction(self, bottomCorner);
//            plan.getActions().add(faceBall);
//
//        } else {
//            //opp is down, shoot up
//            Action faceBall = new FaceAction(self, topCorner);
//            plan.getActions().add(faceBall);
//        }
//
//        // kick to score
//        Action kick = new KickAction();
//        plan.getActions().add(kick);
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
//        return true;
//    }
//
//    @Override
//    public boolean feasible(WorldState state) {
//        return true;
//    }
//
//    @Override
//    public boolean isPlanSound(WorldState state, Plan plan) {
//        // TODO: might need a more complex method
//        return true;
//    }
//}