package melmac.core.threading;

public final class ThreadImpl extends Thread
{

    private final Runnable runnable;

    public ThreadImpl(Runnable runnable)
    {
        this.runnable = runnable;
    }

    public ThreadImpl(Runnable runnable, String threadName)
    {
        super(threadName);
        this.runnable = runnable;
    }

    @Override
    public void run()
    {
        runnable.run();
    }
}
