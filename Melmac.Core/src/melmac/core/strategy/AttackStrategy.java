//package melmac.core.strategy;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import melmac.core.world.Point;
//import melmac.core.world.WorldState;
//import melmac.core.utils.Distance;
//
//public class AttackStrategy implements IStrategy {
//
//    @Override
//    public double getUtility(WorldState state)
//    {
//        //calculate the distance from the robot to the ball to see if Attacking Strategy is a meaningful option in the current state.
//
//
//    	// get positions of the robot and the ball
//    	Point positionSelf = state.getSelf().getPose().getPosition();
//    	Point positionBall = state.getBall().getPosition();
//
//        // get the dimensions(x_max,y_max) of the pitch and create two points (0,0) and (x_max,y_max)
//        int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
//        int pitchHeight = state.getVisionInfo().getPitchInfo().getDimension().getHeight();
//        Point startOfPitch = new Point(0,0);
//        Point endOfPitch = new Point(pitchWidth, pitchHeight);
//
//        // calculate distance to Ball and maximum possible distance (diagonal of the pitch)
//        double distToBall = Distance.squaredEuclidean(positionSelf, positionBall);
//        double maxDist = Distance.squaredEuclidean(startOfPitch, endOfPitch);
//
//        //calculate utilityValue (range from 0 to 1) where 1 is when the robot is closest to the ball
//        double utilityValue = 1 - distToBall / maxDist;
//    	//double utilityValue = 0;
//
//        return 1;
//    }
//
//    @Override
//    public Plan plan(WorldState state, Plan currentPlan)
//    {
//        Logger.getAnonymousLogger().log(Level.INFO, "PitchInfo: " + state.getVisionInfo().getPitchInfo().toString());
//
//    	//OpponentAvoidance opponentAvoidance = new OpponentAvoidance(state);
//        //int stopRadiusSquared = 3600;
//        //Point targetGoal = new Point(0, 170);//FIX it currently shows always the left goal
//        int power = 100;
//
//
//    	Point opp = state.getOpponent().getPose().getPosition();
//        Point self = state.getSelf().getPose().getPosition();
//        Point directionSelf = state.getSelf().getPose().getDirectionVector();
//        Point ball = state.getBall().getPosition();
//        int pitchWidth = state.getVisionInfo().getPitchInfo().getDimension().getWidth();
//        int pitchHeight = state.getVisionInfo().getPitchInfo().getDimension().getHeight();
//        Point targetGoal = state.getTargetGoalCenter();
//        Point ownGoal = state.getOwnGoalCenter();
//
//        Plan plan = new Plan();
//
//        double oppToBall = Distance.squaredEuclidean(ball, opp);
//        double selfToBall = Distance.squaredEuclidean(ball, self);
//        double oppToGoal = Distance.squaredEuclidean(targetGoal, opp);
//        double selfToGoal = Distance.squaredEuclidean(targetGoal, self);
//        if (oppToBall < selfToBall){
//
//            Point point = null;
//            Point point2 = null;
//
//
//            if (Math.abs(ball.getX() - self.getX()) < Math.abs(ball.getY() - self.getY()) &&
//                    ((ball.getY() < opp.getY() && opp.getY() < self.getY())
//                    || (self.getY() < opp.getY() && opp.getY() < ball.getY())))
//            {
//                // Find minimum X between opp and walls
//                double leftDistance = Distance.squaredEuclidean(opp, new Point(0, opp.getY()));
//                double rightDistance = Distance.squaredEuclidean(opp, new Point(pitchWidth, opp.getY()));
//
//                if (leftDistance < rightDistance)
//                {
//                    System.out.println("Going right");
//                    if (ball.getY() < opp.getY() && opp.getY() < self.getY())
//                    {
//                    	// objects are arranged from top to bottom: ball, opponent, self
//                        point = new Point((opp.getX() + pitchWidth) / 2, Math.min(opp.getY() + 75, pitchHeight - 40));
//                        point2 = new Point((opp.getX() + pitchWidth) / 2, Math.max(opp.getY() - 75, 40));
//                    } else {
//                    	// objects are arranged from top to bottom: self, opponent, ball
//                        point = new Point((opp.getX() + pitchWidth) / 2, Math.max(opp.getY() - 75, 40));
//                        point2 = new Point((opp.getX() + pitchWidth) / 2, Math.min(opp.getY() + 75, pitchHeight - 40));
//                    }
//                }
//                else
//                {
//                    System.out.println("Going left");
//                    if (ball.getY() < opp.getY() && opp.getY() < self.getY())
//                    {
//                    	// objects are arranged from top to bottom: ball, opponent, self
//                    	point = new Point(opp.getX() / 2, Math.min(opp.getY() + 75, pitchHeight - 40));
//                    	point2 = new Point(opp.getX() / 2, Math.max(opp.getY() - 75, 40));
//                    } else {
//                    	// objects are arranged from top to bottom: self, opponent, ball
//                    	point = new Point(opp.getX() / 2, Math.max(opp.getY() - 75, 40));
//                    	point2 = new Point(opp.getX() / 2, Math.min(opp.getY() + 75, pitchHeight - 40));
//                    }
//                }
//            }
//            else
//            {
//
//                if ((ball.getX() < opp.getX() && opp.getX() < self.getX())
//                        || (self.getX() < opp.getX() && opp.getX() < ball.getX()))
//                {
//                    // Find minimum Y between opp and walls
//                    double topDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), 0));
//                    double bottomDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), pitchHeight));
//
//                    if (topDistance > bottomDistance)
//                    {
//                        System.out.println("Going up");
//                        if (ball.getX() < opp.getX() && opp.getX() < self.getX())
//                        {
//                        	// objects are arranged from left to right: ball, opponent, self
//                        	point = new Point(Math.min(opp.getX() + 75, pitchWidth - 40), opp.getY() / 2);
//                            point2 = new Point(Math.max(opp.getX() - 75, 40), opp.getY() / 2);
//                        } else {
//                        	// objects are arranged from left to right: self, opponent, all
//                        	point = new Point(Math.max(opp.getX() - 75, 40), opp.getY() / 2);
//                            point2 = new Point(Math.min(opp.getX() + 75, pitchWidth - 40), opp.getY() / 2);
//                        }
//                    }
//                    else
//                    {
//                        System.out.println("Going down");
//                        if (ball.getX() < opp.getX() && opp.getX() < self.getX())
//                        {
//                        	// objects are arranged from left to right: ball, opponent, self
//                        	point = new Point(Math.min(opp.getX() + 75, pitchWidth - 40), (opp.getY() + pitchHeight) / 2);
//                            point2 = new Point(Math.max(opp.getX() - 75, 40), (opp.getY() + pitchHeight) / 2);
//                        } else {
//                        	// objects are arranged from left to right: self, opponent, ball
//                        	point = new Point(Math.max(opp.getX() - 75, 40), (opp.getY() + pitchHeight) / 2);
//                            point2 = new Point(Math.min(opp.getX() + 75, pitchWidth - 40), (opp.getY() + pitchHeight) / 2);
//                        }
//                    }
//                }
//            }
//
//            	if (point != null)
//            	{
//            		Action moveToPoint = new MoveToAction(point, power, false);
//            		plan.getActions().add(moveToPoint);
//            	}
//            	if (point2 != null){
//            		Action moveToPoint2 = new MoveToAction(point2, power, false);
//            		plan.getActions().add(moveToPoint2);
//            	}
//
//        }
//
//        System.out.println("Going to ball");
//        Point target;
//    	if (targetGoal.getX() < ball.getX())
//    	{
//    		int targetX = ball.getX() + 60;
//    		int targetY = ball.getY();
//    		target = new Point(targetX, targetY);
//
//    		Point point3 = null;
//            Point point4 = null;
//
//            if (self.getX() < ball.getX())
//            {
//                // Find minimum Y between ball and walls
//                double topDistance = Distance.squaredEuclidean(ball, new Point(ball.getX(), 0));
//                double bottomDistance = Distance.squaredEuclidean(ball, new Point(ball.getX(), pitchHeight));
//
//                if (topDistance > bottomDistance)
//                {
//                    System.out.println("Going up");
//                    // objects are arranged from left to right: ball, opponent, self
//                    point3 = new Point(Math.max(ball.getX() - 60, 40), ball.getY() - 60);
//                    point4 = new Point(Math.min(ball.getX() + 60, pitchWidth - 40), ball.getY() - 60);
//                }
//                else
//                {
//                    System.out.println("Going down");
//                    // objects are arranged from left to right: ball, opponent, self
//                    point3 = new Point(Math.max(ball.getX() - 60, 40), ball.getY() + 60);
//                    point4 = new Point(Math.min(ball.getX() + 60, pitchWidth - 40), ball.getY() + 60);
//                }
//            }
//            if (point3 != null)
//            {
//            	Action moveToPoint3 = new MoveToAction(point3, power, false);
//        		plan.getActions().add(moveToPoint3);
//            }
//            if (point4 != null){
//            	Action moveToPoint4 = new MoveToAction(point4, power, false);
//        		plan.getActions().add(moveToPoint4);
//            }
//
//            System.out.println("Going to target");
//
//            Action moveToTarget = new MoveToAction(target, power/3, true);
//    		plan.getActions().add(moveToTarget);
//
//
//
//
//
//    	} else {
//    		int targetX = ball.getX() - 60;
//    		int targetY = ball.getY();
//    		target = new Point(targetX, targetY);
//
//    		Point point3 = null;
//            Point point4 = null;
//
//            if (ball.getX() < self.getX())
//            {
//                // Find minimum Y between ball and walls
//                double topDistance = Distance.squaredEuclidean(ball, new Point(ball.getX(), 0));
//                double bottomDistance = Distance.squaredEuclidean(ball, new Point(ball.getX(), pitchHeight));
//
//                if (topDistance > bottomDistance)
//                {
//                    System.out.println("Going up");
//                    // objects are arranged from left to right: ball, opponent, self
//                    point3 = new Point(Math.min(ball.getX() + 60, pitchWidth - 40), ball.getY() - 60);
//                    point4 = new Point(Math.max(ball.getX() - 60, 40), ball.getY() - 60);
//                }
//                else
//                {
//                    System.out.println("Going down");
//                   // objects are arranged from left to right: ball, opponent, self
//                   point3 = new Point(Math.min(ball.getX() + 60, pitchWidth - 40), ball.getY() + 60);
//                   point4 = new Point(Math.max(ball.getX() - 60, 40), ball.getY() + 60);
//                }
//            }
//            if (point3 != null)
//            {
//            	Action moveToPoint3 = new MoveToAction(point3, power, false);
//        		plan.getActions().add(moveToPoint3);
//            }
//            if (point4 != null){
//            	Action moveToPoint4 = new MoveToAction(point4, power, false);
//        		plan.getActions().add(moveToPoint4);
//            }
//
//            System.out.println("Going to target");
//
//            Action moveToTarget = new MoveToAction(target, power/2, true);
//    		plan.getActions().add(moveToTarget);
//    	}
//    	System.out.println("Going to ball");
//
//    	Point approachPoint;
//    	if (target.getX() < ball.getX()){
//    		approachPoint = new Point(ball.getX() - 45, ball.getY());
//    	} else {
//    		approachPoint = new Point(ball.getX() + 45, ball.getY());
//    	}
//    	Action approachBall = new MoveToAction(approachPoint, 25, false);
//    	plan.getActions().add(approachBall);
//
//    	Action faceBall = new FaceAction(approachPoint, targetGoal);
//    	plan.getActions().add(faceBall);
//
//    	//Action moveToBall = new MoveToAction(ball, power, false);
//    	//plan.getActions().add(moveToBall);
//
//
//
//
//    	//avoid opponent with ball
//    	Point aPoint = null;
//        Point aPoint2 = null;
//        int aTargetX = 0;
//        int aTargetY = 0;
//        if (targetGoal.getX() < 10)
//        {
//        	aTargetX = Math.min(ball.getX()*5/6, pitchWidth/4);
//        	if (targetGoal.getY() >= ball.getY()){
//        		aTargetY = targetGoal.getY() - (targetGoal.getY() - ball.getY())/2;
//        	} else {
//        		aTargetY = targetGoal.getY() + (ball.getY() - targetGoal.getY())/2;
//        	}
//        } else {
//        	aTargetX = Math.max((ball.getX() + (targetGoal.getX() - ball.getX())/6) , pitchWidth*3/4);
//        	if (targetGoal.getY() >= ball.getY()){
//        		aTargetY = targetGoal.getY() - (targetGoal.getY() - ball.getY())/2;
//        	} else {
//        		aTargetY = targetGoal.getY() + (ball.getY() - targetGoal.getY())/2;
//        	}
//        }
//        Point aTarget = new Point(aTargetX, aTargetY);
//
//        if ((aTarget.getX() < opp.getX() && opp.getX() < target.getX())
//                || (target.getX() < opp.getX() && opp.getX() < aTarget.getX()))
//        {
//            // Find minimum Y between opp and walls
//            double topDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), 0));
//            double bottomDistance = Distance.squaredEuclidean(opp, new Point(opp.getX(), pitchHeight));
//
//            if (topDistance > bottomDistance)
//            {
//                System.out.println("Going up");
//                if (aTarget.getX() < opp.getX() && opp.getX() < target.getX())
//                {
//                 	// objects are arranged from left to right: ball, opponent, self
//                   	aPoint = new Point(Math.min(opp.getX() + 75, pitchWidth - 40), opp.getY() / 2);
//                    aPoint2 = new Point(Math.max(opp.getX() - 75, 40), opp.getY() / 2);
//                } else {
//                   	// objects are arranged from left to right: self, opponent, all
//                   	aPoint = new Point(Math.max(opp.getX() - 75, 40), opp.getY() / 2);
//                    aPoint2 = new Point(Math.min(opp.getX() + 75, pitchWidth - 40), opp.getY() / 2);
//                }
//            }
//            else
//            {
//                System.out.println("Going down");
//                if (aTarget.getX() < opp.getX() && opp.getX() < target.getX())
//                {
//                  	// objects are arranged from left to right: ball, opponent, self
//                   	aPoint = new Point(Math.min(opp.getX() + 75, pitchWidth - 40), (opp.getY() + pitchHeight) / 2);
//                    aPoint2 = new Point(Math.max(opp.getX() - 75, 40), (opp.getY() + pitchHeight) / 2);
//                } else {
//                   	// objects are arranged from left to right: self, opponent, ball
//                   	aPoint = new Point(Math.max(opp.getX() - 75, 40), (opp.getY() + pitchHeight) / 2);
//                    aPoint2 = new Point(Math.min(opp.getX() + 75, pitchWidth - 40), (opp.getY() + pitchHeight) / 2);
//                }
//            }
//        }
//
//        if (aPoint != null)
//        {
//        	Action moveToAPoint = new MoveToAction(aPoint, power, false);
//    		plan.getActions().add(moveToAPoint);
//        }
//        if (aPoint2 != null)
//        {
//        	Action moveToAPoint2 = new MoveToAction(aPoint2, power, false);
//    		plan.getActions().add(moveToAPoint2);
//        }
//
//        System.out.println("Going to aTarget");
////        Action moveToATarget = new MoveToAction(aTarget, power, false);
////		plan.getActions().add(moveToATarget);
////		Action kickAction = new KickAction();
////		plan.getActions().add(kickAction);
////		Action stopAction = new StopAction();
////		plan.getActions().add(stopAction);
//
//
//        Action a = new DribbleAndKickAction(aTarget);
//    	plan.getActions().add(a);
//
//
//		return plan;
//
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
//
//    	Action[] actions =  plan.getActions().toArray(new Action[0]);
//        for (int i = 0; i < actions.length; i++){
//        	if (actions[i] instanceof FaceAction){
//        		return true;
//        	}
//        }
//        Point robot = state.getSelf().getPose().getPosition();
//        Point ball = state.getBall().getPosition();
//        Point targetGoal = state.getTargetGoalCenter();
//        if ((ball.getY() < robot.getY() - 45) || (ball.getY() > robot.getY() + 45)){
//        	//unnecessary logic starting here, if position of ball is correct remove everything up to return false;
//        	if ((targetGoal.getX() < robot.getX() && robot.getX() < ball.getX()) || (targetGoal.getX() > robot.getX() && robot.getX() > ball.getX())){
//        		if (Math.abs(robot.getX() - ball.getX()) > 100){
//        			return true;
//        			//this assumes that the position of the ball is wrong so it continues to move forward.
//        			//It is impossible for the ball to move about 150 pixels between two calls of the method.
//        		}
//        	}
//                Logger.getAnonymousLogger().log(Level.INFO, "Ball missed: " + robot.toString() + ", " + ball.toString());
//        	//return false;
//        }
//        //add if statement for case when ball is too much to the front (in front of the point at which we have to score)
//    	return true;
//    }
//}
