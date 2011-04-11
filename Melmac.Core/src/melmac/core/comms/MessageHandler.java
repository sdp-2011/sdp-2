package melmac.core.comms;

public interface MessageHandler
{

    public boolean handle(int messageType, int[] argBuffer, int argCount) throws Exception;
}
