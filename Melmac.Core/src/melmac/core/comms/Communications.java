package melmac.core.comms;

import melmac.core.threading.Process;

public interface Communications extends Process
{
    void reset();

    boolean isClearToSend();

    void setMessageHandler(int messageType, MessageHandler messageHandler);

    void sendSync(int messageType) throws Exception;

    void sendAsync(int messageType) throws Exception;

    void sendSync(int messageType, int[] argBuffer, int argCount) throws Exception;

    void sendAsync(int messageType, int[] argBuffer, int argCount) throws Exception;
}
