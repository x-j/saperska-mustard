package saperskaMustard;

import java.io.Serializable;
import java.util.ArrayList;

/*This class is used so we can easily access information needed to initialize a Game object without sending Game object itself*/
public class GameInfo implements Serializable {
	private String usernameOfHost;
	private String ipAddressOfHost;
	private ArrayList<String> playerList = new ArrayList<>();
	private int boardSize;
	private int gameID;

	public GameInfo(String userOfHost, String ip, int bsize, int gameIdx) {
		this.usernameOfHost = userOfHost;
		this.ipAddressOfHost = ip;
		this.boardSize = bsize;
		playerList.add(userOfHost);
		//this.gameIndex = gameIdx;

	}

	
	public String getUsernameOfHost() {
		return usernameOfHost;
	}

	public int getGameIndex() {
		return gameID;
	}

	public String getIpAddress() {
		return ipAddressOfHost;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setGameIndex(int gameIdx) {
		gameID = gameIdx;
	}

	public ArrayList<String> getPlayers() {
		return playerList;
	}
}
