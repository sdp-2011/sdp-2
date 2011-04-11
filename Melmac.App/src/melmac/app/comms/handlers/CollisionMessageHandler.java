package melmac.app.comms.handlers;

import melmac.app.control.CollisionHandler;
import melmac.core.comms.MessageHandler;

public final class CollisionMessageHandler implements MessageHandler
{

    private final CollisionHandler collisionHandler;

    public CollisionMessageHandler(CollisionHandler collisionHandler)
    {
        this.collisionHandler = collisionHandler;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        collisionHandler.handleCollision(argBuffer[0]);
        return false;
    }
}
