package melmac.core.comms;

public class MessageType
{
    public static final int JavaToNxtCount = 7;
    public static final int JavaToNxt_Acknowledgement = 0;
    public static final int JavaToNxt_Move = 1;
    public static final int JavaToNxt_Spin = 2;
    public static final int JavaToNxt_Stop = 3;
    public static final int JavaToNxt_Kick = 4;
    public static final int JavaToNxt_CurvedMove = 5;
    public static final int JavaToNxt_Reset = 6;
    public static final int JavaToPythonCount = 5;
    public static final int JavaToPython_Acknowledgement = 0;
    public static final int JavaToPython_PitchReq = 1;
    public static final int JavaToPython_StartFrames = 2;
    public static final int JavaToPython_StopFrames = 3;
    public static final int JavaToPython_Reset = 4;
    public static final int PythonToJavaCount = 4;
    public static final int PythonToJava_Acknowledgement = 0;
    public static final int PythonToJava_Pitch = 1;
    public static final int PythonToJava_Frame = 2;
    public static final int PythonToJava_Log = 3;
    public static final int NxtToJavaCount = 3;
    public static final int NxtToJava_Acknowledgement = 0;
    public static final int NxtToJava_Collision = 1;
    public static final int NxtToJava_Log = 2;
    public static final int NxtToRcxCount = 3;
    public static final int NxtToRcx_Kick = 0;
    public static final int NxtToRcx_Acknowledgement = 1;
    public static final int NxtToRcx_Reset = 2;
    public static final int RcxToNxtCount = 3;
    public static final int RcxToNxt_Acknowledgement = 0;
    public static final int RcxToNxt_Collision = 1;
    public static final int RcxToNxt_Log = 2;
}
