package melmac.simulator.comms.nxthandlers;

import melmac.core.comms.MessageHandler;
import melmac.simulator.Player;

public final class CurvedMoveMessageHandler implements MessageHandler
{
    private final Player player;

    public CurvedMoveMessageHandler(Player player)
    {
        this.player = player;
    }

    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
