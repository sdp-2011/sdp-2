package melmac.slaveblock.control;

import josx.platform.rcx.Motor;

public final class Controller
{

    private static final int KICK_DURATION = 175;
    private final Motor leftKickerMotor;
    private final Motor rightKickerMotor;

    public Controller(Motor leftKickerMotor, Motor rightKickerMotor)
    {
        this.leftKickerMotor = leftKickerMotor;
        this.rightKickerMotor = rightKickerMotor;
    }

    public synchronized void kick() throws Exception
    {
        leftKickerMotor.forward();
        rightKickerMotor.forward();
        Thread.sleep(KICK_DURATION);
        leftKickerMotor.backward();
        rightKickerMotor.backward();
        Thread.sleep(KICK_DURATION + 25);
        leftKickerMotor.stop();
        rightKickerMotor.stop();
    }
}
