import java.awt.*;
import java.awt.image.BufferedImage;

public class SpeedUp extends PowerUp
{
    public SpeedUp(Rectangle rect, BufferedImage img)
    {
        super(rect, img);
        this.color=Color.black;
    }

    public void performFunction(Player p)
    {
        p.animations.add(new Animation ( Animation.LIGHTNINGANIMATION, Color.GRAY));
        //System.out.println("lightning printed");
        p.setXVelocity(p.getXVelocity()*2);
        //System.out.println(p.getXVelocity());
    }
}
