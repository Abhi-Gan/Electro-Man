//import sun.java2d.pipe.hw.ExtendedBufferCapabilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Animation
{
    private ArrayList<BufferedImage> animationSequence;
    private int currentAnimationFrame;
    public int FPS=12;
    public static final int runningAnimation = 0;
    public static final int LIGHTNINGANIMATION = 1;
    public static final int EXPLODEANIMATION = 2;
    public static final int SHOOTINGANIMATION = 3;
    public long startTime = System.nanoTime();
    String animationName;


    public Animation(String animationName, int numPictures)
    {
        this.animationName = animationName;
        animationSequence=new ArrayList<>();
        currentAnimationFrame = 0;
        try {

            for (int x = 1; x < numPictures; x++) {
                animationSequence.add(ImageIO.read(new File(animationName + x + ".png")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Animation(int i, Color color)
    {
        animationSequence=new ArrayList<>();
        if(runningAnimation == i)
        {
            FPS=12;
            this.animationName = "runningAnimation";
            try
            {
                BufferedImage silverSpritesheet = ImageTools.recolor(ImageIO.read(new File("silverSpritesheet.png")),
                        color);
                int numCols=14;
                int numRows=1;
                int subImageWidth = silverSpritesheet.getWidth()/numCols;
                int subImageHeight = silverSpritesheet.getHeight()/numRows;

                for(int r=0; r<numRows; r++)
                {
                    for(int c=0; c<numCols; c++)
                    {
                        animationSequence.add(silverSpritesheet.getSubimage(
                                c*subImageWidth, r*subImageHeight, subImageWidth, subImageHeight));
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if(LIGHTNINGANIMATION == i)
        {
            FPS=12;
            this.animationName = "lightningAnimation";
            try
            {
                BufferedImage lightnings = ImageTools.recolor(ImageIO.read(new File("lightnings.png")),
                        color);
                //last 2 in the bottom row dont exist
                int numCols=4;
                int numRows=1;
                int subImageWidth = lightnings.getWidth()/numCols;
                int subImageHeight = lightnings.getHeight()/numRows;

                for(int r=0; r<numRows; r++)
                {
                    for(int c=0; c<numCols; c++)
                    {
                        animationSequence.add(lightnings.getSubimage(
                                c*subImageWidth, r*subImageHeight, subImageWidth, subImageHeight));
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if( EXPLODEANIMATION == i)
        {
            FPS=40;
            this.animationName = "ExplodeAnimation";
            try
            {
                BufferedImage explosion = ImageTools.recolor(ImageIO.read(new File("explosionSpritesheet.png")),
                        color);

                int numCols=9;
                int numRows=1;
                int subImageWidth = explosion.getWidth()/numCols;
                int subImageHeight = explosion.getHeight()/numRows;

                for(int r=0; r<numRows; r++)
                {
                    for(int c=0; c<numCols; c++)
                    {
                        animationSequence.add(explosion.getSubimage(
                                c*subImageWidth, r*subImageHeight, subImageWidth, subImageHeight));
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if( SHOOTINGANIMATION == i)
        {
            FPS=1;
            this.animationName = "shootingAnimation";
            try
            {
                BufferedImage explosion = ImageTools.recolor(ImageIO.read(new File("shootingSpritesheet.png")),
                        color);

                int numCols=18;
                int numRows=1;
                int subImageWidth = explosion.getWidth()/numCols;
                int subImageHeight = explosion.getHeight()/numRows;

                for(int r=0; r<numRows; r++)
                {
                    for(int c=0; c<numCols; c++)
                    {
                        animationSequence.add(explosion.getSubimage(
                                c*subImageWidth, r*subImageHeight, subImageWidth, subImageHeight));
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void update()
    {
        currentAnimationFrame++;
    }

    public void printImage(Graphics g, int x, int y)
    {
        if(getAnimationSequence() == null)
        {
            //dont do animation if no curr animation sequence
        }
        else
        {
            if(currentAnimationFrame/FPS >= getAnimationSequence().size())
            {
                currentAnimationFrame=0;
                //put at -1 if over and is shooting animation
                if(animationName.equals("shootingAnimation"))
                {
                    currentAnimationFrame=-2;
                }
                //setAnimationSequence(null);
            }
            else
            {
                g.drawImage(
                        getAnimationSequence().get(currentAnimationFrame/FPS),
                        x,y,null
                );
            }
        }
    }

    public void setAnimationSequence(ArrayList<BufferedImage> animationSequence)
    {
        this.animationSequence = animationSequence;
    }

    public ArrayList<BufferedImage> getAnimationSequence()
    {
        return animationSequence;
    }

    public double getTimeElapsed()
    {
        long currentTime = System.nanoTime();
        double diff = currentTime-startTime;
        diff /= 1000000000;
        return (diff);
    }

    public int getCurrentAnimationFrame()
    {
        return currentAnimationFrame;
    }

    public void resetStart()
    {
        startTime = System.nanoTime();
    }

}
