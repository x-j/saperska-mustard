package saperskaMustard;

import java.io.Serializable;
import java.util.ArrayList;

/*This class is used so we can easily access information needed to initialize a Game object without sending Game object itself*/
public class GameInfo implements Serializable{
    private String username,ipAddress;
    private ArrayList<String> playerList = new ArrayList<>();
    private int boardsize;
    public GameInfo(String userOfHost, String ip, int bsize){
        this.username = userOfHost;
        this.ipAddress = ip;
        this.boardsize = bsize;

    }
    public String getUsername(){return username;}
    public String getIpAddress(){return ipAddress;}
    public int getBoardsize(){return boardsize;}
    public ArrayList<String> getPlayers(){return playerList;}
}
