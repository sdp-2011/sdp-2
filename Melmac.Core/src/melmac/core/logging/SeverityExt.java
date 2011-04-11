package melmac.core.logging;

public final class SeverityExt
{
    private SeverityExt()
    {
    }

    public static String toString(int severity)
    {
        switch (severity)
        {
            case Severity.Debug:
                return "Debug";
            case Severity.Exception:
                return "Exception";
            case Severity.Info:
                return "Info";
            case Severity.Severe:
                return "Severe";
            case Severity.Warning:
                return "Warning";
        }

        return null;
    }
}
