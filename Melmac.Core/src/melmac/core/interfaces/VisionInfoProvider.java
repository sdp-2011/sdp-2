package melmac.core.interfaces;

import melmac.core.threading.Notifier;
import melmac.core.world.VisionInfo;
import melmac.core.threading.Process;

public interface VisionInfoProvider extends Notifier, Process
{

    VisionInfo getVisionInfo();
}
