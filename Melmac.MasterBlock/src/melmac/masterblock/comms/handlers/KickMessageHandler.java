package melmac.masterblock.comms.handlers;

import melmac.core.comms.MessageHandler;
import melmac.core.comms.MessageType;
import melmac.masterblock.comms.RcxCommunications;

public final class KickMessageHandler implements MessageHandler
{

    private final RcxCommunications rcxCommunications;

    public KickMessageHandler(RcxCommunications rcxCommunications)
    {
        this.rcxCommunications = rcxCommunications;
    }

    @Override
    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception
    {
        rcxCommunications.sendSync(MessageType.NxtToRcx_Kick, argBuffer, argCount);
        return true;
    }
}
