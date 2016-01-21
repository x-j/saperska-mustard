package saperskaMustard;

import java.util.ArrayList;

public class Game {

	//this class sits on the server, contains all the neccesary information about the game in progress
//	also has the chatbox history, catches new messages and sends them to all users
//	also sends information to new players connecting to the lobby

	int numberOfMines;
	public boolean firstClickHappened = false;
	public int gameID;
	private GameInfo info;
	private boolean isOpen;
	//public static ArrayList<Game> ALL_GAMES = new ArrayList<>(); /*number of games is already tracked by the server*/

	boolean [][] mines;

	public Game( GameInfo info ) {
		this.info = info;
		this.gameID = 0000 + MinesweeperThreadedServer.OPEN_GAMES.size(); //server already adds games to an array list, this is redundant
		numberOfMines = (int) ( Math.pow(info.getBoardSize(), 2) * 0.18 );
		mines = new boolean[info.getBoardSize()][info.getBoardSize()];
		isOpen = true;

	}

	public void click( int i, int j ) {   //this method will be called from some outer class, ints i, j come from the user
// it sets up the mines if there arent any, and then shares the information about a click with other players

		if ( !firstClickHappened ) {

			//sets up the mines here.
			for ( int iterator = 0; iterator < numberOfMines; iterator++ ) {
				int rand1;
				int rand2;
				do {
					rand1 = (int) ( Math.random() * info.getBoardSize() );
					rand2 = (int) ( Math.random() * info.getBoardSize() );
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

	public ArrayList<String> getPlayers() {
		return info.getPlayers();
	}

	public String getIpOfHost() {
		return info.getIpAddress();
	}

	public String getUsernameOfHost(){
		return info.getUsernameOfHost();
	}
	
	public int getBoardSize(){
		return info.getBoardSize();
	}

	public GameInfo getInfo() {
		return info;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
}