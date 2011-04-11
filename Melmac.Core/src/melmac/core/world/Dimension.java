package melmac.core.world;

public final class Dimension
{

    private final int width;
    private final int height;

    public Dimension(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }
}
