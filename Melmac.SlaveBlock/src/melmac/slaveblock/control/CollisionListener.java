package melmac.slaveblock.control;

import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;
import melmac.core.control.CollisionListenerBase;

public final class CollisionListener extends CollisionListenerBase implements SensorListener
{
    private static final int DOWN_VALUE_BOUNDARY = 1000;

    public CollisionListener(CollisionHandler collisionHandler, int sensorId)
    {
        super(collisionHandler, sensorId, DOWN_VALUE_BOUNDARY);
    }

    public void stateChanged(Sensor sensor, int oldValue, int newValue)
    {
        super.stateChanged(oldValue, newValue);
    }
}
