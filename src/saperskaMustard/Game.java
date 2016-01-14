package saperskaMustard;

import java.util.ArrayList;

public class Game {

	//this class sits on the server, contains all the neccesary information about the game in progress
//	also has the chatbox history, catches new messages and sends them to all users
//	also sends information to new players connecting to the lobby

	public String usernameOfHost;
	int numberOfMines;
	public int boardSize;
	public boolean firstClickHappened = false;
	String ipOfHost;
	private ArrayList<String> players = new ArrayList<>();
	public int gameID;
	//public static ArrayList<Game> ALL_GAMES = new ArrayList<>(); /*number of games is already tracked by the server*/

	boolean[][] mines;

	public Game( GameInfo info) {
		this.usernameOfHost = info.getUsername();
		this.gameID = 0000 + MinesweeperThreadedServer.openGames.size(); //server already adds games to an array list, this is redundant
		players.add(usernameOfHost);
		numberOfMines = (int) ( Math.pow(boardSize, 2) * 0.18 );
		this.boardSize = info.getBoardsize();
		this.ipOfHost = info.getIpAddress();
		mines = new boolean[boardSize][boardSize];

	}

	public void click( int i, int j ) {   //this method will be called from some outer class, ints i, j come from the user
// it sets up the mines if there arent any, and then shares the information about a click with other players
		
		if ( !firstClickHappened ) {

			//sets up the mines here.
			for ( int iterator = 0; iterator < numberOfMines; iterator++ ) {
				int rand1;
				int rand2;
				do {
					rand1 = (int) ( Math.random() * boardSize );
					rand2 = (int) ( Math.random() * boardSize );
				} while ( mines[rand1][rand2] == true || ( rand1 == i && rand2 == j ) );

				mines[rand1][rand2] = true;
			}

			//DEAR FILIP
			//TODO the game is supposed to now send the array mines to all players.
			//the client, upon receiving this array, will activate the createSquares method in class Board
			//resulting in a filled board. clear that point?

		}

		//TODO now, send to information about which square was clicked (so i, j) to all players
		//clients upon receiving these two integers (or put them in an array of size 2, whichever works better)
		//will update their Board via the receiveClick method in class Board
		
		
	}

}
