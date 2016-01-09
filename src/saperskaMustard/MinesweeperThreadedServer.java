package saperskaMustard;

import java.io.IOException;
import java.net.*;

/**
 * Created by Filip Matracki on 1/7/2016.
 */
public class MinesweeperThreadedServer {
    static final int PORT = 5000;//must be the same as the variable PORT in TheFrameInWhichYouCreateANewTable.java
    static int NUM_OF_CLIENTS = 0; //it's basically a global,but need this shit for minesweeper thread
    public static void main(String[] args){
       ServerSocket serverSocket = null;
       Socket socket = null;

        try{
            serverSocket = new ServerSocket(PORT);
        } catch(IOException e){
            e.printStackTrace();
        }
        while(true){
            try{
                System.out.println("Waiting for a client to connect to us");
                socket = serverSocket.accept();
                NUM_OF_CLIENTS++;
                System.out.println("A client has connected..NUM_OF_CLIENTS: "+NUM_OF_CLIENTS);
            } catch (IOException e){

            }

               new MinesweeperThread(socket).start();



        }


    }
}

