///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import melmac.core.utils.Distance;
//import melmac.core.world.Point;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author karel_evzen
// */
//public class JustMoveStrategy implements IStrategy {
//
//    @Override
//    public double getUtility(WorldState state)
//    {
//        return 0;
//        //return 0.5;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan)
//    {
//        Action a = new MoveToAction(new Point(100, 100), 100, true);
//        Action b = new FaceAction(90);
//        Action c = new MoveToAction(new Point(500, 220), 100, true);
//        Action d = new FaceAction(180);
//        Plan plan = new Plan();
//        plan.getActions().add(a);
//        plan.getActions().add(b);
//        plan.getActions().add(c);
//        plan.getActions().add(d);
//        return plan;
//    }
//
//    @Override
//    public boolean achieved(WorldState state)
//    {
//        return Distance.squaredEuclidean(new Point(500, 220), state.getSelf().getPose().getPosition()) < 225; // 15px
//    }
//
//    @Override
//    public boolean achievable(WorldState state)
//    {
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
//    public boolean isPlanSound(WorldState state, Plan plan)
//    {
//        return true;
//    }
//
//}
