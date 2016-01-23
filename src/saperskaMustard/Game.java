package saperskaMustard;

import java.util.ArrayList;

public class Game {

    //this class sits on the server, contains all the neccesary information about the game in progress
//	also has the chatbox history, catches new messages and sends them to all users
//	also sends information to new players connecting to the lobby

    public boolean firstClickHappened = false;
    boolean[][] mines;
    MinesweeperThreadedServer server;
    private int numberOfMines;
    private int gameIndex;
    private GameInfo info;
    private boolean isOpen;

    public Game(GameInfo info, MinesweeperThreadedServer server) {
        this.server = server;
        this.info = info;
        this.gameIndex = MinesweeperThreadedServer.INDEXER; //server already adds games to an array list, this is redundant
        numberOfMines = (int) (Math.pow(getBoardSize(), 2) * 0.18);
        mines = new boolean[getBoardSize()][getBoardSize()];
        isOpen = true;
        info.setGameIndex(gameIndex);
    }

    public void click(int i, int j) {   //this method will be called from some outer class, ints i, j come from the user
// it sets up the mines if there arent any, and then shares the information about a makeClick with other players
        if (!firstClickHappened) {
            firstClickHappened = true;
            //sets up the mines here.
            for (int iterator = 0; iterator < numberOfMines; iterator++) {
                int rand1;
                int rand2;
                do {
                    rand1 = (int) (Math.random() * info.getBoardSize());
                    rand2 = (int) (Math.random() * info.getBoardSize());
                } while (mines[rand1][rand2] == true || (rand1 == i && rand2 == j));

                mines[rand1][rand2] = true;
            }

            server.sendToGame(mines, gameIndex);

        }

        int[] coordinates = {i, j};
        server.sendToGame(coordinates, gameIndex);

        //clients upon receiving these two integers (or put them in an array of size 2, whichever works better)
        //will update their Board via the receiveClick method in class Board


    }

    public void addPlayer(String usernameOfPlayer) {

        System.out.println("a player: " + usernameOfPlayer + " connected to game " + getIndex());
        info.getPlayers().add(usernameOfPlayer);
        if (info.getPlayers().size() == 4) {
            isOpen = false;
            System.out.println("A game of index " + getIndex() + " was filled.");
        }

    }

    public void removePlayer(String usernameOfPlayer) {

        System.out.println("removing " + usernameOfPlayer + " from game " + getIndex());
        getPlayers().remove(usernameOfPlayer);
        isOpen = true;
        if (getPlayers().size() == 0) {
            System.out.println("all players left game " + getIndex());
        }


    }

    public ArrayList<String> getPlayers() {
        return info.getPlayers();
    }

    public String getIpOfHost() {
        return info.getIpAddress();
    }

    public String getUsernameOfHost() {
        return info.getUsernameOfHost();
    }

    public int getBoardSize() {
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

    public int getIndex() {
        return info.getGameIndex();
    }
}