package melmac.core.logging;

public final class LogMessagesExt
{
    public static final int CmsStrt = 0;
    public static final int CmsWtng = 1;
    public static final int CmsCntd = 2;
    public static final int CmsDctd = 3;
    public static final int CmsStpd = 4;
    public static final int WdgStrt = 5;
    public static final int WdgStpd = 6;
    public static final int WdgDied = 7;
    public static final int WdgSgnd = 8;
    public static final int RecStrt = 9;
    public static final int RecStpd = 10;
    public static final int RecDied = 11;
    public static final int RecSgnd = 12;
    public static final int SndStrt = 13;
    public static final int SndStpd = 14;
    public static final int SndDied = 15;
    public static final int SndSgnd = 16;
    public static final int SndMeQu = 17;
    public static final int SndMeSe = 18;
    public static final int SndQuOF = 19;
    public static final int SndReFo = 20;
    public static final int PrcStrt = 21;
    public static final int PrcStpd = 22;
    public static final int PrcDied = 23;
    public static final int PrcSgnd = 24;
    public static final int PrcMeQu = 25;
    public static final int PrcMePr = 26;
    public static final int PrcQuOF = 27;
    public static final int PrcReFo = 28;

    public static String toString(LogMessages logMessages, int logMessage, boolean useShortForm)
    {
        int base = logMessages.getBase();
        String prefix = logMessages.getPrefix();

        if (logMessage - base == CmsStrt)
        {
            return prefix + "CmsStrt";
        }
        else if (logMessage - base == CmsWtng)
        {
            return prefix + "CmsWtng";
        }
        else if (logMessage - base == CmsCntd)
        {
            return prefix + "CmsCntd";
        }
        else if (logMessage - base == CmsDctd)
        {
            return prefix + "CmsDctd";
        }
        else if (logMessage - base == CmsStpd)
        {
            return prefix + "CmsStpd";
        }
        else if (logMessage - base == WdgStrt)
        {
            return prefix + "WdgStrt";
        }
        else if (logMessage - base == WdgStpd)
        {
            return prefix + "WdgStpd";
        }
        else if (logMessage - base == WdgDied)
        {
            return prefix + "WdgDied";
        }
        else if (logMessage - base == WdgSgnd)
        {
            return prefix + "WdgSgnd";
        }
        else if (logMessage - base == RecStrt)
        {
            return prefix + "RecStrt";
        }
        else if (logMessage - base == RecStpd)
        {
            return prefix + "RecStpd";
        }
        else if (logMessage - base == RecDied)
        {
            return prefix + "RecDied";
        }
        else if (logMessage - base == RecSgnd)
        {
            return prefix + "RecSgnd";
        }
        else if (logMessage - base == SndStrt)
        {
            return prefix + "SndStrt";
        }
        else if (logMessage - base == SndStpd)
        {
            return prefix + "SndStpd";
        }
        else if (logMessage - base == SndDied)
        {
            return prefix + "SndDied";
        }
        else if (logMessage - base == SndSgnd)
        {
            return prefix + "SndSgnd";
        }
        else if (logMessage - base == SndMeQu)
        {
            return prefix + "SndMeQu";
        }
        else if (logMessage - base == SndMeSe)
        {
            return prefix + "SndMeSe";
        }
        else if (logMessage - base == SndQuOF)
        {
            return prefix + "SndQuOF";
        }
        else if (logMessage - base == SndReFo)
        {
            return prefix + "SndReFo";
        }
        else if (logMessage - base == PrcStrt)
        {
            return prefix + "PrcStrt";
        }
        else if (logMessage - base == PrcStpd)
        {
            return prefix + "PrcStpd";
        }
        else if (logMessage - base == PrcDied)
        {
            return prefix + "PrcDied";
        }
        else if (logMessage - base == PrcSgnd)
        {
            return prefix + "PrcSgnd";
        }
        else if (logMessage - base == PrcMeQu)
        {
            return prefix + "PrcMeQu";
        }
        else if (logMessage - base == PrcMePr)
        {
            return prefix + "PrcMePr";
        }
        else if (logMessage - base == PrcQuOF)
        {
            return prefix + "PrcQuOF";
        }
        else if (logMessage - base == PrcReFo)
        {
            return prefix + "PrcReFo";
        }

        return null;
    }
}
