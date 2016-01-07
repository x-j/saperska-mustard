package saperskaMustard;

import java.util.ArrayList;

public class Game {

	public String usernameOfHost;
	public int numberOfMines;
	public int flagsDeployed;
	public String currentPlayer;
	public int boardSize;
	boolean firstClick = false;
	String ip;
	ArrayList<String>players;
	
	public Game(String usernameOfHost, int boardSize, String ip) {
		this.usernameOfHost = usernameOfHost;
		players.add(usernameOfHost);
		numberOfMines = (int) (Math.pow(boardSize, 2) * 0.18);
		flagsDeployed = 0;
		currentPlayer = usernameOfHost;
		this.boardSize = boardSize;
		this.ip = ip;
	}

	public static void start() {
		
		System.out.println("Waiting for first click!");
		
		
		
	}
	
	
	
}