package melmac.masterblock;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

public final class LightTest
{
    private LightTest()
    {
    }

    private static final class LightSensorPortListener implements SensorPortListener
    {
        public void stateChanged(SensorPort sensorPort, int oldValue, int newValue)
        {
            LCD.drawInt(newValue, 0, 0);
            LCD.drawInt(lightSensor.readValue(), 0, 1);
            LCD.drawInt(lightSensor.readNormalizedValue(), 0, 2);
        }
    }

    private static final class ControlButtonListener implements ButtonListener
    {
        @Override
        public void buttonPressed(Button button)
        {
            lightSensor.setFloodlight(true);
        }

        @Override
        public void buttonReleased(Button button)
        {
            lightSensor.setFloodlight(false);
        }
    }

    private static LightSensor lightSensor;

    public static void main(String[] args) throws Exception
    {
        lightSensor = new LightSensor(SensorPort.S1, false);
        SensorPort.S1.addSensorPortListener(new LightSensorPortListener());
        Button.ENTER.addButtonListener(new ControlButtonListener());
        Thread.sleep(1000);
        Button.ESCAPE.waitForPress();
        lightSensor.calibrateLow();
        LCD.drawString("Cal LOW done", 0, 3);
        lightSensor.setFloodlight(true);
        Button.ESCAPE.waitForPress();
        lightSensor.calibrateHigh();
        LCD.drawString("Cal HIGH done", 0, 4);

        while (true)
        {
            Thread.sleep(10);
        }
    }
}
