package melmac.vision;

import melmac.core.comms.Communications;
import melmac.core.comms.MessageType;
import melmac.core.comms.handlers.LogMessageHandler;
import melmac.core.logging.LogMessage;
import melmac.core.interfaces.VisionInfoProvider;
import melmac.core.logging.Logger;
import melmac.core.threading.BasicNotifier;
import melmac.core.threading.ProcessBase;
import melmac.core.threading.Subscriber;
import melmac.vision.comms.handlers.FrameMessageHandler;
import melmac.vision.comms.handlers.PitchMessageHandler;
import melmac.core.world.PitchInfo;
import melmac.core.world.PositionInfo;
import melmac.core.world.VisionInfo;
import melmac.vision.comms.PythonCommunications;

public final class Vision extends ProcessBase implements VisionInfoProvider
{

    private final Communications pythonCommunications;
    private final BasicNotifier notificator = new BasicNotifier();
    private volatile VisionInfo visionInfo;
    private volatile PitchInfo pitchInfo;
    private volatile PositionInfo positionInfo;

    public Vision(Logger logger, String host, int port)
    {
        super(logger, LogMessage.VisStarted, LogMessage.VisStopped);
        this.pythonCommunications = new PythonCommunications(logger, host, port);
        this.pythonCommunications.setMessageHandler(MessageType.PythonToJava_Frame, new FrameMessageHandler(this));
        this.pythonCommunications.setMessageHandler(MessageType.PythonToJava_Pitch, new PitchMessageHandler(this));
        this.pythonCommunications.setMessageHandler(MessageType.PythonToJava_Log, new LogMessageHandler(logger));
    }

    @Override
    protected void onStart() throws Exception
    {
        super.onStart();
        pythonCommunications.start();
    }

    @Override
    protected void onStop() throws Exception
    {
        super.onStop();

        if (pythonCommunications.isClearToSend())
        {
            pythonCommunications.sendSync(MessageType.JavaToPython_StopFrames);
        }

        pythonCommunications.stop();
    }

    public void setPitchInfo(PitchInfo pitchInfo) throws Exception
    {
        synchronized (this)
        {
            this.pitchInfo = pitchInfo;
            
            if (pitchInfo != null && positionInfo != null)
            {
                visionInfo = new VisionInfo(pitchInfo, positionInfo);
            }
        }

        pythonCommunications.sendAsync(MessageType.JavaToPython_StartFrames);
    }

    public void setPositionInfo(PositionInfo positionInfo)
    {
        synchronized (this)
        {
            this.positionInfo = positionInfo;

            if (pitchInfo != null && positionInfo != null)
            {
                visionInfo = new VisionInfo(pitchInfo, positionInfo);
            }
        }

        notificator.notifySubscribers();
    }

    @Override
    public synchronized VisionInfo getVisionInfo()
    {
        return visionInfo;
    }

    @Override
    public void addSubscriber(Subscriber subscriber)
    {
        notificator.addSubscriber(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber)
    {
        notificator.removeSubscriber(subscriber);
    }
}
