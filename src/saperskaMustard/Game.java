package saperskaMustard;

import java.util.ArrayList;

public class Game {

    //this class sits on the MinesweeperThreadedServer, contains all the neccesary information about the game in progress
//	also has the chatbox history, catches new messages and sends them to all users
//	also sends information to new players connecting to the lobby

    public boolean firstClickHappened = false;
    boolean[][] mines;
    private int numberOfMines;
    private int gameIndex;
    private GameInfo info;
    private boolean isOpen;

    public Game(GameInfo info) {
        this.info = info;
        this.gameIndex = MinesweeperThreadedServer.INDEXER++;
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

            MinesweeperThreadedServer.sendToGame(mines, gameIndex);//send mines to all other players in game

        }

        int[] coordinates = {i, j};
        MinesweeperThreadedServer.sendToGame(coordinates, gameIndex);

        //clients upon receiving these two integers (or put them in an array of size 2, whichever works better)
        //will update their Board via the receiveClick method in class Board


    }

    public void addPlayer(String usernameOfPlayer) {

        if (getPlayers().contains(usernameOfPlayer))
            usernameOfPlayer += "1";  //this line right here gets rid of the awkwardness of having two players with the same username in-game
        System.out.println("a player: " + usernameOfPlayer + " connected to game " + getIndex());
        info.getPlayers().add(usernameOfPlayer);
        if (info.getPlayers().size() == 4) {
            isOpen = false;
            System.out.println("A game of index " + getIndex() + " was filled.");
        }

    }

    public void removePlayer(String usernameOfPlayer) {

        getPlayers().remove(usernameOfPlayer);
        isOpen = true;
        MinesweeperThreadedServer.status(usernameOfPlayer + " left game " + gameIndex);
        if (usernameOfPlayer.equals(getUsernameOfHost()) && !firstClickHappened) {
            MinesweeperThreadedServer.status("Host left the game " + gameIndex + " before the game started! Removing it from MinesweeperThreadedServer.");
            MinesweeperThreadedServer.ALL_GAMES.remove(gameIndex);
        }
        if (getPlayers().size() == 0) {
            MinesweeperThreadedServer.status("All players left game " + gameIndex + "! Removing it from MinesweeperThreadedServer.");
            MinesweeperThreadedServer.ALL_GAMES.remove(gameIndex);
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