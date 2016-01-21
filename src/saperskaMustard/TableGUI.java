package saperskaMustard;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;

public class TableGUI extends JFrame {

	final String usernameOfHost;
	int boardSize;
	Chatbox chatbox = new Chatbox();
	Board board;
	String clientUsername;
	int counterOfMinesLeft;

	private JPanel boardPanel;
	private JTextArea chatboxArea;
	private JTextField chatboxMessageField;
	public JLabel minesLeftLabel;
	private JButton quitToMMButton;
	private JButton startGameButton;
	public JLabel statusIcon;
	private JLabel whosePlayerTurnItIsLabel;

	/* The constructor below is used for clients */
	public TableGUI(GameInfo info, String username, Board board) {// username will always be username of client, since GameInfo already knows username of host

		this.board = board;
		board.gui = this;
		usernameOfHost = info.getUsernameOfHost();
		this.clientUsername = username;
		this.boardSize = info.getBoardSize();
		counterOfMinesLeft = board.numberOfMines;

		startGUI();

	}

	/************* The constructor below is only used for hosts! ******************/

	public TableGUI(GameInfo info, Board board) {// both clientUsername and hostUsername are the same, I think that's ok for now?

		this.board = board;
		board.gui = this;
		usernameOfHost = info.getUsernameOfHost();
		this.clientUsername = info.getUsernameOfHost();
		this.boardSize = info.getBoardSize();
		counterOfMinesLeft = board.numberOfMines;

		startGUI();

	}

	private void startGUI() {
		setTitle("Saperska Mustard - " + usernameOfHost + "'s room");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() { // this block adds the exit
			// prompt
			public void windowClosing(WindowEvent we) {

				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Saperska Mustard", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
				if (PromptResult == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		setResizable(false);
		this.lookAndFeelCustomization();
		initComponents();
		makeTheTable();
		initializeChatbox();
	}

	private void initializeChatbox() {

		chatboxArea.setEditable(false);

	}

	private void initComponents() {

		whosePlayerTurnItIsLabel = new JLabel();
		boardPanel = new JPanel();
		chatboxMessageField = new JTextField();
		quitToMMButton = new JButton();
		startGameButton = new JButton();
		statusIcon = new JLabel();
		minesLeftLabel = new JLabel();
		chatboxArea = new JTextArea();

		if (clientUsername == usernameOfHost)
			startGameButton.setVisible(true);
		else
			startGameButton.setVisible(false);

		whosePlayerTurnItIsLabel.setFont(new java.awt.Font("Tahoma", 3, 12));
		whosePlayerTurnItIsLabel.setHorizontalTextPosition(SwingConstants.CENTER);

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
		statusIcon.setText("status");

		minesLeftLabel.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
		minesLeftLabel.setText("<html>" + counterOfMinesLeft + " mines left</html>");

		chatboxArea.setColumns(boardSize);
		chatboxArea.setRows(boardSize);
		chatbox.setViewportView(chatboxArea);
		chatbox.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatbox.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addGroup(
												layout.createSequentialGroup().addComponent(whosePlayerTurnItIsLabel, GroupLayout.PREFERRED_SIZE, boardSize * 15 / 2, GroupLayout.PREFERRED_SIZE).addGap(boardSize * 5 / 2, boardSize * 5 / 2, boardSize * 5 / 2).addComponent(statusIcon, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE).addGap(boardSize * 5 / 2, boardSize * 5 / 2, boardSize * 5 / 2)
														.addComponent(minesLeftLabel, GroupLayout.PREFERRED_SIZE, boardSize * 10, GroupLayout.PREFERRED_SIZE)).addComponent(boardPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(startGameButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE).addComponent(quitToMMButton, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)).addComponent(chatboxMessageField).addComponent(chatbox)).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addGroup(
												layout.createSequentialGroup().addComponent(chatbox, GroupLayout.PREFERRED_SIZE, boardSize * 20, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(chatboxMessageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(startGameButton, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE).addComponent(quitToMMButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
										.addGroup(
												layout.createSequentialGroup()
														.addGroup(
																layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(whosePlayerTurnItIsLabel, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE).addComponent(statusIcon, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																		.addComponent(minesLeftLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(boardPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
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
					String message = ((JTextField) e.getSource()).getText();
					message = clientUsername + "> " + message;
					chatboxMessageField.setText("");
					chatbox.sendMessage(message);
				}
			}
		}); // catches messages sent through the chat box and sends them to the
			// Chatbox class

		quitToMMButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to quit to main menu?", "Saperska Mustard", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
				if (PromptResult == JOptionPane.YES_OPTION) {
					MainMenu.run();
					dispose();
				}
			}
		}); // this block adds the exit prompt

		startGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				startGameButton.setVisible(false);
				chatbox.sendMessage("[" + usernameOfHost + " started the game!]");
				board.gameStart();
				if (boardSize >= 12)
					whosePlayerTurnItIsLabel.setText("<html>It's " + board.currentPlayer + "'s turn. </html>");
				else
					whosePlayerTurnItIsLabel.setText("<html>" + board.currentPlayer + "'s turn. </html>");

			}
		}); // handles the startGameButton, begins the game via the Board method

	} // assigngs Action Listeners to some components

	public void lookAndFeelCustomization() {

		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
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

	private void makeTheTable() {

		// this method adds squares to the boardPanel
		// and sets up the Board, i guess

		GridLayout boardPanelLayout = new GridLayout(boardSize, boardSize);
		boardPanel.setLayout(boardPanelLayout);

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				board.squares[i][j] = new SquareButton(board, i, j);
				boardPanel.add(board.squares[i][j]);
			}
		}

	}

}