package saperskaMustard;

import javax.swing.*;
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
	public static ArrayList<Game> OPEN_GAMES = new ArrayList<>();    //when a new game lobby is created a NOT RANDOM 4 digit game ID is created so we can distinguish between games in progress
	private ArrayList<ConnectionToClient> clientList;
	private LinkedBlockingQueue<Object> receivedObjects;
	private ServerSocket serverSocket;
	ServerGUI sGUI;

	public MinesweeperThreadedServer( int port ) throws IOException {
		clientList = new ArrayList<ConnectionToClient>();
		receivedObjects = new LinkedBlockingQueue<Object>();
		serverSocket = new ServerSocket(port);
		sGUI = new ServerGUI(this);
		sGUI.annoy();

		Thread waitingForClientsToConnect = new Thread() {
			public void run() {
				while ( true ) {
					try {
						System.out.println("Waiting for a client to connect on port " + PORT);
						Socket clientsSocket = serverSocket.accept();
						clientList.add(new ConnectionToClient(clientsSocket));

					} catch ( Exception e ) {
						JOptionPane.showMessageDialog(sGUI, "An error occurred: " + "asd", "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};
		waitingForClientsToConnect.setDaemon(true);
		waitingForClientsToConnect.start();


		Thread handleObjectsInQueue = new Thread() {
			public void run() {
				while ( true ) {
					///////////////////////////////////////////////////////////////////////////
					/////WE DECIDE WHAT OBJECT WAS OBTAINED, AND WHO DO WE SEND IT TO HERE/////
					///////////////////////////////////////////////////////////////////////////

					try {
						Object nextObjectInQueue = receivedObjects.take();
						if ( nextObjectInQueue instanceof String ) {
							//we received chat message:

							// EXAMPLE CODE OF SENDING CHAT MSG TO ALL USERS IN A SPECIFIC GAME:
							/*
							for(int i = 0; i < clientList.size(); i++){
							 	if(clientList.get(i).getGameIndex() == gameIndex){//gameIndex somehow must be encoded in chat message, should be easy
									clientList.write(object);
								}
							 }


		*/

						} else if (nextObjectInQueue instanceof int[]) {//we received click information
							int[] coordinates = ((int[])(nextObjectInQueue));


							for (int i = 0; i < clientList.size(); i++) {
								if (clientList.get(i).getGameIndex() == coordinates[2]) {
									clientList.get(i).write(coordinates);
								}
							}





						}

					} catch ( Exception e ) {
						JOptionPane.showMessageDialog(sGUI, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};
		handleObjectsInQueue.setDaemon(true);
		handleObjectsInQueue.start();

		try

		{
			waitingForClientsToConnect.join(); //wait for threads to finish (they won't finish since they have infinite loops, but we need this so program won't exit immediately)
			handleObjectsInQueue.join();
		} catch (
				Exception e
				)

		{
			JOptionPane.showMessageDialog(sGUI, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void sendToOne( int index, Object message ) throws IndexOutOfBoundsException {
		clientList.get(index).write(message);
	}

	public void sendToAll( Object message ) {
		for ( ConnectionToClient client : clientList )
			client.write(message);
	}

	private class ConnectionToClient {
		ObjectInputStream inputFromClient;
		ObjectOutputStream outputToClient;
		Socket socket;
		private int gameIndex; // keeps track of which game this client belongs to

		public ConnectionToClient( Socket socket ) throws IOException {
			this.socket = socket;
			outputToClient = new ObjectOutputStream(socket.getOutputStream());
			inputFromClient = new ObjectInputStream(socket.getInputStream());


			NUM_OF_CLIENTS++;
			System.out.println("A client has connected..NUM_OF_CLIENTS: " + NUM_OF_CLIENTS);

			//In the thread below we add an object that we received from the client to the queue which we process in handleObjectsInQueue
			Thread handleObjectFromClient = new Thread() {
				public void run() {
					while ( true ) {
						try {
							Object obj = inputFromClient.readObject();
							System.out.println("An object was read from a client.");

							if (obj instanceof String) {
								if (((String) obj).startsWith("@")) {
									String newPlayer = (String) obj;
									Game game;

									do {
										ConnectionToClient.this.gameIndex = (int) (Math.random() * OPEN_GAMES.size()); //we randomize a game for him here
										game = OPEN_GAMES.get(ConnectionToClient.this.gameIndex);

									} while (game.getPlayers().size() >= 4);
									System.out.println("It seems like a client has joined an existing game");//we check if the game is already filled
									if (game.getPlayers().contains(newPlayer))
										newPlayer += "1";  //this line right here gets rid of the awkwardness of having two players with the same username in-game
									game.getPlayers().add(newPlayer);   //if not, we add him!
									//we do not add the username to queue since it's not a chat message
									GameInfo info = new GameInfo(game.usernameOfHost, game.ipOfHost, game.boardSize, gameIndex);
									outputToClient.writeObject(info);
								} else {
									receivedObjects.put(obj);
									//we received a chat message that we add to the queue that the server handles
								}

							} else if (obj instanceof GameInfo) {
								//We received a request to start a new game from a client
								GameInfo info = ((GameInfo) obj);
								Game newGame = new Game(info);
								OPEN_GAMES.add(newGame);
								ConnectionToClient.this.gameIndex = OPEN_GAMES.size() - 1;
								System.out.println("w00t we received a request to host a game from a client and server successfully created game");
								//////Maybe once we get a game we send the index of the game both in ConnectionToClient and Client.
							} else {
								receivedObjects.put(obj);
							}
						} catch ( IOException e ) {
							//ConnectionToClient.this.closeConnections();
						} catch ( ClassNotFoundException e ) {
							e.printStackTrace();
						} catch ( InterruptedException e ) {
							e.printStackTrace();
						}
					}
				}
			};

			handleObjectFromClient.setDaemon(true); // terminate when main ends
			handleObjectFromClient.start();

		}

		public void setGameIndex(int gameIndex) {
			this.gameIndex = gameIndex;
		}

		public int getGameIndex() {
			return this.gameIndex;
		}

		public void write( Object obj ) {
			try {
				outputToClient.writeObject(obj);
			} catch ( IOException e ) {
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
			} catch ( Exception e ) {
				JOptionPane.showMessageDialog(sGUI, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public static void main( String[] args ) {
		try {

			System.out.println("About to start server");
			MinesweeperThreadedServer server = new MinesweeperThreadedServer(PORT);


		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

}


