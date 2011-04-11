package melmac.core.interfaces;

public interface Controller
{

    public void move(int angle, int power);

    public void spin(int angle, int power);

    public void stop();

    public void kick();

    public void curvedMove(int directionAngle, int Xdistance, int Ydistance, int spinAngle, int power);
}
