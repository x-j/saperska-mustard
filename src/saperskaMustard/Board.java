package saperskaMustard;

import java.util.ArrayList;

public class Board {

	//Board belongs to the client, stores all the necessary information about the board state, the players in the lobby, whose player turn it currently is etc.
//	also recieves information from the server about which squares have been clicked and which not
// also has all the methods necessary for setting up the board (i. e. the filling all the empty squares with numbers)

	SquareButton[][] squares;
	public int numberOfMines = 0;
	int boardSize;
	ArrayList<String> players = new ArrayList<>();
	public String usernameOfHost;
	public boolean gameOver = false;
	public String currentPlayer;
	String clientUsername;
	public TableGUI gui;
    public boolean hasBegun = false;

    public Board( GameInfo info,String clientUsername ) {
		this.boardSize = info.getBoardsize();
		this.usernameOfHost = info.getUsername();
		numberOfMines = (int) ( Math.pow(boardSize, 2) * 0.18 );
        currentPlayer = usernameOfHost;
        //this.clientUsername = clientUsername;
		players.add(clientUsername);
        squares = new SquareButton[boardSize][boardSize];
    }
	
	private ArrayList<Character> getNeighbours( int i, int j ) {

		ArrayList<Character> neighbours = new ArrayList<>();

		i++;
		if ( squareExists(i, j) ) neighbours.add(squares[i][j].content);
		j++;
		if ( squareExists(i, j) ) neighbours.add(squares[i][j].content);
		i--;
		if ( squareExists(i, j) ) neighbours.add(squares[i][j].content);
		i--;
		if ( squareExists(i, j) ) neighbours.add(squares[i][j].content);
		j--;
		if ( squareExists(i, j) ) neighbours.add(squares[i][j].content);
		j--;
		if ( squareExists(i, j) ) neighbours.add(squares[i][j].content);
		i++;
		if ( squareExists(i, j) ) neighbours.add(squares[i][j].content);
		i++;
		if ( squareExists(i, j) ) neighbours.add(squares[i][j].content);

		return neighbours;
	}

	public boolean squareExists( int i, int j ) {
		if ( i < 0 || j < 0 ) return false;
		if ( i >= boardSize || j >= boardSize ) return false;
		return true;
	}

	public void setUpSquares( boolean[][] mines ) {

		for ( int i = 0; i < boardSize; i++ ) {
			for ( int j = 0; j < boardSize; j++ ) {
				if ( !mines[i][j] ) {
					ArrayList<Character> neighbours = getNeighbours(i, j);
					int mineCounter = 0;
					for ( char c : neighbours )
						if ( c == 'm' ) mineCounter++;
					if(mineCounter == 0) squares[i][j].content = ' ';
					else squares[i][j].content = (char) ( mineCounter + '0' );
				}else squares[i][j].content = 'm';
			}
		}

	}
	
	public  void gameStart() {
        hasBegun= true;
		for (SquareButton sB : SquareButton.ALL_SQUAREBUTTONS)
			sB.setEnabled(true);
	}

	public  void notYourTurn(){
		for (SquareButton sB : SquareButton.ALL_SQUAREBUTTONS)
			sB.setEnabled(false);
	}

	public  void yourTurn(){
		for (SquareButton sB : SquareButton.ALL_SQUAREBUTTONS)
			sB.setEnabled(true);
	}

	public void click( int i, int j ) {     //this method gets called by a clicked button.
		//it sends information to the server about coordinates of a button that was clicked.
		//TODO dear Filip, from here send info to the server about i, j

	}

	public void receiveClick(int i, int j ){    //this method gets called somewhere from the Client class
		//after receiving information from the server about a clicked square, we update our local Board

		squares[i][j].reveal();
		if(!gameOver){

			int currentPlayerIndex = players.indexOf(currentPlayer);
			currentPlayerIndex++;
			if(currentPlayerIndex >= players.size()) currentPlayerIndex = 0;

			currentPlayer = players.get(currentPlayerIndex );
			if(currentPlayer.equals(clientUsername)) yourTurn();
			else notYourTurn();

		}



	}
}
