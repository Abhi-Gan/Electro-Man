import java.awt.*;
import java.util.ArrayList;

public class HomingLauncher extends Obstacle
{
    Game game;
    ArrayList<Player> playersShotAt;
        public static final int BUFFERZONE = 400;
    public HomingLauncher(Rectangle rect, Game game)
    {
        super(rect);
        color=Color.YELLOW;
        this.game=game;
        playersShotAt=new ArrayList<>();
    }

    @Override
    public void update() {
        super.update();
        for(Player p : game.players)
        {
            if(!shotAtPlayer(p)
            && p.getX() >= getX() + getWidth() + BUFFERZONE)
            {
                sendMissile(p);
            }
        }
    }

    private boolean shotAtPlayer(Player p)
    {
        for(Player player: playersShotAt)
        {
            if(player == p)
            {
                return true;
            }
        }
        return false;
    }

    public void sendMissile(Player p)
    {
        game.addObstacle(new HomingMissile(getX()+getWidth(), p.getY(), p, Game.homingMissileImage));
        playersShotAt.add(p);
    }
}
