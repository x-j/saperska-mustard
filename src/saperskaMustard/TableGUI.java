package saperskaMustard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TableGUI extends JFrame {

    private final String usernameOfHost;
    private Board board;
    private int boardSize;
    private int counterOfMinesLeft;
    private String clientUsername;
    private JLabel minesLeftLabel;
    private JLabel statusIcon;
    private JPanel boardPanel;
    private JTextArea chatboxArea;
    private JTextField chatboxMessageField;
    private JButton quitToMMButton;
    private JButton startGameButton;
    private JLabel whosePlayerTurnItIsLabel;
    private JPanel mainPanel;
    private JScrollPane chatboxPane;
    private boolean isHost;

    public TableGUI(GameInfo info, String username, Board board, boolean isHost) {// username will always be username of client, since GameInfo already knows username of host

        this.board = board;
        board.setGui(this);
        this.isHost = isHost;
        usernameOfHost = info.getUsernameOfHost();
        this.clientUsername = username;
        this.boardSize = info.getBoardSize();
        counterOfMinesLeft = board.getNumberOfMines();

        startGUI();
        pack();
    }

    private void startGUI() {
        setTitle("Saperska Mustard - " + usernameOfHost + "'s room");
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() { // this block adds the exit prompt
            public void windowClosing(WindowEvent we) {

                String ObjButtons[] = {"Yes", "No"};
                int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Saperska Mustard", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                if (PromptResult == JOptionPane.YES_OPTION) {
                    board.getConnection().send(Client.DISCONNECT_SIGNAL);//sending USER_DISCONNECTED_SIGNAL so server will can tell others that one has disconnected
                    System.exit(0);
                }
            }
        });
        initComponents();
        makeTheTable();
        pack();
    }


    private void initComponents() {
        add(mainPanel);
        if (isHost)
            startGameButton.setVisible(true);
        else
            startGameButton.setVisible(false);

        if (boardSize >= 14)
            whosePlayerTurnItIsLabel.setText("<html>Waiting for the game to start</html>");
        else
            whosePlayerTurnItIsLabel.setText("<html>Waiting for start</html>");

        minesLeftLabel.setText("<html>" + counterOfMinesLeft + " mines left</html>");
        setLocationRelativeTo(null);
        addActionListeners();

    }

    private void addActionListeners() {
        chatboxMessageField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String message = ((JTextField) e.getSource()).getText() + "\n";
                    message = message.trim();
                    if (message.startsWith("@")) message = message.substring(1);
                    message = clientUsername + ": " + message;
                    chatboxMessageField.setText("");
                    board.getConnection().send(message);
                    //chatboxArea.append(message);//we really dont need the chatbox class I think. just append message to chatboxArea
                    // chatbox.addMessage(message);
                    if (message.contains("penis")) statusIcon.setText("( ͡° ͜ʖ ͡°)");
                    if (message.contains("such") && message.contains("and")) {
                        whosePlayerTurnItIsLabel.setText("such");
                        statusIcon.setText("and");
                        minesLeftLabel.setText("such");
                    }

                }
            }
        }); // catches messages sent through the chat box and sends them to the
        // Chatbox class

        quitToMMButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!board.isGameOver()) {
                    String ObjButtons[] = {"Yes", "No"};
                    int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to quit to main menu?", "Saperska Mustard", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                    if (PromptResult == JOptionPane.YES_OPTION) {
                        board.getConnection().disconnect();
                        dispose();
                    }
                } else {
                    board.getConnection().disconnect();
                    dispose();
                }
            }
        }); // this block adds the exit prompt

        startGameButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                startGameButton.setVisible(false);
                board.getConnection().send("[" + usernameOfHost + " started the game!]");
                board.gameStart();
                if (boardSize >= 12)
                    whosePlayerTurnItIsLabel.setText("<html>It's " + board.getCurrentPlayer() + "'s turn. </html>");
                else
                    whosePlayerTurnItIsLabel.setText("<html>" + board.getCurrentPlayer() + "'s turn. </html>");

            }
        }); // handles the startGameButton, begins the game via the Board method

    } // assigngs Action Listeners to some components


    private void makeTheTable() {

        // this method adds squares to the boardPanel
        // and sets up the Board, i guess

        GridLayout boardPanelLayout = new GridLayout(boardSize, boardSize);
        boardPanel.setLayout(boardPanelLayout);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board.getSquares()[i][j] = new SquareButton(board, i, j);
                boardPanel.add(board.getSquares()[i][j]);
            }
        }

    }

    public JTextArea getChatboxArea() {
        return chatboxArea;
    }

    public int getCounterOfMinesLeft() {
        return counterOfMinesLeft;
    }

    public JLabel getMinesLeftLabel() {
        return minesLeftLabel;
    }

    public void incrementCounterOfMinesLeft() {
        counterOfMinesLeft++;
    }

    public void decrementCounterOfMinesLeft() {
        counterOfMinesLeft--;
    }

    public JLabel getStatusIcon() {
        return statusIcon;
    }

    public JLabel getWhosePlayerTurnItIsLabel() {
        return whosePlayerTurnItIsLabel;
    }


}