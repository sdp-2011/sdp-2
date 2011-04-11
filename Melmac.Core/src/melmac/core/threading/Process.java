package melmac.core.threading;

public interface Process
{
    void start() throws Exception;

    void stop() throws Exception;

    boolean isRunning();
}
