package melmac.core.interfaces;

import melmac.core.threading.Notifier;
import melmac.core.world.UiInfo;

public interface UiInfoProvider extends Notifier
{

    UiInfo getUiInfo();

    void resetStrategySelection();
}
