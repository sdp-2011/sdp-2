package melmac.slaveblock.comms;

import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorConstants;
import melmac.core.comms.LightReceiver;
import melmac.core.comms.SyncMessageProcessor;
import melmac.core.comms.SyncMessageSender;
import melmac.core.comms.MessageProcessor;
import melmac.core.comms.MessageSender;
import melmac.core.comms.LightCommunications;
import melmac.core.comms.MessageType;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class NxtCommunications extends LightCommunications
{
    private final NxtLightReceiver nxtLightReceiver;
    private final Sensor sensor;
    private final MessageProcessor messageProcessor;
    private final MessageSender messageSender;

    public NxtCommunications(Logger logger, Sensor sensor)
    {
        super(logger, LogMessages.NxtCommunications, MessageType.NxtToRcx_Acknowledgement, null, true);
        this.nxtLightReceiver = new NxtLightReceiver(logger, LogMessages.NxtCommunications, this);
        this.sensor = sensor;
        this.messageProcessor =
        new SyncMessageProcessor(logger, LogMessages.NxtCommunications, this, MessageType.RcxToNxt_Acknowledgement,
                                 MessageType.NxtToRcxCount);
        this.messageSender = new SyncMessageSender(logger, LogMessages.NxtCommunications, this);
        sensor.setTypeAndMode(SensorConstants.SENSOR_TYPE_LIGHT, SensorConstants.SENSOR_MODE_RAW);
        sensor.addSensorListener(nxtLightReceiver);
    }

    // NOTE: @Override not supported on RCX
    protected void turnLightOn()
    {
        sensor.activate();
    }

    // NOTE: @Override not supported on RCX
    protected void turnLightOff()
    {
        sensor.passivate();
    }

    // NOTE: @Override not supported on RCX
    protected MessageProcessor getMessageProcessor()
    {
        return messageProcessor;
    }

    // NOTE: @Override not supported on RCX
    protected MessageSender getMessageSender()
    {
        return messageSender;
    }

    // NOTE: @Override not supported on RCX
    protected LightReceiver getLightReceiver()
    {
        return nxtLightReceiver;
    }
}
