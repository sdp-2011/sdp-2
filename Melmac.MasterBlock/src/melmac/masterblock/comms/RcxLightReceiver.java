package melmac.masterblock.comms;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import melmac.core.comms.LightReceiver;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class RcxLightReceiver extends LightReceiver implements SensorPortListener
{
    private static final int LIGHT_ON_LEVEL = 284;
    private static final int LIGHT_LEVEL_MARGIN = 50;

    public RcxLightReceiver(Logger logger, LogMessages logMessages, RcxCommunications rcxCommunications)
    {
        super(logger, logMessages, LIGHT_ON_LEVEL, LIGHT_LEVEL_MARGIN, rcxCommunications);
    }

    @Override
    public void stateChanged(SensorPort sensorPort, int oldValue, int newValue)
    {
        stateChanged(oldValue, newValue);
    }
}
