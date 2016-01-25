package saperskaMustard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {

    public static final Object DISCONNECT_SIGNAL = 2;
    public static final int NO_OPEN_GAMES_SIGNAL = 3;

    private GameInfo info;
    private Board board;
    private int port; //will be read from config.xml file.
    private String username;

    private ConnectionPopup popup;

    private ConnectionToServer server;
    private LinkedBlockingQueue<Object> objectsReceivedFromServer;
    private Socket socket;
    private boolean hasGameInfo = false;

    public Client(boolean isHost, String IPAddress, String clientUsername, int boardSize) throws IOException {

        readConfig();   //reads info from the config.xml file.

        //we establish a connection with the server:
        socket = new Socket(IPAddress, port);
        objectsReceivedFromServer = new LinkedBlockingQueue<>();
        server = new ConnectionToServer(socket);
        this.username = clientUsername;

        if (isHost) {

            //if we're the host, we are the ones who create new GameInfo
            info = new GameInfo(clientUsername, IPAddress, boardSize);
            board = new Board(info, clientUsername, this, true);
            board.setGui(new TableGUI(info, clientUsername, board, true));
            board.getGui().getWhosePlayerTurnItIsLabel().setText("Waiting for game start");
            //sending the info to the server, also means that a new Game will start on the server
            server.write(info);
            hasGameInfo = true; //becomes true, because we obviosly already created the Board and board.getGui()GUI

        } else {

            //if we're not the host, we have to send our username (with @ at the beginning) this
            // this is equivalent with asking the server for a suiboard.getGui() game for us to join.
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

                        //if we receive GameInfo for the first time  from the server, we use it to create ourselves a Board and GUI
                        if (objectFromServer instanceof GameInfo && !hasGameInfo) {

                            popup.dispose();    //get rid of the popup

                            info = ((GameInfo) objectFromServer);
                            hasGameInfo = true;

                            //there's a possibility that our username had to be changed because it was already existing in this game
                            //so we check:
                            if (info.getPlayers().contains(username + "1"))
                                username += "1";

                            //create new Board and GUI using the GameInfo
                            System.out.println("Players in gameInfo that " + username + " received: ");
                            System.out.println(info.getPlayers());
                            board = new Board(info, username, Client.this, false);
                            board.setGui(new TableGUI(info, username, board, false));
                            board.getGui().getWhosePlayerTurnItIsLabel().setText("Waiting for host");

                        }

                        //otherwise, IF WE ALREADY HAVE RECEIVED INFO, WE UPDATE OUR BOARD.
                        //because it means a new player has joined/left the Game
                        else if (objectFromServer instanceof GameInfo && hasGameInfo) {
                            GameInfo newInfo = (GameInfo) objectFromServer;
                            System.out.println("The client " + username + " will update his board with the following players: ");
                            System.out.println(newInfo);
                            board.updateBoard(newInfo);
                        }


                        //if we receive a String from the server, then we know its a new chat message, so we add it to the chatbox
                        else if (objectFromServer instanceof String) {

                            String message = (String) objectFromServer;
                            board.getGui().addMessage(message);

                        }

                        //if we receive a twodimensional aray of booleans, we setUp our mines and create buttons.
                        else if (objectFromServer instanceof boolean[][]) {
                            boolean[][] mines = (boolean[][]) objectFromServer;
                            board.setUpSquares(mines);

                        }

                        //if we received an array of ints, it means these are coordinates, so we click using them.
                        else if (objectFromServer instanceof int[]) {
                            int[] coordinates = ((int[]) objectFromServer);
                            board.getGui().getChatboxArea().append(board.getCurrentPlayer() + " has clicked the square (" + coordinates[0] + "," + coordinates[1] + ")\n");
                            board.receiveClick(coordinates[0], coordinates[1]);

                            board.getGui().getWhosePlayerTurnItIsLabel().setText(board.getCurrentPlayer() + "'s turn");

                        }

                        //if we received a singular int, that means it's some sort of a signal from the server
                        else if (objectFromServer instanceof Integer) {
                            int signal = (int) objectFromServer;

                            //if it's a signal that there are no open games left, we display a message and return to MM
                            if (signal == NO_OPEN_GAMES_SIGNAL && !hasGameInfo) {
                                JOptionPane.showMessageDialog(null, "No open games available.", "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
                                popup.dispose();
                                MainMenu.run();
                            }
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
                        } catch (ClassNotFoundException | InterruptedException e) {
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

        private javax.swing.JButton quitButton;
        private javax.swing.JLabel textLabel;

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
            quitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainMenu.run();
                    dispose();
                }
            });
        }

        private void initComponents() {

            quitButton = new javax.swing.JButton();
            textLabel = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            setTitle("Saperska Mustard");
            setAutoRequestFocus(false);
            setResizable(false);
            //setType(java.awt.Window.Type.POPUP);

            quitButton.setText("Quit");

            textLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
            textLabel.setText("Connecting to server...");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(quitButton)
                                    .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(textLabel)
                                    .addContainerGap(93, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap(12, Short.MAX_VALUE)
                                    .addComponent(textLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(quitButton)
                                    .addContainerGap())
            );
            setLocationRelativeTo(null);
            pack();
        }// </editor-fold>

    }   //this small GUI object tells us that we're waiting to join a game.

}











