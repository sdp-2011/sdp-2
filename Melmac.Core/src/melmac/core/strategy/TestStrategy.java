///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import melmac.core.world.WorldState;
//
///**
// *
// * @author karel_evzen
// */
//public class TestStrategy implements IStrategy {
//
//    @Override
//    public double getUtility(WorldState state)
//    {
//        return 0;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan)
//    {
//        Plan p = new Plan();
//        p.getActions().add(new TestAction());
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
