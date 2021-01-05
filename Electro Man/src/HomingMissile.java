import java.awt.*;
import java.awt.image.BufferedImage;

public class HomingMissile extends Bullet
{
    Player player;
    private int yDirection = 1;
    public double yVelocity=7;
    //public final double missileAcceleration = Game.acceleration*90;
    public HomingMissile(int x, int y, Player p, BufferedImage img)
    {
        super(p.getXVelocity()*11/8, 1, img);
        setRect(new Rectangle(img.getWidth(), img.getHeight()));
        getRect().setLocation(x,y);
        player=p;
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
                    System.out.println("homing destroyed");
                    game.Obstacles.remove(this);
                    ((Player) obstacle).setAlive(false);
                    destroyBullet(game);
                    i--;
                    return true;
                }
            }
        }

        //otherwise just move forward
        //find angle to rotate
        double angle = Math.toDegrees(Math.atan2(
                player.getY() - getY()
                , player.getX() - getX()));

        //nothing
        if(getY() >= player.getY()
        && getY() <= player.getY()+player.getHeight())
        {
            yDirection=0;
            angle = 0;
        }
        //rotate down
        else if(getY() < player.getY() + player.getHeight())
        {
            yDirection=1;
        }
        //rotate up
        else if(getY() > player.getY())
        {
            yDirection=-1;
        }
        setImage(ImageTools.rotate(Game.homingMissileImage, angle));
        //yVelocity=yVelocity+yDirection*missileAcceleration;
        setYLocation((int)(getY() + yVelocity*yDirection));
        //setYLocation(player.getY());
        setXLocation(getX()+ speed*direction);
        return false;
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
