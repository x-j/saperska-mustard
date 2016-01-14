package saperskaMustard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Filip Matracki on 1/7/2016.
 */
public class MinesweeperThreadedServer {
    static final int PORT = 5000;//must be the same as the variable PORT in TheFrameInWhichYouCreateANewTable.java and where join random game button is
    public static int NUM_OF_CLIENTS = 0; //we can call MinesweeperThreadedServer.NUM_OF_CLIENTS in other classes to see how many clients are connected
    public static ArrayList<Game> openGames = new ArrayList<>();    //when a new game lobby is created a NOT RANDOM 4 digit game ID is created so we can distinguish between games in progress
    private ArrayList<ConnectionToClient> clientList;
    private LinkedBlockingQueue<Object> receivedObjects;
    private ServerSocket serverSocket;
    ServerGUI sGUI;

    public MinesweeperThreadedServer(int port) throws IOException {
        clientList = new ArrayList<ConnectionToClient>();
        receivedObjects = new LinkedBlockingQueue<Object>();
        serverSocket = new ServerSocket(port);
        sGUI = new ServerGUI(this);
        sGUI.annoy();

        Thread waitingForClientsToConnect = new Thread(){
          public void run(){
              while(true){
                  try{
                      System.out.println("Waiting for a client to connect on port "+PORT);
                      Socket clientsSocket = serverSocket.accept();
                      clientList.add(new ConnectionToClient(clientsSocket));

                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          }
        };
        waitingForClientsToConnect.setDaemon(true);
        waitingForClientsToConnect.start();

	    //TODO DEAR FILIP somewhere here we have to send the new user the information about the game (unless he's the host)
//                  please handle that, we need to get the info from the class Game (boardSize, current players, username of host) and send it to the newly connected dude.

        Thread handleObjectsInQueue = new Thread(){
            public void run(){
                while(true){
                    ///////////////////////////////////////////////////////////////////////////
                    /////WE DECIDE WHAT OBJECT WAS OBTAINED, AND WHO DO WE SEND IT TO HERE/////
                    ///////////////////////////////////////////////////////////////////////////

                    try {
                        Object nextObjectInQueue = receivedObjects.take();
                        if(nextObjectInQueue instanceof  String){
                            //We received a chat message

                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        handleObjectsInQueue.setDaemon(true);
        handleObjectsInQueue.start();

        try {
            waitingForClientsToConnect.join(); //wait for threads to finish (they won't finish since they have infinite loops, but we need this so program won't exit immediately)
            handleObjectsInQueue.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

	private class ConnectionToClient{
		ObjectInputStream inputFromClient;
        ObjectOutputStream outputToClient;
        Socket socket;

        public ConnectionToClient(Socket socket) throws IOException {
            this.socket = socket;
            outputToClient = new ObjectOutputStream(socket.getOutputStream());
            inputFromClient = new ObjectInputStream(socket.getInputStream());


            NUM_OF_CLIENTS++;
            System.out.println("A client has connected..NUM_OF_CLIENTS: "+NUM_OF_CLIENTS);

           //In the thread below we add an object that we received from the client to the queue which we process in handleObjectsInQueue
            Thread handleObjectFromClient = new Thread(){
                public void run(){
                    while(true){
                        try{
                            Object obj = inputFromClient.readObject();
	                        System.out.println("An object was read from a client.");
	                        receivedObjects.put(obj);
                        }
                        catch(IOException e){ e.printStackTrace(); }
                        catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            handleObjectFromClient.setDaemon(true); // terminate when main ends
            handleObjectFromClient.start();

        }
        public void write(Object obj) {
            try{
                outputToClient.writeObject(obj);
            }
            catch(IOException e){ e.printStackTrace(); }
        }
        public void sendToOne(int index, Object message)throws IndexOutOfBoundsException {
            clientList.get(index).write(message);
        }

        public void sendToAll(Object message){
            for(ConnectionToClient client : clientList)
                client.write(message);
        }
    }
    public static void main(String[] args){
        try {

            System.out.println("About to start server");
            MinesweeperThreadedServer server = new MinesweeperThreadedServer(PORT);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }




