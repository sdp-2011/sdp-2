package melmac.slaveblock;

import josx.platform.rcx.Button;
import josx.platform.rcx.ButtonListener;
import josx.platform.rcx.LCD;
import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorConstants;
import josx.platform.rcx.SensorListener;

public final class LightTest
{

    private LightTest()
    {
    }

    private static final class LightSensorListener implements SensorListener
    {

        public void stateChanged(Sensor sensor, int oldValue, int newValue)
        {
            LCD.showNumber(newValue);
        }
    }

    private static final class ControlButtonListener implements ButtonListener
    {

        public void buttonPressed(Button button)
        {
            Sensor.S1.activate();
        }

        public void buttonReleased(Button button)
        {
            Sensor.S1.passivate();
        }
    }

    public static void main(String[] args) throws Exception
    {
        Sensor.S1.setTypeAndMode(SensorConstants.SENSOR_TYPE_LIGHT, SensorConstants.SENSOR_MODE_RAW);
        Sensor.S1.addSensorListener(new LightSensorListener());
        Button.VIEW.addButtonListener(new ControlButtonListener());

        while (true)
        {
            Thread.sleep(10);
        }
    }
}
