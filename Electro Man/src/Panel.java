import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.management.PlatformLoggingMXBean;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;

/**
 * Abhinav Ganesh
 * 4/22/19
 * Period 7
 */
public class Panel extends JPanel implements Runnable, KeyListener, MouseListener
{
    private int worldX, worldY;
    public static final int UPS = 100;
    private double timeBetweenUpdates =1000.0/UPS;
    private BufferedImage buffer;
    private BufferedImage background;
    private BufferedImage gameOverImage;
    boolean gameOver;
    Game game;
    ArrayList<Character> charactersPressed;
    BufferedImage crown;

    public Panel(int width, int height, ArrayList<Player> players)
    {
        charactersPressed = new ArrayList<>();
        worldX=worldY=0;
        setSize(width, height);
        addKeyListener(this);
        addMouseListener(this);
        buffer=new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        //get number of players
        game=new Game(players, this);
        try
        {
            background = ImageTools.darken(ImageIO.read(new File("moonBackground.png")), .23);
            gameOverImage = ImageIO.read(new File("GameOver.png"));
            crown = ImageIO.read(new File("crown.png"));
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
        Thread t=new Thread(this);
        t.start();
        setVisible(true);
    }

    @Override
    public void paint(Graphics g)
    {

        Graphics bg=buffer.getGraphics();

        if(gameOver)
        {
            ArrayList<Player> deadPlayers = game.deadPlayers;
            bg.setColor(Color.BLACK);
            bg.fillRect(0,0,getWidth(),getHeight());
            bg.drawImage(gameOverImage,
                    0, 0,
                    getWidth(), getHeight()/2,
                    0,0,
                    gameOverImage.getWidth(), gameOverImage.getHeight(),
                    null
                    );

            int numPlayers = deadPlayers.size();
            //sort
            /*for(int i=0; i<numPlayers; i++)
            {
                Player a = deadPlayers.get(i);
                for(int k=i+1; k<numPlayers; k++)
                {
                    Player b = deadPlayers.get(k);
                    if(a.getX() > b.getX())
                    {
                        deadPlayers.set(k, a);
                        deadPlayers.set(i, b);
                    }
                }
            }*
            //draw player's heads and their ranks
            for(int i=0; i<numPlayers; i++)
            {
                int xLoc = i* getWidth()/numPlayers;
                Player p=deadPlayers.get(i);
                p.setYDirection(1);
                BufferedImage head = ImageTools.recolor(p.getImage(), deadPlayers.get(i).color).getSubimage(35, 0, 85, 65);
                bg.drawImage(head, xLoc, getHeight()/2 +30, null);
                //name
                bg.setColor(Color.WHITE);
                bg.setFont(new Font("custom", Font.BOLD, 23));
                bg.drawString(deadPlayers.get(i).getName(), xLoc,
                        getHeight()/2 +30 + 65 + 30);

                //draw Crown on winner
                if(i == numPlayers - 1)
                {
                    crown = ImageTools.scale(crown, 65, 65);
                    bg.drawImage(crown, xLoc,
                            getHeight()/2 + 30 - 65, null);
                }
            }*/
            //print furthest player
            Player winner = game.furthestPlayer;
            winner.setYDirection(1);
//           BufferedImage head = ImageTools.recolor(winner.getImage(), winner.color);
    //        bg.drawImage(head, getWidth()/2 -60, getHeight()/2 +30, null);

            winner.printBox( bg,getWidth()/2 -60, getHeight()/2 +30);
            //name
            bg.setColor(Color.WHITE);
            bg.setFont(new Font("custom", Font.BOLD, 23));
            bg.drawString(winner.getName()+ " wins!", getWidth()/2 - (winner.getName().length()+6)/2 -35,
                    getHeight()/2 +30 + winner.getImage().getHeight() + 30);
            //score
            bg.drawString("Score: "+winner.getX(), getWidth()/2 - (winner.getName().length()+6)/2 -35,
                    getHeight()/2 +30 + winner.getImage().getHeight() + 30 + 30);
            //draw Crown on winner
            crown = ImageTools.scale(crown, 65, 65);
            bg.drawImage(crown, getWidth()/2 -10,
                    getHeight()/2 + 30 -50, null);
        }
        else {

            int backgroundXlocation= 0-(worldX%background.getWidth());
            bg.drawImage( background,
                    backgroundXlocation, 0, null);
            //if the camera's x+width is greater than the background's x+width
            if(getWidth() > backgroundXlocation + background.getWidth())
            {
                //draw image next to it
                bg.drawImage( background,
                        backgroundXlocation + background.getWidth(), 0, null);
            }
            game.paintGame(bg, worldX, worldY);
        }

        g.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void run()
    {
        long startTime=System.nanoTime();
        long updatesMade=0;
        while (true)
        {
            boolean needsRepaint=false;
            long updatesNeeded= (long) ((System.nanoTime()-startTime)/1000000/timeBetweenUpdates);

            for(;updatesMade<updatesNeeded;updatesMade++)
            {
                update();
                needsRepaint=true;
            }

            if(needsRepaint)
                repaint();

            try
            {
                Thread.sleep((int) timeBetweenUpdates);
            }
            catch (Exception e)
            {

            }
        }
    }

    public void update()
    {
        if(game.players.size() <1)
        {
            //endgame options
            gameOver=true;
        }
        else
        {
            game.update();
            worldX=game.furthestX - getWidth()/2;
            worldX+=game.getHorizontalVelocity();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyChar());
        char typedChar = e.getKeyChar();

        if(charactersPressed.contains(typedChar))
        {
            //dont do anything
        }
        else
        {
            charactersPressed.add(typedChar);
            /*if(gameOver && (typedChar == 'r' || typedChar == 'R'))
            {
                game=new Game(howManyPlayers(), this);
                game.players.clear();
                for(int p=0;p<game.players.size();p++)
                {
                    game.players.get(p).setName(findNames(p));
                    game.players.get(p).setGravityChar(findGravityChar(p));
                }
                return;
            }*/

            for(int p=0;p<game.players.size();p++)
            {
                if(game.players.get(p).getGravityChar() == typedChar
                        //and if its colliding vert w/ anything
                        //&& game.players.get(p).canJump()
                /**FIX Can jump*/
                )
                {
                    //System.out.println("Flipping direction for Player " + game.players.get(p).getName());
                    game.players.get(p).switchYDirection();
                }
                if(game.players.get(p).getShootChar() == typedChar)
                {
                    game.players.get(p).shoot(game);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        charactersPressed.remove((Character) e.getKeyChar());
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        /**fix later*/
        /*System.out.println("hi2");
        game.typeKey();*/
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void addNotify()
    {
        super.addNotify();
        requestFocus();
    }

    /*public int howManyPlayers()
    {
        int howManyPlayer = 0;
        Scanner keyboard=new Scanner(System.in);

        while( howManyPlayer<=0 || howManyPlayer > 4)
        {
            System.out.print("How many players?");
            howManyPlayer=Integer.parseInt(keyboard.next());
        }

        return howManyPlayer;
    }

    public char findGravityChar(int i)
    {
        //char gravityChar = 0;
        Scanner keyboard=new Scanner(System.in);
        System.out.print("Enter player "+(game.players.get(i).getName())+"'s gravity switch: ");
        char gravityChar = keyboard.next().charAt(0);

        return gravityChar;
    }

    public String findNames(int i)
    {
        Scanner keyboard=new Scanner(System.in);
        System.out.print("Enter player "+(i+1)+"'s name: ");
        String name = keyboard.next();

        return name;
    }*/
}
