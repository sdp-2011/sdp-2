package melmac.simulator.bodies;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import melmac.core.utils.Angle;
import melmac.core.world.Point;
import melmac.simulator.PlayerColour;
import melmac.simulator.graphics.Renderable;
import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.shapes.Box;

public final class Robot extends ObjectBase implements Renderable
{

    public static final float WIDTH = 18f * Pitch.PIXELS_PER_CM;
    public static final float LENGTH = 20f * Pitch.PIXELS_PER_CM;
    public static final float TEE_THICKNESS = 3f * Pitch.PIXELS_PER_CM;
    public static final float TEE_WIDTH = 9.4f * Pitch.PIXELS_PER_CM;
    public static final float TEE_LENGTH = 11f * Pitch.PIXELS_PER_CM;
    public static final int MASS = 100;
    private static final Stroke thickLine = new BasicStroke(2f);
    private final PlayerColour playerColour;
    private float maxMovementSpeed = 100; //cm per sec
    private float maxRotationSpeed = 150; //degrees per sec

    public Robot(PlayerColour playerColour, float centreX, float centreY, float rotation)
    {
        super(playerColour + " robot", new Box(WIDTH, LENGTH), MASS);
        this.playerColour = playerColour;
        setPosition(centreX + Wall.THICKNESS, centreY + Wall.THICKNESS);
        setRotation(rotation);
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
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setStroke(thickLine);
        graphics2D.setColor(Color.BLACK);
        graphics2D.draw(rectangle);
        graphics2D.setStroke(oldStroke);

        graphics2D.translate(position.getX(), position.getY());
        graphics2D.rotate(rotation);
        graphics2D.setColor(playerColour == PlayerColour.Blue ? Color.BLUE : Color.YELLOW);
        graphics2D.fill(new Rectangle2D.Float(-TEE_THICKNESS / 2f, -TEE_LENGTH + TEE_THICKNESS, TEE_THICKNESS, TEE_LENGTH - TEE_THICKNESS));
        graphics2D.fill(new Rectangle2D.Float(-TEE_WIDTH / 2f, 0, TEE_WIDTH, TEE_THICKNESS));
        graphics2D.setColor(Color.BLACK);
        float diameter = 1.1f * TEE_THICKNESS;
        graphics2D.fill(new Ellipse2D.Float(-diameter / 2f, 1.5f * TEE_THICKNESS, diameter, diameter));
        graphics2D.rotate(-rotation);
        graphics2D.translate(-position.getX(), -position.getY());
    }

    public float getMaxMovementSpeed()
    {
        return maxMovementSpeed;
    }

    public float getMaxRotationSpeed()
    {
        return maxRotationSpeed;
    }

    public PlayerColour getPlayerColour()
    {
        return playerColour;
    }

    public Point getDirectionPoint()
    {
        double rotation = getRotation();
        int x = (int) (10 * Math.cos(rotation - Math.PI / 2d));
        int y = (int) (10 * Math.sin(rotation - Math.PI / 2d));
        return new Point(x, y);
    }

    public double getRelativeAngleTo(Point point)
    {
        Point directionToTarget = getPositionPoint().getDirectionTo(point);
        double angleToTarget = Angle.getAbsoluteAngle(directionToTarget);
        double selfAngle = Angle.getAbsoluteAngle(getDirectionPoint());
        return angleToTarget - selfAngle;
    }
}
