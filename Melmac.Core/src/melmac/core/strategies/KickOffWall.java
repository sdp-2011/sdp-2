/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package melmac.core.strategies;

import melmac.core.actions.Action;
import melmac.core.actions.Kick;
import melmac.core.actions.Spin;
import melmac.core.utils.Distance;
import melmac.core.utils.Path;
import melmac.core.world.Point;
import melmac.core.world.RobotInfo;
import melmac.core.world.WorldState;

/**
 *
 * @author s0830457
 */
public class KickOffWall implements Strategy {

    @Override
    public double getUtility(WorldState state) {
        if (state.isShotPossible())
        {
            return 0;
        }
        if (!state.isBallInPossession())
        {
            return 0;
        }

        double utility = 0.48;

        double maxDist = Distance.euclidean(new Point(0,0), new Point(state.getVisionInfo().getPitchInfo().getDimension().getWidth(), state.getVisionInfo().getPitchInfo().getDimension().getHeight()));
        // the closer we are to the opponent goal - the higher the utility gets
        utility += (1 - state.getDistanceSelfToGoalCenter()/maxDist)*0.5;

        return utility;

    }

    @Override
    public Action getNextAction(WorldState state) {
        /*
         * first select a target point where to shoot
         */
        RobotInfo opp = state.getOpponent();
        RobotInfo self = state.getOpponent();
        int pitchHeight = state.getPitch().getDimension().getHeight();
        Point targetGoal = state.getTargetGoalCenter();
        Point target;
        int targetY;
        int targetX;
        /*
         * set the point to be at the further wall from the opponent robot
         * and half the distance between us and the goal
         */
        if (opp.getPosition().getY() < pitchHeight/2)
        {
            targetY = pitchHeight;
        } else
        {
            targetY = 0;
        }
        if (state.isTargetRight())
        {
            targetX = self.getPosition().getX() + (targetGoal.getX() - self.getPosition().getX())/2;
        } else
        {
            targetX = self.getPosition().getX()/2;
        }
        target = new Point(targetX, targetY);
        /*
         * check if the opponent is also in the way to the target point
         */
        if (Path.isInAWay(self.getPosition(), target, opp.getPosition(), RobotInfo.ROBOT_SIZE))
        {
           /*
            * opponent is too near, aim nearer
            */
            target = new Point(opp.getPosition().getX(), targetY);
        }
        /*
         * check if we are already facing that way
         */
        double ang = self.getRelativeAngleTo(target);
        if (ang > 30)
        {
            /*
             * we are not facing that way, so spin to face the target point
             */
            return Spin.ToFace(target, self);
        }
        /*
         * we are facing that way, so kick!
         */
        return Kick.SINGLETON;
    }

}
