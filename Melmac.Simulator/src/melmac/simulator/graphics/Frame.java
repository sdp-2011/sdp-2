package melmac.simulator.graphics;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import melmac.simulator.bodies.Pitch;
import melmac.simulator.bodies.Wall;

public final class Frame extends JFrame
{
    private final Object closingLock = new Object();

    public Frame(Pitch pitch) throws HeadlessException
    {
        super("Hat-Trick Simulator");
        pack();
        Panel panel = new Panel(pitch);
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Insets insets = getInsets();
        int width = (int)Pitch.INTERNAL_WIDTH + 2 * Wall.THICKNESS + insets.left + insets.right;
        int height = (int)Pitch.INTERNAL_HEIGHT + 2 * Wall.THICKNESS + insets.top + insets.bottom;
        Dimension dimension = new Dimension(width, height);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        setPreferredSize(dimension);
        setSize(dimension);
        setVisible(true);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                synchronized (closingLock)
                {
                    closingLock.notifyAll();
                }
            }
        });
    }

    public void waitForClose() throws InterruptedException
    {
        synchronized (closingLock)
        {
            closingLock.wait();
        }
    }
}
