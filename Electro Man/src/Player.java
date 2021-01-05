import javafx.scene.input.KeyCode;
//import sun.awt.WindowIDProvider;
//import sun.nio.ch.FileKey;

import javax.imageio.ImageIO;
import javax.sound.sampled.Port;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.rmi.activation.ActivationGroup_Stub;
import java.security.Key;
import java.util.ArrayList;

/**
 * Abhinav Ganesh
 * 4/22/19
 * Period 7
 */
public class Player extends Obstacle implements Runnable
{
    BufferedImage playerImage;
    BufferedImage flipped;
    private int yDirection = 1;
    public double Yvelocity=0;
    public final int UP = 0;
    public final int DOWN = -1;
    private boolean alive=true;
    //private KeyEvent gravityKey;
    private char gravityChar;
    private char shootChar;
    String name;
    private boolean canJump;
//   public Animation lightning= new Animation("lightning", 4);
   ArrayList<Animation> animations;
    int xVelocity;
    private boolean canShoot;

   /* public Animation getLightning() { lightning.resetStart();
                                        return lightning;}*/

    //private boolean isPlayerUP, isPlayerDOWN, isPlayerLEFT, isPlayerRIGHT;

    //player constructor should have the key controls needed
    public Player(BufferedImage playerImage, Color color)
    {
        super(
                new Rectangle(playerImage.getWidth(), playerImage.getHeight()),
                playerImage
        );
        setYLocation(500);
        setXLocation(500);
        this.playerImage=ImageTools.recolor(playerImage, color);
        flipped = flipImage(playerImage);
        this.color=color;
        alive=true;
        xVelocity=2;
        animations=new ArrayList<>();
        animations.add(new Animation(Animation.runningAnimation, color));
        canShoot=true;
        //animations.add(lightning);
    }

    public void printBox(Graphics g, int x, int y)
    {
        /*if(getImage()!=null)
        {
            g.drawImage(getImage(),x,y,null);
        }*/
        BufferedImage tempImage = new BufferedImage(playerImage.getWidth(), playerImage.getHeight(), playerImage.getColorModel().getTransparency());
        Graphics tempGraphics = tempImage.getGraphics();
        //print all animations on top
        for(int i=0;i<animations.size();i++)
        {
            Animation currentAnimation=animations.get(i);
            currentAnimation.printImage(tempGraphics, 0 ,0);
            currentAnimation.update();
            //remove from arrayList if time elapsed
            if(!currentAnimation.animationName.equals("runningAnimation") )
            {
            double diff = currentAnimation.getTimeElapsed();
            if (diff > 5 )
            {
                animations.remove(i);
                i--;
            }
            }
            //remove shooting animation after it is over
            //after shooting animation over, add back running animation
            if(currentAnimation.getCurrentAnimationFrame() < 0)
            {
                animations.remove(i);
                i--;
                if(currentAnimation.animationName.equals("shootingAnimation"))
                {
                    animations.add(new Animation(Animation.runningAnimation, color));
                }
            }
        }

        BufferedImage finalImage;

        if(yDirection == 1)
        {
            finalImage = tempImage;
        }
        else
        {
            finalImage = flipImage(tempImage);
        }
        g.drawImage(finalImage, x, y, null);
    }

    public void shoot(Game game)
    {
        if(canShoot)
        {
            //change animation
            //remove running animation
            for(int i=0;i<animations.size();i++) {
                Animation currentAnimation = animations.get(i);
                if (currentAnimation.animationName.equals("runningAnimation")) {
                    animations.remove(i);
                    i--;
                }
            }
            //add shooting animation
            animations.add(new Animation(Animation.SHOOTINGANIMATION, color));
            //actual code
            horizBullet bullet = new horizBullet(
                    getXVelocity() * 4
                    , 1
                    , Game.energyBall
            );
            bullet.setXLocation(getX() + getWidth() + 2);
            bullet.setYLocation(getY() + getHeight() / 2 + yDirection*-1*10);
            game.addObstacle(bullet);

            Thread t = new Thread(this);
            t.start();
        }
    }

    public int getXVelocity()
    {
        return xVelocity;
    }

    public void setXVelocity(int i)
    {
        xVelocity=i;
    }

    public void setYvelocity(double yvelocity)
    {
        this.Yvelocity=yvelocity;
    }

    public double getYvelocity()
    {
        return Yvelocity;
    }

    public int getYDirection()
    {
        return yDirection;
    }

