package saperskaMustard;

import java.util.ArrayList;

public class Game {

	//this class sits on the server, contains all the neccesary information about the game in progress
//	also has the chatbox history, catches new messages and sends them to all users
//	also sends information to new players connecting to the lobby

	public String usernameOfHost;
	int numberOfMines;
	public int boardSize;
	boolean firstClickHappened = false;
	String ipOfHost;
	ArrayList<String> players = new ArrayList<>();
	public int gameID;
	public static ArrayList<Game> ALL_GAMES = new ArrayList<>();

	boolean[][] mines;

	public Game( String usernameOfHost, int boardSize, String ip ) {
		this.usernameOfHost = usernameOfHost;
		this.gameID = 0000 + ALL_GAMES.size();
		players.add(usernameOfHost);
		numberOfMines = (int) ( Math.pow(boardSize, 2) * 0.18 );
		this.boardSize = boardSize;
		this.ipOfHost = ip;
		mines = new boolean[boardSize][boardSize];
		ALL_GAMES.add(this);
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
