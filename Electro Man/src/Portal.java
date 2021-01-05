import java.awt.*;
import java.awt.image.BufferedImage;

public class Portal extends Obstacle
{
    Portal partner;
    char portalChar;

    public Portal(Rectangle rect, BufferedImage img, char c)
    {
        super(rect, img);
        this.color=Color.MAGENTA;
        portalChar=c;
    }

    public void setPartner(Portal p)
    {
        partner=p;
    }

    public Portal getPartner() {
        return partner;
    }

    public void performFunction(Player p, Rectangle futureRect)
    {
        /*System.out.println("currY: "+p.getY()
        +"\nNextY: "+getPartner().getY());*/
        futureRect.setLocation(getPartner().getX()+getWidth(), getPartner().getY());
        //System.out.println("pY: "+p.getY());
    }
}
