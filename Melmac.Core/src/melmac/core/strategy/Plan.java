///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package melmac.core.strategy;
//
//import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.Queue;
//
///**
// *
// * @author karel_evzen
// */
//public class Plan {
//    private final Queue<Action> actions;
//
//    public Plan(Queue<Action> actions) {
//        this.actions = actions;
//    }
//
//    public Plan() {
//        this.actions = new ConcurrentLinkedQueue<Action>();
//    }
//
//    public Plan(Action... actions) {
//        this();
//        for(Action a : actions)
//            this.actions.add(a);
//    }
//
//    public Queue<Action> getActions() {
//        return actions;
//    }
//
//    public String toString() {
//        String s = "Plan [";
//        for(Action a : this.actions) {
//            s += a.toString() + ", ";
//        }
//        s = s.substring(0, s.length() -2);
//        s += "]";
//        return s;
//    }
//}
