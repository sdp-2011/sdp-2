package melmac.core.comms;

public final class OutMessageStatus
{

    public static final int PendingSend = 0;
    public static final int Sending = 1;
    public static final int PendingAcknowledgement = 2;
    public static final int PendingAcknowledgementPickup = 3;
    public static final int Sent = 4;

    private OutMessageStatus()
    {
    }
}
