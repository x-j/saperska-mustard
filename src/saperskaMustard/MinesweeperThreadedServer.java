package saperskaMustard;

import javax.swing.*;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class MinesweeperThreadedServer {

    private static final int USER_DISCONNECTED_SIGNAL = 2;

    public static int port; //WILL BE READ FROM config.xml file. is  the same as the variable port in TheFrameInWhichYouCreateANewTable.java and wherever join random game button is
    public static int INDEXER = 0;  //helps us index games so that their indexes dont overlap
    public static HashMap<Integer, Game> ALL_GAMES = new HashMap<>();   //stores all games present on server, keys are gameIndexii.
    private static ArrayList<ConnectionToClient> ALL_CLIENTS = new ArrayList<>();   //stores all connections to client, obviously

    private static ServerGUI sGUI;  //the GUI of the server
    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        readConfig();   //reads info from the config.xml file (also assigns port)

        try {

            System.out.println("About to start server");
            try {
                serverSocket = new ServerSocket(port);
            } catch (BindException e) {
                JOptionPane.showMessageDialog(sGUI, "Error: this port is already in use.", "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            sGUI = new ServerGUI();
            status("Server established!");
            status("Waiting for clients to join...");

            Thread waitingForClientsToConnect = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            System.out.println("Waiting for a client to connect on port " + port);
                            Socket clientsSocket = serverSocket.accept();
                            ALL_CLIENTS.add(new ConnectionToClient(clientsSocket));
                            sGUI.updateClientsConnectedLabel(); //updates the label which indicates how many users are connected

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(sGUI, "An error occurred: ", "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }; //this thread establishes connections with new clients

            waitingForClientsToConnect.setDaemon(true);
            waitingForClientsToConnect.start();

            try {
                waitingForClientsToConnect.join(); //wait for threads to finish (they won't finish since they have infinite loops, but we need this so program won't exit immediately)
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(sGUI, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readConfig() {
        Properties props = new Properties();
        FileInputStream fis;
        try {
            fis = new FileInputStream("config.xml");
            props.loadFromXML(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        port = Integer.parseInt(props.getProperty("port"));

    }

    public static ArrayList<ConnectionToClient> getAllClients() {
        return ALL_CLIENTS;
    }

    public static void status(String status) {//hahahahahahahaha

        if (status.contains("Nowiński")) for (int i = 0; i < 500; i++)
            sGUI.addStatus("CLEAR THAT POINT");
        sGUI.addStatus(status);

    }

    public static Game getGame(int gameIndex) {
        return ALL_GAMES.get(gameIndex);
    }

    public static void sendToGame(Object obj, int gameIndex) {
        for (ConnectionToClient c : ALL_CLIENTS)
            if (c.getGameIndex() == gameIndex) c.write(obj);
    }

    public static void sendToAll(Object message) {
        status((String) message);
        for (ConnectionToClient client : ALL_CLIENTS)
            client.write(message);
    }

    private static class ConnectionToClient {

        //this class handles connection with one particular client, has its own socket, its own streams and thread and stuff

        boolean clientIsHost = false;
        private ObjectInputStream inputFromClient;
        private ObjectOutputStream outputToClient;
        private Socket socket;
        private String usernameOfClient = "";
        private int gameIndex; // keeps track of which game this client belongs to

        public ConnectionToClient(Socket socket) throws IOException {
            this.socket = socket;
            outputToClient = new ObjectOutputStream(socket.getOutputStream());
            inputFromClient = new ObjectInputStream(socket.getInputStream());

            status("A new client has connected!");

            //THIS THREAD BELOW HANDLES OBJECT FROM A CLIENT

            Thread handleObjectFromClient = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Object obj = inputFromClient.readObject();
                            System.out.println("An object was read from a client: " + obj.toString());

                            //IF WE RECEIVED GAMEINFO, THEN WE CREATE A NEW GAME:

                            if (obj instanceof GameInfo) {
                                //We received a request to start a new game from a client
                                GameInfo info = ((GameInfo) obj);   //parsing the object given, i.e. game info
                                // initialize our fields with info from info:
                                clientIsHost = true;
                                usernameOfClient = info.getUsernameOfHost();    //since we're the host, we just take this from info


                                //make a new Game from the info:
                                Game newGame = new Game(info);
                                gameIndex = info.getGameIndex();
                                ALL_GAMES.put(gameIndex, newGame);
                                status("Game " + info.getGameIndex() + " created. Host: " + info.getUsernameOfHost() + "; BoardSize: " + info.getBoardSize());
                            }

                            //IF THE RECEIVED OBJECT IS A STRING, IT CAN BE EITHER A NEW PLAYER, OR A NEW MESSAGE!

                            else if (obj instanceof String) {

                                if (((String) obj).startsWith("@")) {

                                    //THIS MEANS WE GET A NEW PLAYER WANTING TO JOIN A RAND GAME

                                    String newPlayer = (String) obj;
                                    usernameOfClient = newPlayer.substring(1);  //remove the @ from the username
                                    status(usernameOfClient + " connected to the server!");
                                    //find a suitable Game for the new Client
                                    Game game;
                                    int i = 0;
                                    do {
                                        i = (int) (Math.random() * ALL_GAMES.size()); //we randomize a game for him here
                                        game = getGame(i);

                                    } while (!game.isOpen());//looping through all games until one is open

                                    //add the new player to the game:
                                    game.addPlayer(newPlayer);
                                    status(newPlayer + " connected to game " + game.getIndex() + " whose host is " + game.getUsernameOfHost());

                                    //now we send the updated GameInfo to all players in that game
                                    //this also means that the newly connected player will learn what the size of the Board is, etc.
                                    GameInfo info = game.getInfo();
                                    sendToGame(info, gameIndex);
                                    sendToGame("SERVER: " + newPlayer + " connected to this game!", getGameIndex());
                                }

                                //IF THE STRING DID NOT START WITH '@', THEN ITS A CHAT MESSAGE, SO WE SEND IT TO ALL PLAYERS IN THIS GAME
                                else {
                                    sendToGame((String) obj, getGameIndex());
                                    status((String) obj);
                                }
                            }

                            //IF WE RECIEVED AN ARRAY OF INTS, THEN WE SEND THESE COORDS TO ALL USERS IN A GAME:

                            else if (obj instanceof int[]) {

                                //obj is int[], so this means its a set of coordinates
                                int[] coordinates = (int[]) obj;
                                status("Game " + gameIndex + " received a click at " + coordinates[0] + ", " + coordinates[1]);
                                //activate the click method in Game, which will hopefully send the coords to players.
                                getGame(gameIndex).click(coordinates[0], coordinates[1]);
                            }


                            //IF WE RECEIVED A SINGLE INT, THEN IT WAS SOME SORT OF "SIGNAL", SO WE HANDLE IT APPROPIATELY:
                            else if (obj instanceof Integer) {

                                int signal = (int) obj;

                                //IF THE SIGNAL IS == USER_DISCONNECTED_SIGNAL, WE REMOVE THIS PLAYER FROM THE GAME, and the server
                                if (signal == USER_DISCONNECTED_SIGNAL) {
                                    getGame(gameIndex).removePlayer(usernameOfClient);
                                    ALL_CLIENTS.remove(this);
                                    closeConnections();
                                    //GameInfo gets updated: new ArrayList of players is smaller!
                                    //THEN SEND THE UPDATED GAMEINFO TO ALL PLAYERS IN THE GAME.
                                    if (!clientIsHost)
                                        sendToGame(getGame(gameIndex).getInfo(), gameIndex);
                                    //TODO somehow close connection so we don't get weird server error
                                }

                            }

                        } catch (IOException e) {
                            //if we encountered some error, we disable connections with this client just to be sure
                            status("Closing the connection with user " + usernameOfClient);
                            closeConnections();
                            try {
                                this.join();
                            } catch (InterruptedException e1) {
                                JOptionPane.showMessageDialog(sGUI, "An error occurred on disconnecting." + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                                e1.printStackTrace();
                            }
                        } catch (ClassNotFoundException e) {
                            JOptionPane.showMessageDialog(sGUI, "An error occurred (ClassNotFoundException in MinesweeperThreadedServer.java): " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    }
                }
            };

            handleObjectFromClient.setDaemon(true); // makes sure this thread terminates when main ends
            handleObjectFromClient.start();

        }

        public int getGameIndex() {
            return this.gameIndex;
        }

        public void write(Object obj) {
            try {
                outputToClient.writeObject(obj);
            } catch (SocketException e) {
                //TODO disconnect here
            } catch (IOException e) {
                e.printStackTrace();
                //this.closeConnections();
            }
        }

        public void closeConnections() {
            try {
                //we're shutting down server-side connections to client, and closing socket because this client disconnected
                sGUI.updateClientsConnectedLabel(); //updates the label which indicates how many users are connected
                outputToClient.close();
                inputFromClient.close();
                this.socket.close();
            } catch (Exception e) {
                //JOptionPane.showMessageDialog(sGUI, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}

