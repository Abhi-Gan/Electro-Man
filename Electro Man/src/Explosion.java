import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class Explosion extends Obstacle {
    public static final int EXPLODEHEIGHT = 238;
    public static final int EXPLODEWIDTH = 223;
    private boolean explodeCompleted = false;
    Animation explosion;

    public Explosion ( int x, int y)
    {
        super(new Rectangle(EXPLODEWIDTH, EXPLODEHEIGHT));
        getRect().setLocation(x-EXPLODEWIDTH/2, y-EXPLODEHEIGHT/2);
       color=Color.BLUE;
       explosion = new Animation(Animation.EXPLODEANIMATION, color);
        this.image = new BufferedImage(EXPLODEWIDTH, EXPLODEHEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    }


    public void printBox(Graphics g, int x, int y)
    {
        if (explodeCompleted)
            return;
        BufferedImage tempImage = new BufferedImage(EXPLODEWIDTH, EXPLODEHEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics tempGraphics = tempImage.getGraphics();

        explosion.printImage(tempGraphics, 0 ,0);
        explosion.update();

        //remove from arrayList if time elapsed
        double diff = explosion.getTimeElapsed();
        if (diff > 2 ) {
                   explodeCompleted = true;
        }

        g.drawImage(tempImage, x, y, null);
    }

    @Override
    public boolean collidesWith(Collidable c) {
            return false;
    }
}
