package melmac.simulator.bodies;

import melmac.core.world.Point;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Shape;

public abstract class ObjectBase extends Body
{

    protected ObjectBase(String name, Shape shape, float m)
    {
        super(name, shape, m);
    }

    public Point getPositionPoint()
    {
        return new Point((int) getPosition().getX(), (int) getPosition().getY());
    }

    public Point getVelocityPoint()
    {
        return new Point((int)getVelocity().getX(), (int)getVelocity().getY());
    }
}
