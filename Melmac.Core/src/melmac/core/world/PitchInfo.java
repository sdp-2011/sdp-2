package melmac.core.world;

public final class PitchInfo
{
    private final Dimension dimension;
    private final int leftGoalTop;
    private final int leftGoalBottom;
    private final int rightGoalTop;
    private final int rightGoalBottom;

    public PitchInfo(Dimension dimension, int leftGoalTop, int leftGoalBottom, int rightGoalTop, int rightGoalBottom)
    {
        this.dimension = dimension;
        this.leftGoalTop = leftGoalTop;
        this.leftGoalBottom = leftGoalBottom;
        this.rightGoalTop = rightGoalTop;
        this.rightGoalBottom = rightGoalBottom;
    }

    public Dimension getDimension()
    {
        return dimension;
    }

    public int getLeftGoalBottom()
    {
        return leftGoalBottom;
    }

    public int getLeftGoalTop()
    {
        return leftGoalTop;
    }

    public int getRightGoalBottom()
    {
        return rightGoalBottom;
    }

    public int getRightGoalTop()
    {
        return rightGoalTop;
    }

    @Override
    public String toString()
    {
        return "PitchInfo{" + "dimension=" + dimension + "leftGoalTop=" + leftGoalTop + "leftGoalBottom="
               + leftGoalBottom + "rightGoalTop=" + rightGoalTop + "rightGoalBottom=" + rightGoalBottom + '}';
    }
}
