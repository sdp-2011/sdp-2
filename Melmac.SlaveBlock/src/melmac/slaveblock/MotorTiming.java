package melmac.slaveblock;

import josx.platform.rcx.Button;
import josx.platform.rcx.ButtonListener;
import josx.platform.rcx.LCD;
import josx.platform.rcx.Motor;
import josx.platform.rcx.Sound;

public final class MotorTiming
{

    private MotorTiming()
    {
    }
    private static final Object lock = new Object();
    private static int power = 7;
    private static int time = 0;

    private static final class ViewButtonListener implements ButtonListener
    {

        public void buttonPressed(Button button)
        {
            synchronized (lock)
            {
                if (power == 7)
                {
                    power = 0;
                }
                else
                {
                    power++;
                }

                LCD.showNumber(power);
            }
        }

        public void buttonReleased(Button button)
        {
        }
    }

    private static final class ProgramButtonListener implements ButtonListener
    {

        public void buttonPressed(Button button)
        {
            synchronized (lock)
            {
                if (time == 1000)
                {
                    time = 0;
                }
                else
                {
                    time += 25;
                }

                LCD.showNumber(time);
            }
        }

        public void buttonReleased(Button button)
        {
        }
    }

    private static final class RunButtonListener implements ButtonListener
    {

        public void buttonPressed(Button button)
        {
            try
            {
                int thisTime;
                int thisPower;

                synchronized (lock)
                {
                    thisTime = time;
                    thisPower = power;
                }

                Motor.A.setPower(thisPower);
                Motor.B.setPower(thisPower);

                Motor.A.forward();
                Motor.B.backward();

                Thread.sleep(thisTime);

                Motor.A.reverseDirection();
                Motor.B.reverseDirection();

                Thread.sleep(thisTime + 25);

                Motor.A.stop();
                Motor.B.stop();
            }
            catch (InterruptedException ex)
            {
                Sound.beepSequence();
            }
        }

        public void buttonReleased(Button button)
        {
        }
    }

    public static void main(String[] args) throws Exception
    {
        Button.VIEW.addButtonListener(new ViewButtonListener());
        Button.PRGM.addButtonListener(new ProgramButtonListener());
        Button.RUN.addButtonListener(new RunButtonListener());

        while (true)
        {
            Thread.sleep(10);
        }
    }
}
