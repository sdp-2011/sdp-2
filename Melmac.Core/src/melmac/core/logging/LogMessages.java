package melmac.core.logging;

public final class LogMessages
{
    public static final LogMessages NxtCommunications = new LogMessages(LogMessage.NxtCommunicationsBase, "Nxt");
    public static final LogMessages RcxCommunications = new LogMessages(LogMessage.RcxCommunicationsBase, "Rcx");
    public static final LogMessages JavaCommunications = new LogMessages(LogMessage.JavaCommunicationsBase, "Jav");
    public static final LogMessages PythonCommunications = new LogMessages(LogMessage.PythonCommunicationsBase, "Pyt");

    private final int base;
    private final String prefix;
    public final int CmsStrt;
    public final int CmsWtng;
    public final int CmsCntd;
    public final int CmsDctd;
    public final int CmsStpd;
    public final int WdgStrt;
    public final int WdgStpd;
    public final int WdgDied;
    public final int WdgSgnd;
    public final int RecStrt;
    public final int RecStpd;
    public final int RecDied;
    public final int RecSgnd;
    public final int SndStrt;
    public final int SndStpd;
    public final int SndDied;
    public final int SndSgnd;
    public final int SndMeQu;
    public final int SndMeSe;
    public final int SndQuOF;
    public final int SndReFo;
    public final int PrcStrt;
    public final int PrcStpd;
    public final int PrcDied;
    public final int PrcSgnd;
    public final int PrcMeQu;
    public final int PrcMePr;
    public final int PrcQuOF;
    public final int PrcReFo;

    private LogMessages(int base, String prefix)
    {
        this.base = base;
        this.prefix = prefix;
        this.CmsStrt = base;
        this.CmsWtng = base + 1;
        this.CmsCntd = base + 2;
        this.CmsDctd = base + 3;
        this.CmsStpd = base + 4;
        this.WdgStrt = base + 5;
        this.WdgStpd = base + 6;
        this.WdgDied = base + 7;
        this.WdgSgnd = base + 8;
        this.RecStrt = base + 9;
        this.RecStpd = base + 10;
        this.RecDied = base + 11;
        this.RecSgnd = base + 12;
        this.SndStrt = base + 13;
        this.SndStpd = base + 14;
        this.SndDied = base + 15;
        this.SndSgnd = base + 16;
        this.SndMeQu = base + 17;
        this.SndMeSe = base + 18;
        this.SndQuOF = base + 19;
        this.SndReFo = base + 20;
        this.PrcStrt = base + 21;
        this.PrcStpd = base + 22;
        this.PrcDied = base + 23;
        this.PrcSgnd = base + 24;
        this.PrcMeQu = base + 25;
        this.PrcMePr = base + 26;
        this.PrcQuOF = base + 27;
        this.PrcReFo = base + 28;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public int getBase()
    {
        return base;
    }
}
