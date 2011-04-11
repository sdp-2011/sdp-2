package melmac.simulator.comms.pythonhandlers;

import melmac.core.comms.MessageHandler;
import melmac.simulator.Player;

public final class PitchReqMessageHandler implements MessageHandler
{
    private final Player player;

    public PitchReqMessageHandler(Player player)
    {
        this.player = player;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        player.sendPitchInfo();
        return false;
    }
}
