package melmac.core.world;

import melmac.core.interfaces.UiInfoProvider;
import melmac.core.interfaces.VisionInfoProvider;
import melmac.core.threading.BasicNotifier;
import melmac.core.threading.Subscriber;

public final class WorldStateProvider extends BasicNotifier implements Subscriber
{

    private final VisionInfoProvider visionInfoProvider;
    private final UiInfoProvider uiInfoProvider;

    public WorldStateProvider(VisionInfoProvider vision, UiInfoProvider userInterface)
    {
        this.visionInfoProvider = vision;
        this.uiInfoProvider = userInterface;
        vision.addSubscriber(this);
        userInterface.addSubscriber(this);
    }

    public WorldState getWorldState()
    {
        VisionInfo visionInfo = visionInfoProvider.getVisionInfo();
        UiInfo uiInfo = uiInfoProvider.getUiInfo();

        if (visionInfo == null || uiInfo == null)
        {
            return null;
        }

        return new WorldState(visionInfo, uiInfo);
    }

    public UiInfoProvider getUiInfoProvider()
    {
        return uiInfoProvider;
    }

    @Override
    public void signal()
    {
        notifySubscribers();
    }
}