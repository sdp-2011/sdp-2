package melmac.core.logging;

public final class SourceExt
{
    private SourceExt()
    {
    }

    public static String toString(int source)
    {
        switch (source)
        {
            case Source.Java:
                return "Java";
            case Source.Nxt:
                return "Nxt";
            case Source.Python:
                return "Python";
            case Source.Rcx:
                return "Rcx";
            case Source.UI:
                return "UI";
            case Source.Simulator:
                return "Simulator";
        }

        return null;
    }
}
