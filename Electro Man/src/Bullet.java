import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet extends Obstacle
{
    public static final int BULLETHEIGHT = 40;
    public static final int BULLETWIDTH = 40;
    int speed;
    int direction;

    public Bullet(int speed, int direction, BufferedImage img)
    {
        super(new Rectangle(Bullet.BULLETWIDTH,Bullet.BULLETHEIGHT));
        this.speed=speed;
        this.direction=direction;
        color=Color.BLACK;
        this.image = img;
    }

    public boolean update(Game game)
    {
        //check for wall collision
        //playerCollision
        //oob
        for (int i=0; i<game.Obstacles.size(); i++)
        {
            Obstacle obstacle = game.Obstacles.get(i);
            if (obstacle != this && this.collidesWith(obstacle))
            {
                //collides
                if (obstacle instanceof Wall)
                {
                    destroyBullet(game);
                    game.Obstacles.remove(this);
                    i--;
                    return true;
                }
                else if (obstacle instanceof Player)
                {
                    game.Obstacles.remove(this);
                    ((Player) obstacle).setAlive(false);
                    destroyBullet(game);
                    i--;
                    return true;
                }
            }
        }

        //otherwise just move up
        setYLocation(getY()+ speed*direction);
        return false;
    }

    public void destroyBullet( Game game)
    {
        //print animation
        Explosion e = new Explosion( getX(), getY());
        game.addObstacle(e);

    }

    public void printBox(Graphics g, int x, int y)
    {

        if(getImage()!=null)
        {
            g.drawImage(getImage(),x,y,null);
        }
        else
        {
            g.setColor(color);
            g.fillRect(x,
                    y,
                    (int) getRect().getWidth(),
                    (int) getRect().getHeight());
        }
    }

}
