package melmac.core.control;

public abstract class CollisionListenerBase
{
    private final CollisionHandlerBase collisionHandler;
    private final int sensorId;
    private final int downValueBoundary;
    private boolean isDown;

    protected CollisionListenerBase(CollisionHandlerBase collisionHandler, int sensorId, int downValueBoundary)
    {
        this.collisionHandler = collisionHandler;
        this.sensorId = sensorId;
        this.downValueBoundary = downValueBoundary;
    }

    public void stateChanged(int oldValue, int newValue)
    {
        if (oldValue == 0)
        {
            return;
        }

        boolean handleCollision = false;

        synchronized (this)
        {
            if (isDown && newValue > downValueBoundary)
            {
                isDown = false;
            }
            else if (!isDown && newValue <= downValueBoundary)
            {
                isDown = true;
                handleCollision = true;
            }
        }

        if (handleCollision)
        {
            collisionHandler.handleCollision(sensorId);
        }
    }
}
