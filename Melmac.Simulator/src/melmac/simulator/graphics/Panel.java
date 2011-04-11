package melmac.simulator.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import javax.swing.JPanel;
import melmac.simulator.bodies.Pitch;

public final class Panel extends JPanel
{

    private final Pitch pitch;
    //private Image image;

    public Panel(Pitch pitch) throws HeadlessException
    {
        this.pitch = pitch;
    }

    @Override
    public void paint(Graphics graphics)
    {
        /*if (image == null)
        {
            image = this.createImage(this.getWidth(), this.getHeight());
        }*/

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
        pitch.render(graphics2D);
        
        //graphics.drawImage(image, this.getWidth(), this.getHeight(), null);
    }
}
