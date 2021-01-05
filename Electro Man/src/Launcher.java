import java.awt.*;
import java.awt.image.BufferedImage;

public class Launcher extends Obstacle implements Runnable
{
    Game g;
    int direction;
    boolean started;
    public Launcher(Rectangle rect, Game game, int direction)
    {
        super(rect);
        color=Color.ORANGE;
        g=game;
        this.direction=direction;
        started=false;
    }

    public void start()
    {
        started=true;
        Thread t= new Thread(this);
        t.start();
    }

    public boolean isStarted()
    {
        return started;
    }

    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                int speed = 3;
                Bullet bullet = new Bullet(speed , direction, g.bulletImage);
                bullet.getRect().setLocation(getX(), getY()+direction*getHeight());
                g.addObstacle(bullet);
                Thread.sleep(5000);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
