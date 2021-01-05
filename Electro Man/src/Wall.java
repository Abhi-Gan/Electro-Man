import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Wall extends Obstacle{
    /*Rectangle r,l,t,b;
    Collidable right,left,top,bottom;*/
    public static final int wallWidth = 100;
    public static final int wallHeight = 50;

    public Wall(Rectangle rect) {
        super(rect);
        this.image=null;
        getRect().setSize(wallWidth,wallHeight);
        color=Color.DARK_GRAY;

        /*r=new Rectangle( (int) (getRect().getX()+getRect().getWidth()), getRect().y,wallWidth,5);
        l=new Rectangle( (int) (getRect().getX()-wallHeight), getRect().y,5,wallHeight);
        t=new Rectangle( getRect().x,(int) (getRect().getY() - wallHeight) ,0,wallWidth);
        b=new Rectangle( getRect().x,(int) (getRect().getY()+getRect().getHeight()) ,wallWidth,5);

        right=new Collidable(r);
        left=new Collidable(l);
        top=new Collidable(t);
        bottom=new Collidable(b);*/
    }

    public Wall(Rectangle rect, BufferedImage img) {
        super(rect);
        this.image=img;
        getRect().setSize(wallWidth,wallHeight);
        color=Color.DARK_GRAY;
    }

    /*public void printBox(Graphics g)
    {
        g.setColor(Color.darkGray);
        g.fillRect((int) getRect().getX(),
                (int) getRect().getY(),
                (int) getRect().getWidth(),
                (int) getRect().getHeight());
        g.drawImage(getImage(),getX(),getY(),null);
    }*/

    /*public Collidable getRight()
    {
        return this.right;
    }

    public Collidable getLeft()
    {
        return this.left;
    }

    public Collidable getTop()
    {
        return this.top;
    }

    public Collidable getBottom()
    {
        return this.bottom;
    }*/
}
