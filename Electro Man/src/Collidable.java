/**
 * Abhinav Ganesh
 * 4/22/19
 * Period 7
 */
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Collidable {
    private Rectangle rect;

    public Collidable(Rectangle rect)
    {
        this.rect=rect;
    }

    public Rectangle getRect()
    {
        return rect;
    }

    public void setRect(Rectangle rect)
    {
        this.rect=rect;
    }

    public boolean collidesWith(Collidable c)
    {
        return rect.intersects(c.rect);
    }

    /**add update method?*/
}
