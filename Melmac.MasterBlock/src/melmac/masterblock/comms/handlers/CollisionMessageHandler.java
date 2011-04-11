package melmac.masterblock.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.masterblock.control.CollisionHandler;

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
