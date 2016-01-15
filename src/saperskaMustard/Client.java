package saperskaMustard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
          GameInfo info = new GameInfo(username,IPAddress,boardSize);
            Board board = new Board(info,username); // now hostUsername is passed twice, once by GameInfo, and once by us /*TODO maybe make a different constructor for Board that takes only one GameInfo arg. if isHost*/
            TableGUI table = new TableGUI(info, username,board);/*TODO maybe make a different constructor for Board that takes only one GameInfo arg. if isHost*/
            server.write(info);//sending server information about game
            //as players join lobby, we must add players to the arraylist of players in Board.java, and somehow update tableGUI

	    }else{
             /*TODO send a special GameInfo object using a different constructor than the host above? Server will reply with standard GameInfo object and it will create table in same way as host*/

            /*
            TODO otherwise, send a request to the Server: please tell me how the Game looks like so I can create myself a Board
            todo this means that we have to find a suitable game
            TODO IF THE FOUND SUITABLE GAME ALREADY HAS A PLAYER WITH OUR USERNAME, ADD '1' AT THE END OF OUR USERNAME
            TODO we add our player to the list of players in the given Game
            and in the meantime, a popup window appears telling us that we're waiting for the connection
            */
            ConnectionPopup popup = new ConnectionPopup();

        }


        Thread handleObjectsFromServer = new Thread() {
            public void run(){
                while(true){
                    try{
                        Object message = objectsReceivedFromServer.take();
                        // Do some handling here...
                        System.out.println("Message Received: " + message);
                    }
                    catch(InterruptedException e){
                        //server.closeConnections();//close client's connections with server since something fucked up
                    }

                }
            }
        };

        //TODO IF WE RECEIVED INFO ABOUT THE GAME ITSELF, THEN MAKE A NEW BOARD AND GUI:
        /*

            Board board = new Board(ARGS);
            TableGUI table = new TableGUI(ARGS);
            popup.dispose(); //and we can now close the popup window.


         */
        //TODO if we recieved a two dimensional array of booleans, activate the setUpSquares method in Board.
        //TODO if we received coordinates, activate the receiveClick method in Board.
        //TODO if we receive a String then add it to the Chatbox

        handleObjectsFromServer.setDaemon(true);
        handleObjectsFromServer.start();
    }

    private class ConnectionToServer {
        ObjectInputStream inputFromServer;
        ObjectOutputStream outputToServer;
        Socket socket;

       public ConnectionToServer(Socket socket) throws IOException {
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
                        catch(IOException e){
                           // ConnectionToServer.this.closeConnections();
                            e.printStackTrace();}
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
            catch(IOException e){
                e.printStackTrace();
                //this.closeConnections();
            }
        }
        private void closeConnections(){
            try{
                inputFromServer.close();
                outputToServer.close();
                socket.close();
                System.out.println("We closed the client-side i/o streams and socket, most likely due to server shutdown/disconnect");
                /*TODO display error window saying to client that server has shutdown*/
            }
            catch(IOException e){

            }
        }
    }

    private class ConnectionPopup extends JFrame {

        public ConnectionPopup() {
            setVisible(true);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(ConnectionPopup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(ConnectionPopup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(ConnectionPopup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(ConnectionPopup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            initComponents();
            setActionListener();
            pack();
        }
    
        private void setActionListener() {
            jButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed( ActionEvent e ) {
                    System.exit(0);
                    MainMenu.run();
                }
            });
        }
    
        private void initComponents() {

            jButton1 = new javax.swing.JButton();
            jLabel1 = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            setTitle("Saperska Mustard");
            setAutoRequestFocus(false);
            setResizable(false);
            //setType(java.awt.Window.Type.POPUP);

            jButton1.setText("Quit");

            jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
            jLabel1.setText("Connecting to server...");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1)
                                    .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel1)
                                    .addContainerGap(93, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap(12, Short.MAX_VALUE)
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton1)
                                    .addContainerGap())
            );
            setLocationRelativeTo(null);
            pack(  );
        }// </editor-fold>

        private javax.swing.JButton jButton1;
        private javax.swing.JLabel jLabel1;

    }

    public void send(Object obj) {
        server.write(obj);
    }
	
	
}
