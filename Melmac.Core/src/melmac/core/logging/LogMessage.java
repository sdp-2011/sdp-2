package melmac.core.logging;

public final class LogMessage
{
    public static final int NxtCommunicationsBase = 1000;
    public static final int RcxCommunicationsBase = 1100;
    public static final int JavaCommunicationsBase = 1200;
    public static final int PythonCommunicationsBase = 1300;
    public static final int JavStarted = 0;
    public static final int JavStopped = 1;
    public static final int NxtStarted = 2;
    public static final int Nxt__Reset = 3;
    public static final int RcxStarted = 4;
    public static final int Rcx__Reset = 5;
    public static final int VisStarted = 6;
    public static final int VisStopped = 7;
    public static final int SimStarted = 8;
    public static final int SimStopped = 9;
    public static final int AgtStarted = 10;
    public static final int AgtStopped = 11;
    public static final int Agent_Died = 12;
    public static final int AgtSgnlled = 13;
    public static final int EBadOnDura = 14;
    public static final int EBadOffDur = 15;
    public static final int ETooMnyArg = 16;
    public static final int EHlfDplxFl = 17;
    public static final int EColHlrSnd = 18;
    public static final int EVisBdArgs = 19;
    public static final int ECmdSndFai = 20;
    public static final int EFldDscnct = 21;
    public static final int ESimColHdr = 22;
    public static final int UIWindStrt = 23;
    public static final int UIStrtPrss = 24;
    public static final int UIStopPrss = 25;
    public static final int UIBtnPress = 26;
    public static final int UIManInput = 27;
    public static final int UIStratChn = 28;
    public static final int UIIlglActn = 29;
    public static final int UIWinClose = 30;
    public static final int PlyStarted = 31;
    public static final int PlyStopped = 32;
    public static final int TimStarted = 33;
    public static final int TimStopped = 34;
    public static final int Time__Died = 35;
    public static final int TimSgnlled = 36;
    public static final int CHrStarted = 37;
    public static final int CHrStopped = 38;
    public static final int DEBUG = 99;

    private LogMessage()
    {
    }
}
