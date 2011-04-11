package melmac.masterblock.logging;

import lejos.nxt.LCD;
import melmac.core.logging.LogMessageExt;
import melmac.core.logging.LoggerBase;
import melmac.core.logging.Severity;
import melmac.core.logging.SeverityExt;
import melmac.core.logging.Source;
import melmac.core.logging.SourceExt;

public final class ScreenLogger extends LoggerBase
{
    private static final String BLANK_LINE = "                ";
    private final StringBuffer stringBuffer = new StringBuffer();
    private final int startLine;
    private final int endLine;
    private volatile int line;

    public ScreenLogger(int startLine, int endLine)
    {
        super(Source.Nxt);
        this.startLine = startLine;
        this.endLine = endLine;
        line = startLine;
    }

    @Override
    public synchronized void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount, int source)
    {
        /*if (severity == Severity.Debug)
        {
            return;
        }*/

        if (stringBuffer.length() > 0)
        {
            stringBuffer.delete(0, stringBuffer.length());
        }

        stringBuffer.append(SourceExt.toString(source).charAt(0));
        stringBuffer.append(":");
        stringBuffer.append(SeverityExt.toString(severity).charAt(0));
        stringBuffer.append(":");
        stringBuffer.append(LogMessageExt.toString(logMessage, true));

        for (int index = 0; index < argCount; index++)
        {
            stringBuffer.append(",");
            stringBuffer.append(argBuffer[index]);
        }

        LCD.drawString(BLANK_LINE, 0, line);
        LCD.drawString(stringBuffer.substring(0, Math.min(16, stringBuffer.length())), 0, line);
        line = line == endLine ? startLine : (line + 1);
        LCD.drawString(BLANK_LINE, 0, line);
    }
}
