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
    GameInfo info;
    Board board;
    TableGUI table;

    private ConnectionPopup popup;

    private ConnectionToServer server;
    private LinkedBlockingQueue<Object> objectsReceivedFromServer;
    private Socket socket;
    private boolean hasAlreadyReceivedGameInfo = false;
    private boolean hasAlreadyReceivedGameIndex = false;
    //private boolean hasAlreadyReceivedSquares = false;

    public Client(boolean isHost, String IPAddress, int port, final String clientUsername, int boardSize) throws IOException {

        socket = new Socket(IPAddress, port);
        objectsReceivedFromServer = new LinkedBlockingQueue<>();
        server = new ConnectionToServer(socket);

        if (isHost) {

            info = new GameInfo(clientUsername, IPAddress, boardSize);
            board = new Board(info, this); // DONE: added new Board constructor for hosts
            table = new TableGUI(info, board);//DONE: added new TableGUI constructor for hosts
            server.write(info);//sending server information about game
            hasAlreadyReceivedGameInfo = true;//true since server does not need to send GameInfo to us again. host creates his own GameInfo
            //as players join lobby, we must add players to the arraylist of players in Board.java, and somehow update tableGUI

            //waiting for squares[][] array to be sent from server
        } else {

            //CLIENT MUST CREATE HIS board in the thread handleObjectsFromServer because we must wait for GameInfo and squares.
            server.write("@" + clientUsername);//after this was sent server must reply with squares[][] from a given game so we know how board looks like
            //hasAlreadyReceivedSquares = false;
            hasAlreadyReceivedGameInfo = false;
            popup = new ConnectionPopup();
        }

        Thread handleObjectsFromServer = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Object objectFromServer = objectsReceivedFromServer.take();
                        if (objectFromServer instanceof GameInfo && hasAlreadyReceivedGameInfo == false) {
                            popup.dispose();
                            info = ((GameInfo) objectFromServer);
                            hasAlreadyReceivedGameInfo = true;
                            board = new Board(info, clientUsername, Client.this);
                            JOptionPane.showMessageDialog(null, "bsize: " + info.getBoardSize(), "Saperska Mustard", JOptionPane.INFORMATION_MESSAGE);
                            table = new TableGUI(info, clientUsername, board);

                        }
                        //otherwise, IF WE ALREADY HAVE RECIEVED INFO, WE UPDATE OUR BOARD.

                        else if (objectFromServer instanceof GameInfo && hasAlreadyReceivedGameInfo == true)
                            board.updateBoard((GameInfo) objectFromServer);

                        else if (objectFromServer instanceof String) {//we received a chat message
                            String message = (String) objectFromServer;
                            System.out.println("Message Received: " + message);
                        }
                        //TODO if we receive a String then add it to the Chatbox
                        else if (objectFromServer instanceof boolean[][]) {
                            System.out.println("we received squares yay!!");
                            boolean[][] mines = (boolean[][]) objectFromServer;
                            board.setUpSquares(mines);

                        } else if (objectFromServer instanceof int[]) {
                            int[] coordinates = ((int[]) objectFromServer);
                            board.receiveClick(coordinates[0], coordinates[1]);
                        }
                        //IF WE RECEIVED A TWODIMENSIONAL ARRAY OF BOOLEANS, WE SET UP OUR SQUARES WITH MINES

                        //IF WE RECEIVED INFO ABOUT THE GAME ITSELF FOR THE FIRST TIME, THEN MAKE A NEW BOARD AND GUI:
                        //(BECAUSE IT MEANS WE'RE A NEW PLAYER)

                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                        server.closeConnections();//close client's connections with server since something fucked up
                    }

                }
            }
        };

        handleObjectsFromServer.setDaemon(true);
        handleObjectsFromServer.start();
    }

    public void send(Object obj) {
        server.write(obj);
    }

    public void disconnect() {

        server.closeConnections();

    }

    private class ConnectionToServer {

        ObjectInputStream inputFromServer;

        ObjectOutputStream outputToServer;
        Socket socket;

        public ConnectionToServer(Socket socket) throws IOException {
            this.socket = socket;
            inputFromServer = new ObjectInputStream(socket.getInputStream());
            outputToServer = new ObjectOutputStream(socket.getOutputStream());

            Thread addReceivedObjectsFromServerToQueue = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Object obj = inputFromServer.readObject();
                            objectsReceivedFromServer.put(obj);

                        } catch (IOException e) {
                            ConnectionToServer.this.closeConnections();
                            try {
                                join();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            addReceivedObjectsFromServerToQueue.setDaemon(true);
            addReceivedObjectsFromServerToQueue.start();
        }

        public ConnectionToServer getServer() {
            return server;
        }

        private void write(Object obj) {
            try {
                outputToServer.writeObject(obj);
            } catch (IOException e) {
                e.printStackTrace();
                //this.closeConnections();
            }
        }

        public void closeConnections() {
            try {
                inputFromServer.close();
                outputToServer.close();
                socket.close();
                board.gui.dispose();
                MainMenu.run();
                System.out.println("We closed the client-side i/o streams and socket, most likely due to server shutdown/disconnect");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "An error occurred on disconneting." + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
            }
        }


    }

    private class ConnectionPopup extends JFrame {

        private javax.swing.JButton jButton1;
        private javax.swing.JLabel jLabel1;

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
                public void actionPerformed(ActionEvent e) {
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
            pack();
        }// </editor-fold>

    }

}












