package melmac.simulator.bodies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import melmac.simulator.graphics.Renderable;
import net.phys2d.math.ROVector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Circle;

public final class RandomBall extends Body implements Renderable
{

    private final Color color;

    public RandomBall(Random random)
    {
        super("Ball", new Circle((random.nextFloat() * 10f + 1f) * Pitch.PIXELS_PER_CM), random.nextFloat());
        this.color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
        setPosition(random.nextFloat() * (Pitch.INTERNAL_WIDTH - 2) + Wall.THICKNESS + 1, random.nextFloat() * (Pitch.INTERNAL_HEIGHT - 2) + Wall.THICKNESS + 1);
        setForce(random.nextFloat() * 50f - 25f, random.nextFloat() * 50f - 25f);
        setGravityEffected(false);
        setRotDamping(1f);
        setRestitution(0.9f);
        setFriction(0.5f);
    }

    @Override
    public void render(Graphics2D graphics2D)
    {
        float radius = ((Circle) getShape()).getRadius();
        ROVector2f position = super.getPosition();
        graphics2D.setColor(color);
        graphics2D.fill(new Ellipse2D.Float(position.getX() - radius, position.getY() - radius, radius, radius));
    }
}
