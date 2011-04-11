package melmac.slaveblock.comms;

import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;
import melmac.core.comms.LightReceiver;
import melmac.core.logging.LogMessages;
import melmac.core.logging.Logger;

public final class NxtLightReceiver extends LightReceiver implements SensorListener
{
    private static final int LIGHT_ON_LEVEL = 398;
    private static final int LIGHT_LEVEL_MARGIN = 50;

    public NxtLightReceiver(Logger logger, LogMessages logMessages, NxtCommunications nxtCommunications)
    {
        super(logger, logMessages, LIGHT_ON_LEVEL, LIGHT_LEVEL_MARGIN, nxtCommunications);
    }

    public void stateChanged(Sensor sensor, int oldValue, int newValue)
    {
        stateChanged(oldValue, newValue);
    }
}
