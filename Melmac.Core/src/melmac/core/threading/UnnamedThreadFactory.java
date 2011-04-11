package melmac.core.threading;

public final class UnnamedThreadFactory extends ThreadFactory
{

    @Override
    protected Thread constructImpl(Runnable runnable, String threadName)
    {
        return new ThreadImpl(runnable);
    }
}
