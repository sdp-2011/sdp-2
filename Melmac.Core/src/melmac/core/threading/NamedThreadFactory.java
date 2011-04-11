package melmac.core.threading;

public final class NamedThreadFactory extends ThreadFactory
{

    @Override
    protected Thread constructImpl(Runnable runnable, String threadName)
    {
        return new ThreadImpl(runnable, threadName);
    }
}
