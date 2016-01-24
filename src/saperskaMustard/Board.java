package saperskaMustard;

import javax.swing.*;
import java.util.ArrayList;

public class Board {

    public static final int MINE = 10;
    public static final int FLAG = 9;

    // Board belongs to the client, stores all the necessary information about
    // the board state, the players in the lobby, whose player turn it currently is etc.
    // also has all the methods necessary for setting up the board (i. e. the
    // filling all the empty squares with numbers)

    private int numberOfMines = 0;
    private String usernameOfHost;
    private boolean gameOver = false;
    private String currentPlayer;
    private TableGUI gui;
    private boolean hasBegun = false;
    private SquareButton[][] squares;
    private int boardSize;
    private ArrayList<String> players = new ArrayList<>();
    private String clientUsername;
    private Client connection;
    private int currentPlayerIndex;

    public Board(GameInfo info, String username, Client client, boolean isHost) {
        this.currentPlayerIndex = 0;
        this.connection = client;
        this.boardSize = info.getBoardSize();
        this.usernameOfHost = info.getUsernameOfHost();
        numberOfMines = (int) (Math.pow(boardSize, 2) * 0.18);
        currentPlayer = usernameOfHost;
        this.clientUsername = username;
        this.players = info.getPlayers();
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
            for (int j = 0; j < boardSize; j++) if (mines[i][j]) squares[i][j].setContent(MINE);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!mines[i][j]) {
                    ArrayList<SquareButton> neighbours = getNeighbours(i, j);
                    int mineCounter = 0;
                    for (SquareButton sb : neighbours) {
                        if (sb.getContent() == MINE)
                            mineCounter++;
                    }
                    if (mineCounter == 0)
                        squares[i][j].setContent(0);
                    else
                        squares[i][j].setContent(mineCounter);
                }
            }
        }
        if (!hasBegun) gameStart();
    }

    public void gameStart() {
        //ALL THIS METHOD DOES IS ENABLE THE SQUARES
        if (!hasBegun) {
            gui.getWhosePlayerTurnItIsLabel().setText(usernameOfHost + "'s turn");
            hasBegun = true;
            for (SquareButton sB : SquareButton.ALL_SQUAREBUTTONS)
                sB.setEnabled(true);
        }
    } //enables the buttons, get called after we set up the squares.

    public void notYourTurn() {
        for (SquareButton sB : SquareButton.ALL_SQUAREBUTTONS)
            sB.setEnabled(false);
    }

    public void yourTurn() {
        for (SquareButton sB : SquareButton.ALL_SQUAREBUTTONS)
            if (!sB.isUncovered() && !sB.isFlagged())
                sB.setEnabled(true);
    }

    public void makeClick(int i, int j) { // this method gets called by a clicked button.
        // it sends information to the server about coordinates of a button that was clicked.
        int[] arr = {i, j};
        connection.send(arr);

    }

    public void receiveClick(int i, int j) { // this method gets called somewhere from the Client class

        // after receiving information from the server about a clicked square,
        // we update our local Board

        squares[i][j].reveal();
        if (!gameOver) {
            System.out.println("The client: " + clientUsername + " has the following players in his array list: ");
            for (String s : players)
                System.out.println(s);

            currentPlayerIndex++;
            currentPlayerIndex %= players.size();
            currentPlayer = players.get(currentPlayerIndex);

            if (currentPlayer.equals(clientUsername))
                yourTurn();
            else
                notYourTurn();
        }
        // we unfortunately have to check if the game is over or not
        //this means iterating through all the squares to find if there are any non-mine fields left uncovered:
        if (checkForVictory()) {
            gameOver = true;
            JOptionPane.showMessageDialog(gui, "Victory!", "Saperska Mustard", JOptionPane.INFORMATION_MESSAGE);
            gui.getStatusIcon().setText("VICTORY");
        }


    }

    private boolean checkForVictory() {
        //worst case time complexity is of order n^2 ... can it be done more efficiently?
        //incidentally, the closer the user is to victory, the longer the execution of this method will take.
        for (SquareButton sb : SquareButton.ALL_SQUAREBUTTONS)
            if (sb.getContent() != MINE && sb.isUncovered()) return false;
        return true;
    }

    public void updateBoard(GameInfo gi) {
        players = gi.getPlayers();
        //TODO not so fast, we have to update CurrentPlayer and CurrentPlayerIndex so game can continue even when someone bails out (if someone disconnects)
    }   //deceptively simple method, gets called by Client, tells us about the changed number of players in the game.

    public void uncoverEmptyAdjacent(int i, int j) {


        if (squares[i][j].getContent() == 0) {
            ArrayList<SquareButton> neighbours = getNeighbours(i, j);
            for (SquareButton sb : neighbours) {
                if (!sb.isUncovered() && sb.getContent() != MINE && !sb.isFlagged()) {
                    sb.reveal();
                    uncoverEmptyAdjacent(sb.getI(), sb.getJ());
                }
            }
        }

    }   //nice recursive formula for uncovering all neighbouring empty squares, should we reveal a square with content == 0

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getUsernameOfHost() {
        return usernameOfHost;
    }

    public TableGUI getGui() {
        return gui;
    }

    public void setGui(TableGUI gui) {
        this.gui = gui;
    }

    public SquareButton[][] getSquares() {
        return squares;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public Client getConnection() {
        return connection;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean hasBegun() {
        return hasBegun;
    }

    public String getNextPlayerString() {
        return getPlayers().get(currentPlayerIndex);//also possible is return currentPlayer;
    }
}
