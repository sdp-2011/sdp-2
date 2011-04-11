package melmac.core.comms;

import melmac.core.threading.Process;

public interface MessageProcessor extends Process
{

    void reset();

    void setMessageHandler(int messageType, MessageHandler messageHandler);

    void process(int messageType, int[] argBuffer, int argCount) throws Exception;
    
    boolean isSubscriber();
}
