package melmac.core.logging;

public final class LogMessageExt
{
    private LogMessageExt()
    {
    }

    public static String toString(int logMessage, boolean useShortForm)
    {
        String description = LogMessagesExt.toString(LogMessages.NxtCommunications, logMessage, useShortForm);

        if (description == null)
        {
            description = LogMessagesExt.toString(LogMessages.RcxCommunications, logMessage, useShortForm);
        }

        if (description == null)
        {
            description = LogMessagesExt.toString(LogMessages.JavaCommunications, logMessage, useShortForm);
        }

        if (description == null)
        {
            description = LogMessagesExt.toString(LogMessages.PythonCommunications, logMessage, useShortForm);
        }

        if (description == null)
        {
            switch (logMessage)
            {
                case LogMessage.JavStarted:
                    return "JavStarted";
                case LogMessage.JavStopped:
                    return "JavStopped";
                case LogMessage.NxtStarted:
                    return "NxtStarted";
                case LogMessage.Nxt__Reset:
                    return "Nxt__Reset";
                case LogMessage.RcxStarted:
                    return "RcxStarted";
                case LogMessage.Rcx__Reset:
                    return "Rcx__Reset";
                case LogMessage.VisStarted:
                    return "VisStarted";
                case LogMessage.VisStopped:
                    return "VisStopped";
                case LogMessage.SimStarted:
                    return "SimStarted";
                case LogMessage.SimStopped:
                    return "SimStopped";
                case LogMessage.AgtStarted:
                    return "AgtStarted";
                case LogMessage.AgtStopped:
                    return "AgtStopped";
                case LogMessage.Agent_Died:
                    return "Agent_Died";
                case LogMessage.AgtSgnlled:
                    return "AgtSgnlled";
                case LogMessage.EBadOnDura:
                    return "EBadOnDura";
                case LogMessage.EBadOffDur:
                    return "EBadOffDur";
                case LogMessage.ETooMnyArg:
                    return "ETooMnyArg";
                case LogMessage.EHlfDplxFl:
                    return "EHlfDplxFl";
                case LogMessage.EColHlrSnd:
                    return "EColHlrSnd";
                case LogMessage.EVisBdArgs:
                    return "EVisBdArgs";
                case LogMessage.ECmdSndFai:
                    return "ECmdSndFai";
                case LogMessage.UIWindStrt:
                    return useShortForm ? "UIWindStrt" : "UI Window Started";
                case LogMessage.UIStrtPrss:
                    return useShortForm ? "UIStrtPrss" : "Starting all processes";
                case LogMessage.UIStopPrss:
                    return useShortForm ? "UIStopPrss" : "Stopping all processes";
                case LogMessage.UIBtnPress:
                    return useShortForm ? "UIBtnPress" : "UI Button Pressed";
                case LogMessage.UIManInput:
                    return useShortForm ? "UIManInput" : "Manual input detected";
                case LogMessage.UIStratChn:
                    return useShortForm ? "UIStratChn" : "Strategy changed";
                case LogMessage.UIIlglActn:
                    return useShortForm ? "UIIlglActn" : "Illegal action";
                case LogMessage.UIWinClose:
                    return useShortForm ? "UIWinClose" : "UI window closed";
                case LogMessage.PlyStarted:
                    return "PlyStarted";
                case LogMessage.PlyStopped:
                    return "PlyStopped";
                case LogMessage.TimStarted:
                    return "TimStarted";
                case LogMessage.TimStopped:
                    return "TimStopped";
                case LogMessage.Time__Died:
                    return "Time__Died";
                case LogMessage.TimSgnlled:
                    return "TimSgnlled";
                case LogMessage.CHrStarted:
                    return "CHrStarted";
                case LogMessage.CHrStopped:
                    return "CHrStopped";
                case LogMessage.ESimColHdr:
                    return "ESimColHdr";
                case LogMessage.DEBUG:
                    return "DEBUG";
            }
        }

        if (description == null)
        {
            description = "UNKNOWN_LOG_MESSAGE";
        }

        return description;
    }
}
