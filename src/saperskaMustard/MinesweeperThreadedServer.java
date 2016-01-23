package saperskaMustard;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Filip Matracki on 1/7/2016.
 */
public class MinesweeperThreadedServer {
    public static final int PORT = 5000;//must be the same as the variable PORT in TheFrameInWhichYouCreateANewTable.java and where join random game button is
    private static final int USER_DISCONNECTED_SIGNAL = 2;

    public static int INDEXER = 0;  //helps us index games so that their indexes dont overlap

    public static HashMap<Integer, Game> ALL_GAMES = new HashMap<>();

    ServerGUI sGUI;
    private ArrayList<ConnectionToClient> clientList;
    //private LinkedBlockingQueue<Object> receivedObjects; // rip you glorious queue
    private ServerSocket serverSocket;

    public MinesweeperThreadedServer(int port) throws IOException {
        clientList = new ArrayList<>();
        //receivedObjects = new LinkedBlockingQueue<Object>();
        serverSocket = new ServerSocket(port);
        sGUI = new ServerGUI(this);
        status("Server established!");
        status("Waiting for clients to join...");

        Thread waitingForClientsToConnect = new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("Waiting for a client to connect on port " + PORT);
                        Socket clientsSocket = serverSocket.accept();
                        clientList.add(new ConnectionToClient(clientsSocket));

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(sGUI, "An error occurred: ", "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        waitingForClientsToConnect.setDaemon(true);
        waitingForClientsToConnect.start();

        try {
            waitingForClientsToConnect.join(); //wait for threads to finish (they won't finish since they have infinite loops, but we need this so program won't exit immediately)
        } catch (Exception e) {
            JOptionPane.showMessageDialog(sGUI, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void main(String[] args) {
        try {
            System.out.println("About to start server");
            MinesweeperThreadedServer server = new MinesweeperThreadedServer(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void status(String status) {

        if (status.contains("Nowinski")) for (int i = 0; i < 500; i++)
            sGUI.addStatus("CLEAR THAT POINT");
        sGUI.addStatus(status);

    }

    public Game getGame(int gameIndex) {
        return ALL_GAMES.get(gameIndex);
    }

    public void sendToOne(int index, Object message) throws IndexOutOfBoundsException {
        clientList.get(index).write(message);
    }

    public void sendToGame(Object obj, int gameIndex) {
        for (ConnectionToClient c : clientList)
            if (c.getGameIndex() == gameIndex) c.write(obj);
    }

    public void sendToAll(Object message) {
        status((String) message);
        for (ConnectionToClient client : clientList)
            client.write(message);
    }

    private class ConnectionToClient {
        ObjectInputStream inputFromClient;
        ObjectOutputStream outputToClient;
        Socket socket;

        private int gameIndex; // keeps track of which game this client belongs to

        public ConnectionToClient(Socket socket) throws IOException {
            this.socket = socket;
            outputToClient = new ObjectOutputStream(socket.getOutputStream());
            inputFromClient = new ObjectInputStream(socket.getInputStream());

            status("A client has connected. We now have " + clientList.size() + " clients connected.");

            //THIS THREAD BELOW HANDLES OBJECT FROM A CLIENT!
            Thread handleObjectFromClient = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Object obj = inputFromClient.readObject();
                            System.out.println("An object was read from a client.");

                            //IF WE RECEIVED GAMEINFO, THEN WE CREATE A NEW GAME:

                            if (obj instanceof GameInfo) {
                                //We received a request to start a new game from a client
                                GameInfo info = ((GameInfo) obj);
                                Game newGame = new Game(info, MinesweeperThreadedServer.this);
                                INDEXER++;
                                ALL_GAMES.put(gameIndex, newGame);
                                ConnectionToClient.this.gameIndex = newGame.getIndex();
                                System.out.println("w00t we received a request to host a game from a client and server successfully created game");
                                status("Game " + info.getGameIndex() + " created. Host: " + info.getUsernameOfHost() + "; BoardSize: " + info.getBoardSize());
                            }

                            //IF THE RECEIVED OBJECT IS A STRING, IT CAN BE EITHER A NEW PLAYER, OR A NEW MESSAGE!

                            else if (obj instanceof String) {
                                if (((String) obj).startsWith("@")) {

                                    //THIS MEANS WE GET A NEW PLAYER WANTING TO JOIN A RAND GAME

                                    String newPlayer = (String) obj;
                                    newPlayer = newPlayer.substring(1);
                                    status(newPlayer + " connected to the server!");
                                    Game game;
                                    do {
                                        ConnectionToClient.this.gameIndex = (int) (Math.random() * ALL_GAMES.size()); //we randomize a game for him here
                                        game = ALL_GAMES.get(ConnectionToClient.this.gameIndex);
                                    } while (!game.isOpen());

                                    System.out.println("It seems like a client has joined an existing game");//we check if the game is already filled
                                    status(newPlayer + " connected to game " + game.getIndex() + " whose host is " + game.getUsernameOfHost());
                                    if (game.getPlayers().contains(newPlayer))
                                        newPlayer += "1";  //this line right here gets rid of the awkwardness of having two players with the same username in-game
                                    game.addPlayer(newPlayer);   //if not, we add him!
                                    GameInfo info = game.getInfo();
                                    outputToClient.writeObject(info);
                                    sendToGame("SERVER> " + newPlayer + " connected to this game!", getGameIndex());
                                }

                                //IF THE STRING DID NOT START WITH '@', THEN ITS A CHAT MESSAGE, SO WE SEND IT TO ALL PLAYERS IN THIS GAME
                                else {
                                    status((String) obj);
                                    sendToGame(obj, getGameIndex());
                                }
                            }

                            //IF WE RECIEVED AN ARRAY OF INTS, THEN WE SEND THESE COORDS TO ALL USERS IN A GAME:

                            else if (obj instanceof int[]) {
                                int[] coordinates = (int[]) obj;
                                status("Game " + getGameIndex() + " received a click at " + coordinates[0] + ", " + coordinates[1]);
                                getGame(getGameIndex()).click(coordinates[0], coordinates[1]);
                            }

                            //IF WE RECEIVED A SINGLE INT, THEN IT WAS SOME SORT OF "SIGNAL", SO WE HANDLE IT APPROPIATELY:
                            else if (obj instanceof Integer) {

                                int signal = (int) obj;
                                if (signal == USER_DISCONNECTED_SIGNAL) {
                                    //remove user from the game and....
                                }
                            }

                        } catch (IOException e) {
                            status("Closing the connection with a user.");
                            closeConnections();
                            try {
                                this.join();
                            } catch (InterruptedException e1) {
                                JOptionPane.showMessageDialog(sGUI, "An error occurred on disconneting." + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                                e1.printStackTrace();
                            }
                        } catch (ClassNotFoundException e) {
                            JOptionPane.showMessageDialog(sGUI, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    }
                }
            };

            handleObjectFromClient.setDaemon(true); // terminate when main ends
            handleObjectFromClient.start();

        }

        public int getGameIndex() {
            return this.gameIndex;
        }

        public void setGameIndex(int gameIndex) {
            this.gameIndex = gameIndex;
        }

        public void write(Object obj) {
            try {
                outputToClient.writeObject(obj);
            } catch (IOException e) {
                e.printStackTrace();
                //this.closeConnections();
            }
        }

        public void closeConnections() {
            try {
                outputToClient.close();
                inputFromClient.close();
                this.socket.close();
                System.out.println("Shutting down server-side connections to client, and closing socket because client disconnected");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(sGUI, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}


