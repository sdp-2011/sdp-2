/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Move;
import melmac.core.utils.Path;
import melmac.core.world.BallInfo;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;


/**
 *
 * @author s0830457
 */
public final class BlockOpponent implements Strategy
{

    @Override
    public double getUtility(WorldState state) {
        
        //if the ball is not in opponent's possession there is no need to block the opponent.
        if(!Path.isBallInOppPossession(state)) {
            return 0;
        }

        double utility = 0.79;
        /*
        double utility = 0.50;
        double maxDist = Distance.euclidean(new Point(0,0), new Point(state.getVisionInfo().getPitchInfo().getDimension().getWidth(), state.getVisionInfo().getPitchInfo().getDimension().getHeight()));
        // the closer the opponent is to our goal - the higher the utility gets
        utility += (1 - state.getDistanceOppToGoalCenter()/maxDist)*0.5;
        */
        return utility;
    }

    @Override
    public Action getNextAction(WorldState state) {
        Point self = state.getSelf().getPosition();
        Point ball = state.getBall().getPosition();
        Point ownGoal = state.getOwnGoalCenter();

        /*
         * If our robot is not behind the ball, then go first to a point
         * behind the ball, while avoiding the opponent.
         */
        if (!Path.isBetween(ownGoal.getX(), ball.getX(), self.getX()))
        {
            Point target = Path.getPathAroundOpponent(state);
            return Move.To(target, state.getSelf(), state.getMaxSpeed());
        }

        /*
         * If our robot is already behind the ball, then go to a point
         * in front of the opponent to prevent him from scoring.
         */
        Point opp =  state.getOpponent().getPosition();


        // offsetX is always the size of the ball + the size of the robot
        int offsetX = RobotInfo.ROBOT_SIZE + BallInfo.DIAMETER;
        if (state.isTargetRight())
        {
            offsetX *= -1;
        }
        /* offsetY depends on position of the opponent on the Y axis.
         *  if opponent is at the center, there should be 0 offset
         *  the closer the opponent is to either the top or the bottom, the larger the offset gets
         */
        double yOffsetRatio = ((double)RobotInfo.ROBOT_SIZE/2)/(double)ownGoal.getY();
        double offsetY = (ownGoal.getY() - opp.getY())*yOffsetRatio;

        Point offset = opp.translate(offsetX, (int)offsetY);
        return Move.To(offset, state.getSelf(), state.getMaxSpeed());

    }

}
