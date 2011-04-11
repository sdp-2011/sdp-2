package melmac.app.control;

import melmac.core.strategies.CollisionStrategy;

public final class CollisionHandler
{

    private final CollisionStrategy collisionStrategy;

    public CollisionHandler(CollisionStrategy collisionStrategy)
    {
        this.collisionStrategy = collisionStrategy;
    }

    public void handleCollision(int sensorId) throws Exception
    {
        collisionStrategy.setCollision(sensorId);
    }
}
