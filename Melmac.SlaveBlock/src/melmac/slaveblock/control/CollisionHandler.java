package melmac.slaveblock.control;

import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorConstants;
import melmac.core.comms.Communications;
import melmac.core.comms.MessageType;
import melmac.core.control.CollisionHandlerBase;
import melmac.core.logging.Logger;

public final class CollisionHandler extends CollisionHandlerBase
{
    public CollisionHandler(Logger logger, Sensor[] sensors, Communications communications)
    {
        super(logger, communications, MessageType.RcxToNxt_Collision);

        for (int index = 0; index < sensors.length; index++)
        {
            sensors[index].setTypeAndMode(SensorConstants.SENSOR_TYPE_TOUCH, SensorConstants.SENSOR_MODE_RAW);
            sensors[index].addSensorListener(new CollisionListener(this, index));
        }
    }
}
