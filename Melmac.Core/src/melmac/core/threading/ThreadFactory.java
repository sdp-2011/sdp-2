package melmac.core.threading;

public abstract class ThreadFactory
{

    // NOTE: RCX does not support synchronization on classes
    private static final Object lock = new Object();
    private static ThreadFactory threadFactory = new UnnamedThreadFactory();

    protected ThreadFactory()
    {
    }

    protected abstract Thread constructImpl(Runnable runnable, String threadName);

    public static void setFactory(ThreadFactory threadFactory)
    {
        synchronized (lock)
        {
            ThreadFactory.threadFactory = threadFactory;
        }
    }

    public static Thread construct(Runnable runnable, String threadName)
    {
        synchronized (lock)
        {
            return ThreadFactory.threadFactory.constructImpl(runnable, threadName);
        }
    }
}
