package melmac.core.world;

public final class VisionInfo
{

    private final PitchInfo pitchInfo;
    private final PositionInfo positionInfo;

    public VisionInfo(PitchInfo pitchInfo, PositionInfo positionInfo)
    {
        this.pitchInfo = pitchInfo;
        this.positionInfo = positionInfo;
    }

    public PitchInfo getPitchInfo()
    {
        return pitchInfo;
    }

    public PositionInfo getPositionInfo()
    {
        return positionInfo;
    }
}
