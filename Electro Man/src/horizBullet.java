import java.awt.image.BufferedImage;

public class horizBullet extends Bullet
{
    int horizSpeed;
    int horizDirection;

    public horizBullet(int horizSpeed, int horizDirection, BufferedImage img)
    {
        super(0, 0, img);
        this.horizSpeed=horizSpeed;
        this.horizDirection=horizDirection;
    }

    @Override
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

        //otherwise move horiz and up
        setXLocation(getX() + horizSpeed*horizDirection);
        //setYLocation(getY()+ speed*direction);
        return false;
    }
}
