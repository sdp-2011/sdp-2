package melmac.simulator.bodies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import melmac.simulator.graphics.Renderable;
import net.phys2d.math.ROVector2f;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.shapes.Box;

public abstract class Wall extends StaticBody implements Renderable
{

    public static final int THICKNESS = 20;

    public static final class TopWall extends Wall
    {

        public TopWall()
        {
            super("Top", 0, 0, Pitch.INTERNAL_WIDTH + 2f * THICKNESS, THICKNESS);
        }
    }

    public static final class BottomWall extends Wall
    {

        public BottomWall()
        {
            super("Bottom", 0, Pitch.INTERNAL_HEIGHT + THICKNESS, Pitch.INTERNAL_WIDTH + 2f * THICKNESS, THICKNESS);
        }
    }

    public static final class BottomRightWall extends Wall
    {

        public BottomRightWall()
        {
            super("BottomRight", Pitch.INTERNAL_WIDTH / 2f + THICKNESS, Pitch.INTERNAL_HEIGHT + THICKNESS,
                  Pitch.INTERNAL_WIDTH / 2f + THICKNESS, THICKNESS);
        }
    }

    public static final class LeftTopWall extends Wall
    {

        public LeftTopWall()
        {
            super("LeftTop", 0, THICKNESS, THICKNESS, Pitch.GOAL_TOP);
        }
    }

    public static final class LeftBottomWall extends Wall
    {

        public LeftBottomWall()
        {
            super("LeftBottom", 0, Pitch.GOAL_BOTTOM + THICKNESS, THICKNESS,
                  Pitch.INTERNAL_HEIGHT - Pitch.GOAL_BOTTOM);
        }
    }

    public static final class RightTopWall extends Wall
    {

        public RightTopWall()
        {
            super("RightTop", Pitch.INTERNAL_WIDTH + THICKNESS, THICKNESS, THICKNESS, Pitch.GOAL_TOP);
        }
    }

    public static final class RightBottomWall extends Wall
    {

        public RightBottomWall()
        {
            super("RightBottom", Pitch.INTERNAL_WIDTH + THICKNESS, Pitch.GOAL_BOTTOM + THICKNESS, THICKNESS,
                  Pitch.INTERNAL_HEIGHT - Pitch.GOAL_BOTTOM);
        }
    }

    public static final class LeftGoal extends Wall
    {

        public LeftGoal()
        {
            super("LeftGoal", 0, Pitch.GOAL_TOP + THICKNESS, THICKNESS / 3f, Pitch.GOAL_BOTTOM - Pitch.GOAL_TOP);
        }
    }

    public static final class RightGoal extends Wall
    {

        public RightGoal()
        {
            super("RightGoal", Pitch.INTERNAL_WIDTH + 2f * THICKNESS - THICKNESS / 3f, Pitch.GOAL_TOP + THICKNESS, THICKNESS / 3f, Pitch.GOAL_BOTTOM - Pitch.GOAL_TOP);
        }
    }

    protected Wall(String position, float x, float y, float width, float height)
    {
        super(position + " wall", new Box(width, height));
        setPosition(x + width / 2f, y + height / 2f);
        setGravityEffected(false);
        setRotDamping(1f);
        setRestitution(0.9f);
        setFriction(10.0f);
    }

    @Override
    public void render(Graphics2D graphics2D)
    {
        ROVector2f position = getPosition();
        graphics2D.setColor(Color.BLACK);
        ROVector2f size = ((Box) this.getShape()).getSize();
        graphics2D.fill(new Rectangle2D.Float(position.getX() - size.getX() / 2f, position.getY() - size.getY() / 2f, size.getX(), size.getY()));
    }
}
