package saperskaMustard;

import javax.swing.*;
import javax.swing.border.BevelBorder;
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
    private JScrollPane paneOfChatbox;
    private JLabel timerLabel;

    private boolean isHost;

    private Thread timerThread;

    public TableGUI(GameInfo info, String username, Board board, boolean isHost) {// username will always be username of client, since GameInfo already knows username of host

        this.board = board;
        board.setGui(this);
        this.isHost = isHost;
        usernameOfHost = info.getUsernameOfHost();
        this.clientUsername = username;
        this.boardSize = info.getBoardSize();
        counterOfMinesLeft = board.getNumberOfMines();

        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                while (true) {
                    long then = System.currentTimeMillis();
                    long milisElapsed = then - now;
                    if (milisElapsed % 100 == 0) {
                        int secondsElapsed = (int) (milisElapsed * 0.001);
                        timerLabel.setText(secondsElapsed + "s");
                    }
                }
            }
        });

        startGUI();
        pack();
    }

    private void startGUI() {
        setTitle("Saperska Mustard - " + usernameOfHost + "'s room");
        setVisible(true);
        setResizable(false);
        setMaximumSize(getSize());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() { // this block adds the exit prompt
            public void windowClosing(WindowEvent we) {

                String ObjButtons[] = {"Yes", "No"};
                int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Saperska Mustard", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                if (PromptResult == JOptionPane.YES_OPTION) {
                    stopTimer();
                    board.getConnection().disconnect();  //sending USER_DISCONNECTED_SIGNAL so server will can tell others that one has disconnected
                    System.exit(0);
                }
            }
        });
        this.lookAndFeelCustomization();

        initComponents();
        makeTheTable();
    }

    private void initComponents() {

        whosePlayerTurnItIsLabel = new JLabel();
        boardPanel = new JPanel();
        chatboxMessageField = new JTextField();
        quitToMMButton = new JButton();
        startGameButton = new JButton();
        statusIcon = new JLabel();
        minesLeftLabel = new JLabel();
        paneOfChatbox = new JScrollPane();
        chatboxArea = new JTextArea();
        timerLabel = new JLabel();

        paneOfChatbox.setWheelScrollingEnabled(true);
        paneOfChatbox.getViewport().setView(chatboxArea);
        chatboxArea.setMaximumSize(chatboxArea.getSize());
        chatboxArea.setEditable(false);

        if (clientUsername.equals(usernameOfHost))
            startGameButton.setVisible(true);
        else
            startGameButton.setVisible(false);

        whosePlayerTurnItIsLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));
        whosePlayerTurnItIsLabel.setHorizontalTextPosition(SwingConstants.RIGHT);

        if (boardSize >= 14)
            whosePlayerTurnItIsLabel.setText("<html>Waiting for the game to start</html>");
        else
            whosePlayerTurnItIsLabel.setText("<html>Waiting for start</html>");

        whosePlayerTurnItIsLabel.setPreferredSize(new java.awt.Dimension(boardSize * 15 / 2, 15));

        boardPanel.setPreferredSize(new java.awt.Dimension(boardSize * 25, boardSize * 25));

        boardPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        boardPanel.setForeground(new java.awt.Color(0, 0, 5));

        GridLayout boardPanelLayout = new GridLayout();
        boardPanel.setLayout(boardPanelLayout);

        quitToMMButton.setText("Quit");

        startGameButton.setText("Start game");

        statusIcon.setHorizontalAlignment(SwingConstants.CENTER);
        statusIcon.setText("");
        statusIcon.setFont(new java.awt.Font("Tahoma", 3, 12));

        minesLeftLabel.setFont(new java.awt.Font("Tahoma", 3, 12));
        minesLeftLabel.setText("<html>" + counterOfMinesLeft + " mines left</html>");

        chatboxArea.setColumns(boardSize);
        chatboxArea.setRows(boardSize);
        paneOfChatbox.setViewportView(chatboxArea);
        paneOfChatbox.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        paneOfChatbox.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        setUpLayout();

        pack();
        setLocationRelativeTo(null);
        addActionListeners();

    }

    public void stopTimer() {
        if (timerThread.isAlive()) {
            try {
                timerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void runTimer() {

        timerThread.start();

    }

    private void setUpLayout() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(minesLeftLabel, GroupLayout.PREFERRED_SIZE, boardSize * 10, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(statusIcon, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(timerLabel, GroupLayout.PREFERRED_SIZE, boardSize * 10, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(boardPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(startGameButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(quitToMMButton, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(chatboxMessageField, GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(whosePlayerTurnItIsLabel, GroupLayout.PREFERRED_SIZE, boardSize * 15, GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(paneOfChatbox, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(minesLeftLabel)
                                        .addComponent(timerLabel)
                                        .addComponent(statusIcon)
                                        .addComponent(whosePlayerTurnItIsLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(paneOfChatbox, GroupLayout.PREFERRED_SIZE, boardSize * 20, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(chatboxMessageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(startGameButton, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(quitToMMButton, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(boardPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
    }

    public void lookAndFeelCustomization() {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Steel".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    } // added by default from NetBeans

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
                    if (message.contains("penis")) statusIcon.setText("( ͡° ͜ʖ ͡°)");
                    if (message.contains("such") && message.contains("and")) {
                        whosePlayerTurnItIsLabel.setText("such");
                        statusIcon.setText("and");
                        minesLeftLabel.setText("such");
                    }

                }
            }
        });

        quitToMMButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!board.isGameOver()) {
                    String ObjButtons[] = {"Yes", "No"};
                    int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to quit to main menu?", "Multiplayer Minesweeper", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                    if (PromptResult == JOptionPane.YES_OPTION) {
                        stopTimer();
                        board.getConnection().disconnect();
                        dispose();
                    }
                } else {
                    stopTimer();
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

    public void addMessage(String message) {
        if (!message.endsWith(": ") && !message.endsWith("> ")) {
            chatboxArea.append(message + "\n");
            chatboxArea.setCaretPosition(chatboxArea.getText().length());
            paneOfChatbox.getVerticalScrollBar().setValue(paneOfChatbox.getVerticalScrollBar().getMaximum());
        }
    }

    private void makeTheTable() {

        // this method adds squares to the boardPanel
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