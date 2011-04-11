package melmac.core.threading;

import melmac.core.logging.Logger;
import melmac.core.logging.Severity;
import melmac.core.utils.Constants;

/**
 * All asynchronous processes, i.e. those that run on their own threads, should implement this abstract base class.
 * The only place the Thread class should be referenced is here (and in Sleeper).
 * Any async process can be signalled/notified though not every sub class will make use of that feature.
 * A thread is created when the process is started (i.e. *not* at construction). The thread remains alive while the
 * process if running and is destroyed when the process is stopped. Note that a new thread is created every
 * time the process is stopped/started; this is especially important on the RCX which has no garbage collection so the
 * start/start cycle should be avoided under ordinary conditions.
 */
public abstract class AsyncProcess extends ProcessBase implements Subscriber
{
    /**
     * Hides the implementation details from subclasses and users (i.e. avoids AsyncProcess extending Thread or implementing Runnable).
     * Tries to continue even in the face of exceptions. This can cause problems if an exception occurs repeatedly.
     */
    private final class Runner implements Runnable
    {
        @Override
        public void run()
        {
            // Loops forever but a "break" will be issued when the thread is told to stop
            while (true)
            {
                try
                {
                    boolean runExecution = true;

                    // If we're no longer running break out of the loop
                    if (!isRunning() || Thread.interrupted())
                    {
                        break;
                    }

                    // Looks like there's *probably* something to do (the subclass should still check whether that's
                    // still the case and just return if there isn't anything to do)
                    if (runExecution && !execute())
                    {
                        break;
                    }

                    // The subclass can tell us to avoid waiting if it knows there is still work to be done
                    if (isRunning() && !Thread.interrupted() && shouldWait())
                    {
                        // Wait until something changes (e.g. the object is signalled to indicate there's work to do or
                        // a stop command has been received). This has a timeout because Java monitors don't work
                        // like .NET manual/auto reset events - a notify might happen while we're not waiting.
                        synchronized (AsyncProcess.this)
                        {
                            AsyncProcess.this.wait(Constants.MONITOR_WAIT_TIMEOUT);
                        }
                    }
                }
                catch (InterruptedException interruptedException)
                {
                    break;
                }
                catch (Exception exception)
                {
                    if (onException(exception))
                    {
                        getLogger().log(true, Severity.Severe, diedLogMessage);
                    }
                }
            }

            synchronized (AsyncProcess.this)
            {
                AsyncProcess.this.notifyAll();
            }
        }
    }
    private final String threadName;
    private final int diedLogMessage;
    private final int signalledLogMessage;
    private volatile Thread thread;

    protected AsyncProcess(Logger logger, int startedLogMessage, int stoppedLogMessage, int diedLogMessage,
                           int signalledLogMessage, String threadName)
    {
        super(logger, startedLogMessage, stoppedLogMessage);
        this.threadName = threadName;
        this.diedLogMessage = diedLogMessage;
        this.signalledLogMessage = signalledLogMessage;
    }

    protected abstract boolean execute() throws Exception;

    protected abstract boolean shouldWait() throws Exception;

    @Override
    protected void onStart() throws Exception
    {
        super.onStart();

        synchronized (this)
        {
            // Uses a thread factory because that's the only way to have named threads on the PC and unnamed threads on
            // the RCX (which doesn't support them). The NXT supports named threads but curiously reverses the order of
            // the parameters in the Thread constructor which gives another need for the factory.
            // The factory implementation is setup statically.
            // The RCX doesn't support the Runnable imnterface out of the box so a custom Runnable interface is used.
            thread = ThreadFactory.construct(new Runner(), threadName);
            thread.start();
        }
    }

    @Override
    protected void onStop() throws Exception
    {
        // If the main execute method blocks (e.g. waiting for data on a DataInputStream) then it's necessary to kill
        // the thread before attempting a normal stop - a normal stop won't interrupt a blocking execution.
        synchronized (this)
        {
            if (thread != null)
            {
                thread.interrupt();
            }
        }

        super.onStop();

        synchronized (this)
        {
            if (thread != null)
            {
                thread.interrupt();
            }

            thread = null;
        }
    }

    protected boolean onException(Exception exception)
    {
        return true;
    }

    @Override
    public synchronized void signal()
    {
        notifyAll();
        getLogger().log(false, Severity.Debug, signalledLogMessage);
    }
}
