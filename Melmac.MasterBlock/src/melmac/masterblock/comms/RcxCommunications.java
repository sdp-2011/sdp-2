package melmac.masterblock.comms;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import melmac.core.comms.AsyncMessageProcessor;
import melmac.core.comms.AsyncMessageSender;
import melmac.core.comms.ControllingLightCommunications;
import melmac.core.comms.LightReceiver;
import melmac.core.comms.MessageProcessor;
import melmac.core.comms.MessageSender;
import melmac.core.comms.MessageType;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class RcxCommunications extends ControllingLightCommunications
{
    private static final String threadNamePrefix = "RcxComms";
    private final RcxLightReceiver rcxLightReceiver;
    private final LightSensor lightSensor;
    private final MessageProcessor messageProcessor;
    private final MessageSender messageSender;

    public RcxCommunications(Logger logger, SensorPort sensorPort)
    {
        super(logger, LogMessages.RcxCommunications, MessageType.RcxToNxt_Acknowledgement, MessageType.NxtToRcx_Reset,
              threadNamePrefix, false);
        this.rcxLightReceiver = new RcxLightReceiver(logger, LogMessages.RcxCommunications, this);
        this.lightSensor = new LightSensor(sensorPort, false);
        this.messageProcessor = new AsyncMessageProcessor(logger, LogMessages.RcxCommunications, threadNamePrefix, this,
                                                          MessageType.NxtToRcx_Acknowledgement,
                                                          MessageType.RcxToNxtCount);
        this.messageSender = new AsyncMessageSender(logger, LogMessages.RcxCommunications, threadNamePrefix, this);
        sensorPort.addSensorPortListener(rcxLightReceiver);
    }

    @Override
    protected void turnLightOn()
    {
        lightSensor.setFloodlight(true);
    }

    @Override
    protected void turnLightOff()
    {
        lightSensor.setFloodlight(false);
    }

    @Override
    protected MessageProcessor getMessageProcessor()
    {
        return messageProcessor;
    }

    @Override
    protected MessageSender getMessageSender()
    {
        return messageSender;
    }

    @Override
    protected LightReceiver getLightReceiver()
    {
        return rcxLightReceiver;
    }
}
