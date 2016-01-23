package saperskaMustard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

    public static final Object DISCONNECT_SIGNAL = 2;
    GameInfo info;
    Board board;
    TableGUI table;
    private int port; //will be read from config.xml file.

    private ConnectionPopup popup;

    private ConnectionToServer server;
    private LinkedBlockingQueue<Object> objectsReceivedFromServer;
    private Socket socket;
    private boolean hasGameInfo = false;

    public Client(boolean isHost, String IPAddress, final String clientUsername, int boardSize) throws IOException {

        readConfig();   //reads info from the config.xml file.

        //we establish a connection with the server:
        socket = new Socket(IPAddress, port);
        objectsReceivedFromServer = new LinkedBlockingQueue<>();
        server = new ConnectionToServer(socket);

        if (isHost) {

            //if we're the host, we are the ones who create new GameInfo
            info = new GameInfo(clientUsername, IPAddress, boardSize);
            board = new Board(info, clientUsername, this, true);
            table = new TableGUI(info, clientUsername, board, true);
            //sending the info to the server, also means that a new Game will start on the server
            server.write(info);
            hasGameInfo = true; //becomes true, because we obviosly already created the Board and tableGUI

        } else {

            //if we're not the host, we have to send our username (with @ at the beginning) this
            // this is equivalent with asking the server for a suitable game for us to join.
            server.write("@" + clientUsername);

            //as we wait to connect to the server, we show a popup that tells us that we're waiting.
            popup = new ConnectionPopup();
        }

        //after sending the initial info to the server, we run a thread which will handle objects received

        Thread handleObjectsFromServer = new Thread() {
            public void run() {

                while (true) {

                    try {
                        Object objectFromServer = objectsReceivedFromServer.take();

                        //if we receive GameInfo for the first time fime from the server, we use it to create ourselves a Board and GUI
                        if (objectFromServer instanceof GameInfo && hasGameInfo == false) {

                            popup.dispose();    //get rid of the popup

                            info = ((GameInfo) objectFromServer);
                            hasGameInfo = true;

                            //create new Board and GUI using the GameInfo
                            board = new Board(info, clientUsername, Client.this, false);
                            table = new TableGUI(info, clientUsername, board, false);

                        }

                        //otherwise, IF WE ALREADY HAVE RECEIVED INFO, WE UPDATE OUR BOARD.
                        //because it means a new player has joined/left the Game
                        else if (objectFromServer instanceof GameInfo && hasGameInfo == true)
                            board.updateBoard((GameInfo) objectFromServer);

                            //if we receive a String from the server, then we know its a new chat message, so we add it to the chatbox
                        else if (objectFromServer instanceof String) {
                            String message = (String) objectFromServer;
                            table.getChatboxArea().append(message + "\n");
                        }

                        //if we receive a twodimensional aray of booleans, we setUp our mines and create buttons.
                        else if (objectFromServer instanceof boolean[][]) {
                            boolean[][] mines = (boolean[][]) objectFromServer;
                            board.setUpSquares(mines);

                        }

                        //if we received an array of ints, it means these are coordinates, so we click using them.
                        else if (objectFromServer instanceof int[]) {
                            int[] coordinates = ((int[]) objectFromServer);
                            board.receiveClick(coordinates[0], coordinates[1]);
                            table.getChatboxArea().append(board.getCurrentPlayer() + " has clicked the square (" + coordinates[0] + "," + coordinates[1] + ")\n");
                            table.getWhosePlayerTurnItIsLabel().setText(board.getNextPlayerString() + "'s turn");

                        }

                    } catch (InterruptedException e) {
                        JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                        server.closeConnections();//close client's connections with server since something is wrong
                    }

                }
            }
        };

        handleObjectsFromServer.setDaemon(true);
        handleObjectsFromServer.start();
    }

    private void readConfig() {
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
        this.port = Integer.parseInt(props.getProperty("port"));

    } //reads config.xml file

    public void send(Object obj) {
        server.write(obj);
    }

    public void disconnect() {
        //if we want to disconnect from the server, we have to send a signal, so that the server can remove us from list of clients
        send(DISCONNECT_SIGNAL);
        server.closeConnections();
    }

    private class ConnectionToServer {

        //establishes a connection to the server.

        private ObjectInputStream inputFromServer;
        private ObjectOutputStream outputToServer;
        private Socket socket;

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
            };  //THIS THREAD GETS NEW OBJECTS FROM SERVER AND ADDS THEM TO OUR QUEUE.

            addReceivedObjectsFromServerToQueue.setDaemon(true);
            addReceivedObjectsFromServerToQueue.start();
        }

        private void write(Object obj) {
            try {
                outputToServer.writeObject(obj);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void closeConnections() {
            try {
                inputFromServer.close();
                outputToServer.close();
                socket.close();
                board.getGui().dispose();
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

    }   //this small GUI object tells us that we're waiting to join a game.

}











