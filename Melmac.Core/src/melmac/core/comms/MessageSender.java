package melmac.core.comms;

import melmac.core.threading.Process;

public interface MessageSender extends Process
{
    
    void reset();

    void acknowledgementReceived();

    void send(int messageType, boolean requiresAcknowledgement, int[] argBuffer, int argCount) throws Exception;

    boolean isFull();

    boolean isSubscriber();
}
