package melmac.slaveblock.logging;

import josx.platform.rcx.LCD;
import josx.platform.rcx.Sound;
import melmac.core.logging.LoggerBase;
import melmac.core.logging.Severity;
import melmac.core.logging.Source;

public final class ScreenLogger extends LoggerBase
{

    public ScreenLogger()
    {
        super(Source.Rcx);
    }

    public void log(boolean broadcast, int severity, int logMessage, int[] argBuffer, int argCount, int source)
    {
        if (severity == Severity.Severe || severity == Severity.Warning || severity == Severity.Exception)
        {
            Sound.beepSequence();
        }

        LCD.showNumber(logMessage);
    }
}
