//import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Abhinav Ganesh
 * 4/22/19
 * Period 7
 */
public class Frame extends JFrame
{
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem highScores;
    JMenuItem exit;
    JMenuItem newGame;
    Panel panel;

    public static final int SPACER = 10;

    public Frame(int width, int height)
    {
        super("Electro Man");
        System.out.println("width: "+width+" height: "+height);


        menuBar = new JMenuBar();
        newGame = new JMenuItem("New Game");
        exit = new JMenuItem("Exit");
        highScores = new JMenuItem("High Scores");

        menu = new JMenu("Menu");
        menu.add(newGame);
        //File.add(highScores);
        menu.add(exit);
        menuBar.add(menu);

        setJMenuBar(menuBar);

        newGame.addActionListener(e -> newGame());
        exit.addActionListener(e->exitGame());
        //highScores.addActionListener(e -> displayScores());


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLayout(null);


        setVisible(true);
        setResizable(false);
    }

    public void exitGame()
    {
        //saveScore();
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public void newGame()
    {
        if(panel != null)
        {
            remove(panel);
        }
        panel=new Panel(getWidth(), getHeight(), findPlayers());
        panel.setBounds(0,0,getWidth(), getHeight());
        add(panel);
    }

    public int numPlayers()
    {
        String[] possibleValues = {"1", "2", "3", "4"};
        Object selectedValue = JOptionPane.showInputDialog(null,
                "How many players?", "New Game",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);

        return Integer.parseInt((String) selectedValue);
    }

    public ArrayList<Player> findPlayers()
    {
        int numPlayers = numPlayers();
        ArrayList<Player> players=new ArrayList<>();
        for(int i=0; i<numPlayers; i++)
        {
            //find names
            String name = JOptionPane.showInputDialog("Please enter player "+(i+1)+"'s name: ");
            while(name == null || name.length() ==0)
            {
                name = JOptionPane.showInputDialog("Invalid String; \n Please enter player "+(i+1)+"'s name: ");
            }
            //find gravity char
            String charString = JOptionPane.showInputDialog("Please enter player "+(i+1)+"'s control button: ");
            while(charString == null ||
                    (charString.length()>1 || charString.length() ==0))
            {
                charString = JOptionPane.showInputDialog("Invalid String; \n Please enter player "+(i+1)+"'s control button: ");
            }
            char gravityChar = charString.charAt(0);
            //find shooting char
            charString = JOptionPane.showInputDialog("Please enter player "+(i+1)+"'s shooting button: ");
            while(charString == null ||
                    (charString.length()>1 || charString.length() ==0))
            {
                charString = JOptionPane.showInputDialog("Invalid String; \n Please enter player "+(i+1)+"'s shooting button: ");
            }
            char shootChar = charString.charAt(0);
            //show color?
            Color choiceColor = JColorChooser.showDialog(null, "Select the player's color:", Color.LIGHT_GRAY);

            Player tempPlayer = new Player(new Animation(Animation.runningAnimation, Color.GRAY).getAnimationSequence().get(0), choiceColor);
            tempPlayer.setName(name);
            tempPlayer.setGravityChar(gravityChar);
            tempPlayer.setShootChar(shootChar);
            players.add(tempPlayer);
        }

        return players;
    }
}
