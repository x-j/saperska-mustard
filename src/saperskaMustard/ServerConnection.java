package saperskaMustard;
import java.io.*;
import java.net.*;
public class ServerConnection {

    boolean isHost;
    ObjectInputStream inputFromServer;
    ObjectOutputStream outputToServer;
    private String username;
    private int boardSize;


    public ServerConnection(boolean isHost, String hostName, int port,String username,int boardSize){
        this.isHost = isHost;
        this.username = username;
        this.boardSize = boardSize;

        try {
            Socket clientSocket = new Socket(hostName, port);
            inputFromServer = new ObjectInputStream(clientSocket.getInputStream());
            outputToServer = new ObjectOutputStream(clientSocket.getOutputStream());

        }   catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
            catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	
}
