package melmac.masterblock.control;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import melmac.core.control.CollisionListenerBase;

public final class CollisionListener extends CollisionListenerBase implements SensorPortListener
{
    private static final int DOWN_VALUE_BOUNDARY = 1000;

    public CollisionListener(CollisionHandler collisionHandler, int sensorId)
    {
        super(collisionHandler, sensorId, DOWN_VALUE_BOUNDARY);
    }

    public void stateChanged(SensorPort sensorPort, int oldValue, int newValue)
    {
        super.stateChanged(oldValue, newValue);
    }
}
