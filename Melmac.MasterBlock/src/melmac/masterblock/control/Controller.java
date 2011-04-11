package melmac.masterblock.control;

import lejos.nxt.Motor;

public final class Controller
{
    private static final double WHEEL_DIAMETER = 62;
    private static final double WHEEL_SEPARATION = 52.5;
    private static final double WHEEL_ANGLE = Math.toRadians(60);
    private final Object lock = new Object();
    private final Motor leftWheelMotor;
    private final Motor rightWheelMotor;
    private final Motor backWheelMotor;

    public Controller() throws InterruptedException
    {
        leftWheelMotor = Motor.A;
        rightWheelMotor = Motor.B;
        backWheelMotor = Motor.C;
    }

    private void wheel(int power, Motor motor) throws Exception
    {
        if (power == 0)
        {
            motor.stop();
        }
        else
        {
            motor.setSpeed(power * 5);

            if (power < 0)
            {
                motor.backward();
            }
            else
            {
                motor.forward();
            }
        }
    }

    public void stop()
    {
        synchronized (lock)
        {
            leftWheelMotor.stop();
            rightWheelMotor.stop();
            backWheelMotor.stop();
        }
    }

    public void move(int angle, int power) throws Exception
    {
        synchronized (lock)
        {
            double radians = Math.toRadians(((360 - angle) + 90.0) % 360);
            double leftWheelPower = (Math.cos(radians) * Math.cos(WHEEL_ANGLE) + Math.sin(radians) * Math.sin(
                                     WHEEL_ANGLE)) * power;
            double rightWheelPower = (Math.cos(radians) * Math.cos(WHEEL_ANGLE) + Math.sin(radians) * -Math.sin(
                                      WHEEL_ANGLE)) * power;
            double backWheelPower = -Math.cos(radians) * power;

            wheel((int) leftWheelPower, leftWheelMotor);
            wheel((int) rightWheelPower, rightWheelMotor);
            wheel((int) backWheelPower, backWheelMotor);
        }
    }

    public void spin(int angle, int power) throws Exception
    {
        synchronized (lock)
        {
            leftWheelMotor.setPower(power);
            rightWheelMotor.setPower(power);
            backWheelMotor.setPower(power);
            int wheelAngle = (int) Math.floor(angle * 2 * WHEEL_SEPARATION / WHEEL_DIAMETER);
            leftWheelMotor.rotate(wheelAngle, true);
            rightWheelMotor.rotate(wheelAngle, true);
            backWheelMotor.rotate(wheelAngle);
        }
    }

    public void curvedMove(int directionAngle, int Xdistance, int Ydistance, int spinAngle, int power) throws Exception
    {
        double tTime;

        synchronized (lock)
        {
            //The algo will work only if the ball is placed 45 degrees from the robot and the back wheel of the robot should be parallel to the x axis
            //trying to achieve a way for all the angles.
            //The distance should be in form of (x,y). Distance in x and distance in y

            //Parameters needed
            //Xdistance, Ydistance (x,y)
            //angle from origin to orientation atan (y/x) of the orientation (y/x) vector
            //angle from centre of the robot to the ball b-a vector angle using atan(y/x) theta
            //inorder to find how much rotation is needed need to subtract from the following: 180-theta+angle of centre of the robot and ball
            double pixelTOcm = 0.3585882353;
            double Xdistp = Xdistance;
            double Ydistp = Ydistance;
            double Xdist = Xdistp * pixelTOcm;
            double Ydist = Ydistp * pixelTOcm;
            //TODO: Change the pixels to cm

//        int defaultAngle = 60;
//        double defaultAngle1 = Math.toRadians(defaultAngle);
            //double  length = 6.5;
            double radius = 3.2;
            double differenceL = 6.5;
            double spinAngle1 = Math.toRadians(spinAngle);
            double theta = Math.toRadians(directionAngle);

            double W2d1 = Math.toRadians(120);
            double W3d1 = Math.toRadians(240);

            double back = Math.round((float) (-Math.sin(theta) * Xdist) + (Math.cos(theta) * Ydist) + (spinAngle1
                                                                                                       * differenceL));
            double right = Math.round((float) (-Math.sin(theta + W2d1) * Xdist) + (Math.cos(theta + W2d1) * Ydist)
                                      + (spinAngle1 * differenceL));
            double left = Math.round((float) (-Math.sin(theta + W3d1) * Xdist) + (Math.cos(theta + W3d1) * Ydist)
                                     + (spinAngle1 * differenceL));
            double backwheel = back * 5;
            tTime = (180 * back) / (Math.PI * radius * backwheel);

            wheel((int) left, leftWheelMotor);
            wheel((int) right, rightWheelMotor);
            wheel((int) back, backWheelMotor);

        }

        Thread.sleep((long) tTime * 1000);

        stop();
    }
}
