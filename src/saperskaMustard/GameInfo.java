package saperskaMustard;

import java.io.Serializable;
import java.util.ArrayList;

/*This class is used so we can easily access information needed to initialize a Game object without sending Game object itself*/
public class GameInfo implements Serializable {
    private String usernameOfHost;
    private String ipAddressOfHost;
    private ArrayList<String> playerList = new ArrayList<>();
    private int boardSize;
    private int gameIndex = -1;

    public GameInfo(String userOfHost, String ip, int boardSize) {
        this.usernameOfHost = userOfHost;
        this.ipAddressOfHost = ip;
        this.boardSize = boardSize;
        playerList.add(userOfHost);
    }


    public String getUsernameOfHost() {
        return usernameOfHost;
    }

    public int getGameIndex() {
        return gameIndex;
    }

    public void setGameIndex(int gameIdx) {
        this.gameIndex = gameIdx;
    }

    public String getIpAddress() {
        return ipAddressOfHost;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public ArrayList<String> getPlayers() {
        return playerList;
    }

    @Override
    public String toString() {
        String toReturn = super.toString();
        toReturn += "\nIndex: " + gameIndex + ", Host: " + usernameOfHost + ", BoardSize: " + boardSize + ", Players:";
        for (String aPlayerList : playerList) toReturn += "\n" + aPlayerList;
        return toReturn;
    }


}