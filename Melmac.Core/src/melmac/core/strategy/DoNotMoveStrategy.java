///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import melmac.core.strategy.Action;
//import melmac.core.world.WorldState;
//
///**
// *
// * @author karel_evzen
// */
//public class DoNotMoveStrategy implements IStrategy {
//
//    @Override
//    public double getUtility(WorldState state)
//    {
//        if (state.getUiInfo().isStopRequested())
//            return 1;
//        return 0;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan)
//    {
//        Plan p = new Plan();
//        p.getActions().add(new DoNothingAction());
//        p.getActions().add(new DoNothingAction());
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
//        /*Action[] actions = plan.getActions().toArray(new Action[1]);
//        if ((actions.length == 1) && (actions[0] instanceof DoNothingAction))*/
//            return true;
//       // return false;
//    }
//
//}
