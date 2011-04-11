/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package melmac.app.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import melmac.core.strategies.*;

/**
 *
 * @author Austin
 */
public class StrategyHashMap extends HashMap<Object, Object>
{

    @Override
    public void clear()
    {
        super.clear();
    }

    @Override
    public Object clone()
    {
        return super.clone();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return super.containsValue(value);
    }

    @Override
    public Set<Entry<Object, Object>> entrySet()
    {
        return super.entrySet();
    }

    @Override
    public Object get(Object key)
    {
        return super.get(key);
    }

    @Override
    public boolean isEmpty()
    {
        return super.isEmpty();
    }

    @Override
    public Set<Object> keySet()
    {
        return super.keySet();
    }

    @Override
    public Object put(Object key, Object value)
    {
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends Object, ? extends Object> m)
    {
        super.putAll(m);
    }

    @Override
    public Object remove(Object key)
    {
        return super.remove(key);
    }

    @Override
    public int size()
    {
        return super.size();
    }

    @Override
    public Collection<Object> values()
    {
        return super.values();
    }

    public void setStrategysAndKeys()
    {
        put("AutoSelect", null);
        put("MoveStraightToBall", new MoveStraightToBall());
        put("FaceBall", new FaceBall());
        put("AimToShoot", new AimToShoot());
        put("Collision", new CollisionStrategy());
        put("DoNothing", new DoNothing());
        put("BlockOpponent", new BlockOpponent());
        put("Defend Penalty", new DefendPenalty());
        put("DribbleStraightToOpponentGoal", new DribbleStraightToOpponentGoal());
        put("DribbleToAvoidOpponent", new DribbleToAvoidOpponent());
        put("DribbleToWall", new DribbleToWall());
        put("GoalKeeper", new Goalkeeper());
        put("KickDirect", new KickDirect());
        put("KickOffWall", new KickOffWall());
        put("MoveBehindBall", new MoveBehindBall());
        put("MoveStraightToBall", new MoveStraightToBall());
        put("MoveToAvoidOpponent", new MoveToAvoidOpponent());
        put("MoveToOwnGoal", new MoveToOwnGoal());
        put("TakePenalty", new TakePenalty());
        put("Intercept ball", new InterceptBall());
        put("Stop", new Stop());

    }
}
