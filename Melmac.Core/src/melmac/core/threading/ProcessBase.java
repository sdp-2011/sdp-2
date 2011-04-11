package melmac.core.threading;

import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.utils.Constants;

public abstract class ProcessBase implements Process
{
    private final Logger logger;
    private final int startedLogMessage;
    private final int stoppedLogMessage;
    private volatile boolean running;

    protected ProcessBase(Logger logger, int startedLogMessage, int stoppedLogMessage)
    {
        this.logger = logger;
        this.startedLogMessage = startedLogMessage;
        this.stoppedLogMessage = stoppedLogMessage;
    }

    @Override
    public void start() throws Exception
    {
        synchronized (this)
        {
            // Just return if we're running
            if (running)
            {
                return;
            }

            running = true;
        }

        onStart();

        synchronized (this)
        {
            notifyAll();
        }

        logger.log(true, Severity.Info, startedLogMessage);
    }

    @Override
    public void stop() throws Exception
    {
        synchronized (this)
        {
            // Just return if we're not running
            if (!running)
            {
                return;
            }
        }

        onStop();

        synchronized (this)
        {
            running = false;
            notifyAll();
        }

        logger.log(true, Severity.Info, stoppedLogMessage);
    }

    @Override
    public synchronized boolean isRunning()
    {
        return running;
    }

    protected void onStart() throws Exception
    {
    }

    protected void onStop() throws Exception
    {
    }

    protected synchronized boolean waitCheckRunning()
    {
        try
        {
            if (running)
            {
                wait(Constants.MONITOR_WAIT_TIMEOUT);
                return running;
            }

            return false;
        }
        catch (InterruptedException ex)
        {
            return false;
        }
    }

    protected Logger getLogger()
    {
        return logger;
    }
}
