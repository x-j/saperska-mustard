/**
 * Created by Filip Matracki on 1/7/2016.
 */
package saperskaMustard;
import java.io.*;
import java.net.*;
public class MinesweeperThread extends  Thread{
    protected Socket socket;

    public MinesweeperThread(Socket clientSocket){
        this.socket = clientSocket;
    }

    public void  run(){
        try {
            ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