    public void setYDirection(int yDirection)
    {
        this.yDirection=yDirection;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void switchYDirection()
    {
        yDirection*=-1;
        //System.out.println("yDirection: "+getYDirection());
        //flip image

        //System.out.println("Direction switched to "+yDirection);
    }

    public BufferedImage flipImage(BufferedImage image)
    {
        BufferedImage flipped = new BufferedImage(image.getWidth(), image.getHeight(),
                image.getColorModel().getTransparency());
        flipped.getGraphics().drawImage(
                image,
                0,image.getHeight(),
                image.getWidth(), 0,
                0,0,
                image.getWidth(), image.getHeight(),
                null
        );
        //System.out.println("image flipped");
        return flipped;
    }

    /*public KeyEvent getGravityKey()
    {
        return gravityKey;
    }

    public void setGravityKey(KeyEvent e)
    {
         gravityKey = e;
    }*/

    /*public void setPlayerUP(boolean b)
    {
        isPlayerUP=b;
    }
    public void setPlayerDOWN(boolean b)
    {
        isPlayerDOWN=b;
    }
    public void setPlayerLEFT(boolean b)
    {
        isPlayerLEFT=b;
    }
    public void setPlayerRIGHT(boolean b)
    {
        isPlayerRIGHT=b;
    }*/



    public void update(Game game)
    {
        //System.out.println("Entering Player.Update method ");
        //double oldYlocation = this.getRect().getY();
        Rectangle fRect = new Rectangle(getX(),getY(),
                getWidth(), getHeight());

        if(isAlive())
        {
            moveHoriz(fRect, getXVelocity(), game);
        /*System.out.println("currentX: "+getX()
        +"\nFutureX: "+fRect.getX());*/
            //correct
            setYvelocity(getYvelocity()+getYDirection()*Game.acceleration);
            moveVert(fRect, (int) getYvelocity(), game);
        }
        else
        {
            game.players.remove(this);
        }

        //move right, then check
        //move left, then check
        setRect(
                fRect
        );
    }

    public boolean isAlive()
    {
        return alive;
    }

    public void setAlive(boolean b)
    {
        this.alive=b;
    }

    public void moveHoriz(Rectangle rectangle, int x, Game game)
    {
        rectangle.setLocation(getX()+x, getY());
        ArrayList<Obstacle> Obstacles = game.getObstacles();

        for(int i=0; i<Obstacles.size(); i++)
        {
            Obstacle currentObstacle= Obstacles.get(i);
            //dont count urself obviously
            //also dont count powerups
            if(!(currentObstacle instanceof Player &&
            this.equals((Player) currentObstacle))
                    && rectangle.intersects(currentObstacle.getRect())
            && !(currentObstacle instanceof Explosion))
            {
                if(currentObstacle instanceof PowerUp)
                {
                    ((PowerUp) currentObstacle).performFunction(this);
                    Obstacles.remove(i);
                    i--;
                }
                else if(currentObstacle instanceof Portal)
                {
                ((Portal) currentObstacle).performFunction(this, rectangle);
                //System.out.println("portalFunctions");
                }
                else if(currentObstacle instanceof Bullet)
                {
                    //dont do anything
                }
                else
                {
                    //System.out.println(currentObstacle);
                    //playerHitsWall();
                    rectangle.setLocation(currentObstacle.getX() - getWidth()
                            , getY()
                    );
                }

            }
        }
        setRect(rectangle);
        //now check
    }

    public void moveVert(Rectangle rectangle, int y, Game game)
    {
        rectangle.setLocation(getX(), getY()+y);
        ArrayList<Obstacle> Obstacles = game.getObstacles();

        // XX
        /*System.out.println ("XX yvelocity is " + getYvelocity()
                            + "  Ypostition is " + this.yLocation);*/


  //      setCanJump(false);/**  XX Look here!*/
        //will be false if doesnt collide with anything
        for(int i=0; i<Obstacles.size(); i++)
        {
            Obstacle currentObstacle = Obstacles.get(i);
            //dont count urself obviously
            //also dont count powerups
           if(!(currentObstacle instanceof Player &&
                    this.equals((Player) currentObstacle))
                    && rectangle.intersects(currentObstacle.getRect())
                   && !(currentObstacle instanceof Explosion))
            {
                if(currentObstacle instanceof PowerUp)
                {
                    ((PowerUp) currentObstacle).performFunction(this);
                    Obstacles.remove(i);
                    i--;
                }
                else if(currentObstacle instanceof Portal)
                {
                    ((Portal) currentObstacle).performFunction(this, rectangle);
                    //System.out.println("portalFunctions");
                }
                else if(currentObstacle instanceof Bullet)
                {
                    //dont do anything
                }
                else
                {
                    //System.out.println(getYDirection());
                    //playerHitsWall();
                    if(//is positive velocity
                            getYvelocity()>0)
                    {
                        //System.out.println("Entered Obstacle collide Y dir = 1");
                        rectangle.setLocation(getX()
                                , currentObstacle.getY() - getHeight()
                        );
                    }
                    else if(//is negative velocity
                            getYvelocity()<0)
                    {
                        rectangle.setLocation(getX()
                                , currentObstacle.getY() + currentObstacle.getHeight()
                        );
                    }
                    setCanJump(true);
                    //System.out.println("jump1"+canJump);
                    //System.out.println("currentObstacle: "+currentObstacle);
                    setYvelocity(0);
                    //               return;
                }
            }
        }
        setRect(rectangle);
        //now check
    }

    public void setCanJump(boolean b)
    {
        this.canJump=b;
    }

    public boolean canJump()
    {
 //       System.out.println("CanJump: "+canJump);
        return canJump;
    }

    public void setGravityChar(char c)
    {
        gravityChar=c;
    }

    public char getGravityChar()
    {
        return gravityChar;
    }

    public char getShootChar()
    {
        return shootChar;
    }

    public void setShootChar(char shootChar) {
        this.shootChar = shootChar;
    }

    public boolean equals(Player p)
    {
        /*boolean isEqual=true;

        if(!getImage().equals(p.getImage()))
        {
            isEqual=false;
            //System.out.println("Image");
        }
        if(!color.equals(p.color))
        {
            isEqual=false;
            //System.out.println("color");
        }
        if(//!getRect().equals(p.getRect())
                !(getRect().getHeight() == p.getRect().getHeight()
                && getRect().getWidth() == p.getRect().getWidth())
        )
        {
            //System.out.println("size");
            isEqual=false;
        }*/
        /*if(!(getYDirection() == p.getYDirection()))
        {
            System.out.println("direction");
            isEqual=false;
        }
        if(!(getYvelocity() == p.getYvelocity()))
        {
            System.out.println("velocity");
            isEqual=false;
        }*/

        return p.getName().equals(getName());
    }

    @Override
    public BufferedImage getImage()
    {
        return (yDirection==1)?super.getImage():flipped;
    }

    @Override
    public void run()
    {
        try
        {
            canShoot=false;
            Thread.sleep(500);
            canShoot=true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
