package saperskaMustard;

import java.util.ArrayList;

public class Board {

    public static final int MINE = 10;
    public static final int FLAG = 9;
    private static final Object REQUEST_FOR_MINES = 2;

    // Board belongs to the client, stores all the necessary information about
    // the board state, the players in the lobby, whose player turn it currently
    // is etc.
    // also recieves information from the server about which squares have been
    // clicked and which not
    // also has all the methods necessary for setting up the board (i. e. the
    // filling all the empty squares with numbers)
    public int numberOfMines = 0;
    public String usernameOfHost;
    public boolean gameOver = false;
    public String currentPlayer;
    public TableGUI gui;
    public boolean hasBegun = false;
    SquareButton[][] squares;
    int boardSize;
    ArrayList<String> players = new ArrayList<>();
    String clientUsername;
    Client connection;

    /* The constructor below is for clients */
    public Board(GameInfo info, String username, Client client) {
        this.connection = client;
        this.boardSize = info.getBoardSize();
        this.usernameOfHost = info.getUsernameOfHost();
        numberOfMines = (int) (Math.pow(boardSize, 2) * 0.18);
        currentPlayer = info.getUsernameOfHost();
        this.clientUsername = username;
        players.add(clientUsername);
        squares = new SquareButton[boardSize][boardSize];
        SquareButton.setUpIcons();

    }

    /* The constructor below is for hosts */
    public Board(GameInfo info, Client client) {
        this.connection = client;
        this.boardSize = info.getBoardSize();
        this.usernameOfHost = info.getUsernameOfHost();
        numberOfMines = (int) (Math.pow(boardSize, 2) * 0.18);
        currentPlayer = usernameOfHost;
        this.clientUsername = info.getUsernameOfHost();
        players.add(clientUsername);
        squares = new SquareButton[boardSize][boardSize];
        SquareButton.setUpIcons();
    }

    private ArrayList<SquareButton> getNeighbours(int i, int j) {

        ArrayList<SquareButton> neighbours = new ArrayList<>();

        i++;
        if (squareExists(i, j))
            neighbours.add(squares[i][j]);
        j++;
        if (squareExists(i, j))
            neighbours.add(squares[i][j]);
        i--;
        if (squareExists(i, j))
            neighbours.add(squares[i][j]);
        i--;
        if (squareExists(i, j))
            neighbours.add(squares[i][j]);
        j--;
        if (squareExists(i, j))
            neighbours.add(squares[i][j]);
        j--;
        if (squareExists(i, j))
            neighbours.add(squares[i][j]);
        i++;
        if (squareExists(i, j))
            neighbours.add(squares[i][j]);
        i++;
        if (squareExists(i, j))
            neighbours.add(squares[i][j]);

        return neighbours;
    }

    public boolean squareExists(int i, int j) {
        if (i < 0 || j < 0)
            return false;
        return !(i >= boardSize || j >= boardSize);
    }

    public void setUpSquares(boolean[][] mines) {

        System.out.println("setting up squares now.");

        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++) if (mines[i][j]) squares[i][j].content = MINE;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!mines[i][j]) {
                    ArrayList<SquareButton> neighbours = getNeighbours(i, j);
                    int mineCounter = 0;
                    for (SquareButton sb : neighbours) {
                        if (sb.content == MINE)
                            mineCounter++;
                    }
                    if (mineCounter == 0)
                        squares[i][j].content = 0;
                    else
                        squares[i][j].content = mineCounter;
                }
            }
        }
        if (!hasBegun) gameStart();
    }

    public void gameStart() {
        //ALL THIS METHOD DOES IS ENABLE THE SQUARES
        if (!hasBegun) {
            hasBegun = true;
            for (SquareButton sB : SquareButton.ALL_SQUAREBUTTONS)
                sB.setEnabled(true);
        }
    } //this method gets called by the GUI to indicate that we want to get them squares. OR BY A CLIENT TO INDICATE THAT WE GOT THEM MINES

    public void notYourTurn() {
        for (SquareButton sB : SquareButton.ALL_SQUAREBUTTONS)
            sB.setEnabled(false);
    }

    public void yourTurn() {
        for (SquareButton sB : SquareButton.ALL_SQUAREBUTTONS)
            if (!sB.uncovered && !sB.flagged)
                sB.setEnabled(true);
    }

    public void makeClick(int i, int j) { // this method gets called by a clicked button.
        // it sends information to the server about coordinates of a button that
        // was clicked.
        int[] arr = {i, j};
        connection.send(arr);

    }

    public void receiveClick(int i, int j) { // this method gets called somewhere from the Client class

        // after receiving information from the server about a clicked square,
        // we update our local Board

        //chatbox is updated in Client.java

        squares[i][j].reveal();
        if (!gameOver) {

            int currentPlayerIndex = players.indexOf(currentPlayer);
            currentPlayerIndex++;
            if (currentPlayerIndex >= players.size())
                currentPlayerIndex = 0;

            currentPlayer = players.get(currentPlayerIndex);
            if (currentPlayer.equals(clientUsername))
                yourTurn();
            else
                notYourTurn();

        }

    }

    public void updateBoard(GameInfo gi) {

        players = gi.getPlayers();

    }

    public void uncoverEmptyAdjacent(int i, int j) {


        if (squares[i][j].content == 0) {
            ArrayList<SquareButton> neighbours = getNeighbours(i, j);
            for (SquareButton sb : neighbours) {
                if (!sb.uncovered && sb.content != MINE && !sb.flagged) {
                    sb.reveal();

                    uncoverEmptyAdjacent(sb.i, sb.j);
                }
            }
        }

    }

    public void receiveMessage(String s) {

    }

}