import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Abhinav Ganesh
 * 4/22/19
 * Period 7
 */
public class Obstacle extends Collidable
{

    BufferedImage image;
    int xLocation;
    int yLocation;
    Color color;
    //boolean isVisible;
    /** add bacl isVisible later?*/

    public Obstacle(Rectangle rect) {
        super(rect);
       // color=Color.RED;
        //isVisible = true;
        //setRect(new Rectangle(50,10));
        //image= ImageIO.read(new File("YouWin.jpg"));
    }

    public Obstacle(Rectangle rect, BufferedImage image)
    {
        super(rect);
        this.image=image;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image=image;
    }

    public void printBox(Graphics g, int x, int y)
    {
        //if(isVisible)
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

    /*public void setVisible(boolean b)
    {
        isVisible = b;
    }

    public boolean getVisible()
    {
        return isVisible;
    }*/

    public void update()
    {
        //have to fill in later if needed
    }

    public void setXLocation(int xLocation)
    {
        getRect().setLocation(xLocation ,
                getRect().y);
        xLocation=(int) getRect().getX();
        yLocation=(int) getRect().getY();
    }

    public void setYLocation(int yLocation)
    {
        getRect().setLocation( getRect().x ,
                yLocation);
        xLocation=(int) getRect().getX();
        yLocation=(int) getRect().getY();
    }

    public int getX()
    {
        return (int) getRect().getX();
    }

    public int getY()
    {
        return (int) getRect().getY();
    }

    public int getWidth()
    {
        return (int) getRect().getWidth();
    }
    public int getHeight()
    {
        return (int) getRect().getHeight();
    }

    @Override
    public boolean collidesWith(Collidable c)
    {
        /*if(isVisible)
        {
            return super.collidesWith(c);
        }
        else
        {
            return false;
        }*/
        return super.collidesWith(c);
    }
}
