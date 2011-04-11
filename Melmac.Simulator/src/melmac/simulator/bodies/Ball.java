package melmac.simulator.bodies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import melmac.simulator.graphics.Renderable;
import net.phys2d.math.ROVector2f;
import net.phys2d.raw.shapes.Circle;

public final class Ball extends ObjectBase implements Renderable
{

    private static final float RADIUS = 2f * Pitch.PIXELS_PER_CM;
    private static final int MASS = 1;

    public Ball(float centreX, float centreY)
    {
        super("Ball", new Circle(RADIUS), MASS);
        setPosition(centreX + Wall.THICKNESS, centreY + Wall.THICKNESS);
        setGravityEffected(false);
        setDamping(0.008f);
        setRestitution(0.5f);
        setFriction(0.1f);
    }

    @Override
    public void render(Graphics2D graphics2D)
    {
        float radius = ((Circle) getShape()).getRadius();
        ROVector2f position = super.getPosition();
        graphics2D.setColor(Color.RED);
        graphics2D.fill(new Ellipse2D.Float(position.getX() - RADIUS, position.getY() - RADIUS, radius * 2f, radius * 2f));
    }
}