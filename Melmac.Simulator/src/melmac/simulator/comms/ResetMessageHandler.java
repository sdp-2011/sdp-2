package melmac.simulator.comms;

import melmac.core.comms.MessageHandler;
import melmac.simulator.Player;

public final class ResetMessageHandler implements MessageHandler
{
    private final Player player;

    public ResetMessageHandler(Player player)
    {
        this.player = player;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        player.reset();
        return true;
    }
}
