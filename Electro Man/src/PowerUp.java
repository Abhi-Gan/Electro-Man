import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Abhinav Ganesh
 * 4/22/19
 * Period 7
 */
public class PowerUp extends Obstacle
{
    BufferedImage powerUpImage;
    boolean isVisible;

    //player constructor should have the key controls needed
    public PowerUp(Rectangle rect, BufferedImage img)
    {
        super(
                rect, img
        );
        this.powerUpImage=img;
        this.color = Color.WHITE;
        isVisible=true;
    }

    public void printBox(Graphics g, int x, int y)
    {
        if(isVisible)
        {
            g.setColor(color);
            g.fillRect(x,
                    y,
                    (int) getRect().getWidth(),
                    (int) getRect().getHeight());
            if(getImage()!=null)
            {
                g.drawImage(getImage(),x,y,null);
            }
        }
    }

    public void performFunction(Player p)
    {

    }
}
