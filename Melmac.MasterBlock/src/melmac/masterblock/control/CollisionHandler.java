package melmac.masterblock.control;

import melmac.core.comms.MessageType;
import lejos.nxt.SensorPort;
import melmac.core.comms.Communications;
import melmac.core.control.CollisionHandlerBase;
import melmac.core.logging.Logger;

public final class CollisionHandler extends CollisionHandlerBase
{
    private final Controller controller;

    public CollisionHandler(Logger logger, SensorPort[] sensorPorts, Controller controller,
                            Communications communications)
    {
        super(logger, communications, MessageType.NxtToJava_Collision);
        this.controller = controller;

        for (int index = 0; index < sensorPorts.length; index++)
        {
            sensorPorts[index].addSensorPortListener(new CollisionListener(this, index + 2));
        }
    }

    @Override
    public void handleCollision(int sensorId)
    {
        controller.stop();
        super.handleCollision(sensorId);
    }
}
