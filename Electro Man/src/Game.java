import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Abhinav Ganesh
 * 4/22/19
 * Period 7
 */
public class Game
{
    public static final char CHAR_WALL = 'w';
    public static final char CHAR_POWERUP = 'p';
    public static final char CHAR_SPEEDUP = 's';
    public static final char CHAR_UPLAUNCHER = 'u';
    public static final char CHAR_DOWNLAUNCHER = 'd';
    public static final char CHAR_HOMINGLAUNCHER = 'h';
    // Portals are chars 1-9

    public static BufferedImage wallImage;
    public static BufferedImage speedupImage;
    public static BufferedImage portalImage;
    public static BufferedImage bulletImage;
    public static BufferedImage homingMissileImage;
    public static BufferedImage playerImage;
    public static BufferedImage powerUpImage;
    public static BufferedImage energyBall;
    //public static BufferedImage explosionImage;

    int level;
    ArrayList<Obstacle> Obstacles;
    ArrayList<Player> players;
    ArrayList<Player> deadPlayers;
    Panel panel;
    public static final double acceleration = 2 * 0.100;
    int horizontalVelocity;
    int furthestX;
    Player furthestPlayer;

    public Game(ArrayList<Player> players, Panel p)
    {
        this.players=players;
        //sets the level
        level=0;
        this.panel = p;
        Obstacles=new ArrayList<>();
        deadPlayers = new ArrayList<>();
        horizontalVelocity=2;
        //default 1 player
        /** STILL HAVE TO DO STUFF FOR MORE PLAYERS */
        try {
            playerImage = new Animation(Animation.runningAnimation, Color.GRAY).getAnimationSequence().get(0);
            powerUpImage = ImageIO.read(new File("powerUpTEST.png"));
            wallImage = ImageIO.read( new File("wall.jpeg"));
            speedupImage = ImageIO.read( new File("speedUp2.png"));
            portalImage = ImageIO.read( new File("portal.png"));
            bulletImage = ImageIO.read( new File("bullet.png"));
            homingMissileImage = ImageIO.read( new File("missile.png"));
            energyBall = ImageIO.read( new File("energyBall.png"));
            //explosionImage = ImageIO.read( new File( "explode1.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        levelUpdate();

        for(int i=0;i<players.size();i++)
        {
            /*int red = (int) ((Math.random())*(i+1)*474)%255;
            int blue = (int) ((Math.random())*(i+1)*232)%255;
            int green = (int) ((Math.random())*(i+1)*199)%255;
            Color color = new Color(red,green,blue);
            Player playerOne=new Player(playerImage, color);
            players.add(playerOne);*/
            Player tempPlayer = players.get(i);
            tempPlayer.setYLocation(50*5+tempPlayer.getHeight()*i);
            Obstacles.add(players.get(i));
            //System.out.println(playerOne.color);
        }
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public void levelUpdate()
    {
        if(level==1)
        {
            createLevel(new File("lvl1"));
        }
        else if(level==0)
        {
            createLevel(new File("demoLevel"));
        }
            /*else if(level==2)
            {
                level(new File("LevelTwo.txt"));
            }
            else if(level==0||player.getLives()<=0)
            {
                youLose();
            }
            else
            {
                youWin();
            }*/

    }

    public void createLevel(File file)
    {
        Obstacles.clear();
        /** add players to obstacles list */
        int xLoc=0;
        int yLoc=0;

        levelString lvl=new levelString(file);
        ArrayList<String> stringArrayList=lvl.getStringArr();

        //goes thru txt file
        for(int r=0; r<stringArrayList.size();r++)
        {
            yLoc=r*Wall.wallHeight;
            String row=stringArrayList.get(r);
            for(int c=0;c<row.length();c++)
            {
                xLoc=c*Wall.wallWidth;
                char currChar=row.charAt(c);

                //if its a ... makes new correspondingly
                if(currChar==' ')
                {
                    //doesn't do anything
                }
                else if(currChar==CHAR_WALL)
                {
                    Wall wall=new Wall(new Rectangle(Wall.wallWidth, Wall.wallHeight), wallImage);
                    wall.getRect().setLocation(xLoc,yLoc);
                    Obstacles.add(wall);
                }
                else if(currChar==CHAR_POWERUP)
                {
                    PowerUp powerUp=new PowerUp(new Rectangle(Wall.wallWidth, Wall.wallHeight), powerUpImage);
                    powerUp.getRect().setLocation(xLoc,yLoc);
                    Obstacles.add(powerUp);
                }
                else if(currChar==CHAR_SPEEDUP)
                {
                    PowerUp powerUp=new SpeedUp(new Rectangle(Wall.wallWidth, Wall.wallHeight), speedupImage);
                    powerUp.getRect().setLocation(xLoc,yLoc);
                    Obstacles.add(powerUp);
                }
                else if(currChar>='0'&&currChar<='9')
                {
                    Portal p=new Portal(new Rectangle(Wall.wallWidth, Wall.wallHeight), portalImage, currChar);
                    p.getRect().setLocation(xLoc,yLoc);
                        for(Obstacle o : Obstacles)
                        {
                            if(o instanceof Portal
                            && ((Portal) o).portalChar == currChar)
                            {
                                ((Portal) o).setPartner(p);
                                p.setPartner((Portal) o);
                                break;
                            }
                        }
                    Obstacles.add(p);
                }
                else if(currChar==CHAR_UPLAUNCHER)
                {
                    Launcher upLauncher = new upLauncher(new Rectangle(Wall.wallWidth, Wall.wallHeight),
                            this);
                    upLauncher.getRect().setLocation(xLoc,yLoc);
                    addObstacle(upLauncher);
                }
                else if(currChar==CHAR_DOWNLAUNCHER)
                {
                    Launcher downLauncher = new downLauncher(new Rectangle(Wall.wallWidth, Wall.wallHeight),
                            this);
                    downLauncher.getRect().setLocation(xLoc,yLoc);
                    addObstacle(downLauncher);
                }
                else if(currChar==CHAR_HOMINGLAUNCHER)
                {
                    HomingLauncher homingLauncher = new HomingLauncher(new Rectangle(Wall.wallWidth, Wall.wallHeight),
                            this);
                    homingLauncher.getRect().setLocation(xLoc,yLoc);
                    addObstacle(homingLauncher);
                }
            }
        }
    }

    public void addObstacle(Obstacle o)
    {
        Obstacles.add(o);
    }

    public void paintGame(Graphics g, int worldX, int worldY)
    {
        //
        //go through all obstacles

        for(int i=0; i<Obstacles.size(); i++)
        {
            Obstacle obstacle=Obstacles.get(i);
            //if its x is within bounds and its y, paint
            if(
                    ((obstacle.getX() > worldX &&
                            obstacle.getX() < worldX + panel.getWidth())
                    || (obstacle.getX() + obstacle.getWidth() > worldX &&
                            obstacle.getX() + obstacle.getWidth() < worldX + panel.getWidth()))
                    &&
                            ((obstacle.getY() > worldY &&
                                    obstacle.getY() < worldY + panel.getHeight())
                                    || (obstacle.getY() + obstacle.getHeight() > worldY &&
                                    obstacle.getY() + obstacle.getHeight() < worldY + panel.getHeight()))
            )
            {
                if(obstacle instanceof Launcher && !((Launcher) obstacle).isStarted())
                {
                    ((Launcher) obstacle).start();
                }

                obstacle.printBox(g,
                        obstacle.getX() - worldX,
                        obstacle.getY() - worldY
                );
            }
            else
            {


                //if its a player, he dead
                if(obstacle instanceof Player
                && ((Player) obstacle).isAlive())
                {
                    ((Player) obstacle).setAlive(false);
                    System.out.println(((Player) obstacle).getName() + " is dead");
                    players.remove(obstacle);
                    Obstacles.remove(obstacle);
                    deadPlayers.add((Player) obstacle);
                }
                else if(obstacle instanceof Bullet)
                {
                    Obstacles.remove(obstacle);
                }
                //remove obstacles past x
                else if(obstacle.getX() + obstacle.getWidth() + HomingLauncher.BUFFERZONE< worldX)
                {
                    Obstacles.remove(obstacle);
                }
            }
        }
    }

    public void update()
    {
        //setFurthestPlayer
        furthestPlayer = players.get(0);
        for(int i=1; i<players.size(); i++)
        {
            if(players.get(i).getX() > furthestPlayer.getX())
            {
                furthestPlayer = players.get(i);
            }
        }
        //move all players right if possible
        //gravity and (moveDown)
        //remove player if dead
        for(int i=0; i<players.size(); i++)
        {
            Player currentPlayer = players.get(i);
            currentPlayer.update(this);
        }

        //all bullet collisions
        for(int i=0; i<Obstacles.size(); i++)
        {
            Obstacle o = Obstacles.get(i);
            if(o instanceof Bullet)
            {
                if(((Bullet) o).update(this))
                {
                    i--;
                }
            }
            else if(o instanceof HomingLauncher)
            {
                o.update();
            }
        }

        //find max horiz velocity and go with that
        int maxVel=horizontalVelocity;
        for(int i=0; i<players.size(); i++)
        {
            if(players.get(i).getXVelocity() > maxVel)
            {
                maxVel=players.get(i).getXVelocity();
            }
            if(players.get(i).getX() > furthestX)
            {
                furthestX=players.get(i).getX();
            }
        }
        horizontalVelocity=maxVel;

    }

    public int getHorizontalVelocity()
    {
        return horizontalVelocity;
    }

    /*public void moveDown(Player player)
    {
        /**Code this properly later*/

        /*for(int i=0; i<Obstacles.size(); i++)
        {
            Obstacle currentObstacle=Obstacles.get(i);
            //dont count urself obviously
            //also dont count powerups
            if(!player.equals(currentObstacle)
                    && player.collidesWith(currentObstacle))
            {
                //playerHitsWall();
                player.setYLocation(currentObstacle.getY() - player.getHeight());
                player.setYvelocity(0);
            }
        }
        player.setYLocation(player.getY() + (int) player.getYvelocity());
    }*/

    public ArrayList<Obstacle> getObstacles()
    {
        return Obstacles;
    }

    public void typeKey(char c)
    {

        /**Fix for multiplayer later*/
        //if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {

            for(int i=0;i<players.size();i++)
            {
                players.get(i).switchYDirection();
                System.out.println("hi");
            }
        }
    }

    /*public void moveDown(Player player)
    {
        //if any collision then fix
        for(int i=0; i<Obstacles.size(); i++)
        {
            Obstacle currentObstacle=Obstacles.get(i);
            //dont count urself obviously
            //also dont count powerups
            if(!player.equals(currentObstacle)
            && player.collidesWith(currentObstacle))
            {
                playerHitsWall();
            }
        }
        if(player.collidesWith())
        {
            return;
        }

        player.setYLocation(player.getY() + (int) player.getYvelocity());
    } */
}
