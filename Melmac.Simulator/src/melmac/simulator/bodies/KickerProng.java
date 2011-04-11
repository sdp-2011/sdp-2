package melmac.simulator.bodies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import melmac.simulator.graphics.Renderable;
import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.shapes.Box;

public final class KickerProng extends ObjectBase implements Renderable
{
    public static final float WIDTH = 1f * Pitch.PIXELS_PER_CM;
    public static final float LENGTH = 3f * Pitch.PIXELS_PER_CM;

    public KickerProng(Robot robot, boolean isLeft, boolean facingRight)
    {
        super("Kicker prong", new Box(WIDTH, LENGTH), Robot.MASS);
        setPosition(
            robot.getPosition().getX() + (Robot.LENGTH / 2f + LENGTH / 2f) * (facingRight ? 1f : -1f),
            robot.getPosition().getY() + (Robot.WIDTH / 2f -WIDTH / 2f) * (isLeft ? -1f : 1f));
        setRotation(robot.getRotation());
        setGravityEffected(false);
        setRotDamping(15.0f);
        setDamping(6.0f);
        setRestitution(0.1f);
        setFriction(10.0f);
    }

    @Override
    public void render(Graphics2D graphics2D)
    {
        ROVector2f position = getPosition();
        float rotation = getRotation();
        Vector2f[] vertices = ((Box) getShape()).getPoints(position, rotation);
        GeneralPath rectangle = new GeneralPath();
        rectangle.moveTo(vertices[0].getX(), vertices[0].getY());

        for (int index = 1; index < vertices.length; index++)
        {
            rectangle.lineTo(vertices[index].getX(), vertices[index].getY());
        }

        rectangle.closePath();
        graphics2D.setColor(Color.BLACK);
        graphics2D.fill(rectangle);
    }
}
