package saperskaMustard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

    boolean isHost;
    ObjectInputStream inputFromServer;
    ObjectOutputStream outputToServer;
    String username;
    int boardSize;

    private ConnectionToServer server;
    private LinkedBlockingQueue<Object> objectsReceivedFromServer;
    private Socket socket;

    public Client(boolean isHost, String IPAddress, int port, String clientUsername, int newboardSize) throws IOException{
        this.isHost = isHost;
        this.username = clientUsername;//these 3 lines may not be necessary we can just use the ones passed into the function, but who knows
        this.boardSize = newboardSize;
        
        socket = new Socket(IPAddress, port);
        objectsReceivedFromServer = new LinkedBlockingQueue<Object>();
        server = new ConnectionToServer(socket);

	    if(isHost) {
		    //  DEAR FILIP
			//here, the Client sends a request to the Server: please create a new Game, using the constructor most likely
		    //so plx code this :)

	    }


        Thread handleObjectsFromServer = new Thread() {
            public void run(){
                while(true){
                    try{
                        Object message = objectsReceivedFromServer.take();
                        // Do some handling here...
                        System.out.println("Message Received: " + message);
                    }
                    catch(InterruptedException e){ }
                }
            }
        };

        handleObjectsFromServer.setDaemon(true);
        handleObjectsFromServer.start();
    }

    private class ConnectionToServer {
        ObjectInputStream inputFromServer;
        ObjectOutputStream outputToServer;
        Socket socket;

        ConnectionToServer(Socket socket) throws IOException {
            this.socket = socket;
            inputFromServer = new ObjectInputStream(socket.getInputStream());
            outputToServer = new ObjectOutputStream(socket.getOutputStream());

            Thread addReceivedObjectsFromServerToQueue = new Thread(){
                public void run(){
                    while(true){
                        try{
                            Object obj = inputFromServer.readObject();
                            objectsReceivedFromServer.put(obj);
                        }
                        catch(IOException e){ e.printStackTrace(); }
                        catch(ClassNotFoundException e){e.printStackTrace(); }
                        catch(InterruptedException e){e.printStackTrace(); }
                    }
                }
            };

            addReceivedObjectsFromServerToQueue.setDaemon(true);
            addReceivedObjectsFromServerToQueue.start();
        }

        private void write(Object obj) {
            try{
                outputToServer.writeObject(obj);
            }
            catch(IOException e){ e.printStackTrace(); }
        }
    }

    public void send(Object obj) {
        server.write(obj);
    }
	
	
}
