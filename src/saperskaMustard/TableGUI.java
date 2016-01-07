package saperskaMustard;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class TableGUI extends JFrame {

	final String usernameOfHost;
	int boardSize;
	Chatbox chatbox = new Chatbox();
	SquareButton[][] boardButtons;
	Game game;
	String clientUsername;

	private JPanel boardPanel;
	private JTextArea chatboxArea;
	private JTextField chatboxMessageField;
	private JLabel minesLeftLabel;
	private JScrollPane paneOfChatbox;
	private JButton quitToMMButton;
	private JButton startGameButton;
	private JLabel statusIcon;
	private JLabel whosePlayerTurnItIsLabel;

	public TableGUI(String username, int boardSize, Game game) {

		this.game = game;
		usernameOfHost = game.usernameOfHost;
		this.clientUsername = username;
		this.boardSize = boardSize;
		this.boardButtons = new SquareButton[boardSize][boardSize];

		setTitle("Saperska Mustard - " + usernameOfHost + "'s room");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() { // this block adds the exit
												// prompt
			public void windowClosing(WindowEvent we) {

				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?",
						"Saperska Mustard", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
						ObjButtons, ObjButtons[1]);
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
		paneOfChatbox = new JScrollPane();
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

		boardPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		boardPanel.setForeground(new java.awt.Color(0, 0, 5));

		GridLayout boardPanelLayout = new GridLayout();
		boardPanel.setLayout(boardPanelLayout);

		quitToMMButton.setText("Quit");

		startGameButton.setText("Start game");

		statusIcon.setHorizontalAlignment(SwingConstants.CENTER);
		statusIcon.setText("status");

		minesLeftLabel.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
		minesLeftLabel.setText("<html>" + (game.numberOfMines - game.flagsDeployed) + " mines left</html>");

		chatboxArea.setColumns(boardSize);
		chatboxArea.setRows(boardSize);
		paneOfChatbox.setViewportView(chatboxArea);
		paneOfChatbox
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		paneOfChatbox.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addGroup(
												layout.createSequentialGroup()
														.addComponent(whosePlayerTurnItIsLabel,
																GroupLayout.PREFERRED_SIZE,
																boardSize * 15 / 2,
																GroupLayout.PREFERRED_SIZE)
														.addGap(boardSize * 5 / 2, boardSize * 5 / 2,
																boardSize * 5 / 2)
														.addComponent(statusIcon, GroupLayout.PREFERRED_SIZE,
																50, GroupLayout.PREFERRED_SIZE)
														.addGap(boardSize * 5 / 2, boardSize * 5 / 2,
																boardSize * 5 / 2)
														.addComponent(minesLeftLabel,
																GroupLayout.PREFERRED_SIZE, boardSize * 10,
																GroupLayout.PREFERRED_SIZE))
										.addComponent(boardPanel, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(15, 15, 15)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(
												layout.createSequentialGroup()
														.addComponent(startGameButton)
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.RELATED, 33,
																Short.MAX_VALUE)
														.addComponent(quitToMMButton,
																GroupLayout.PREFERRED_SIZE, 82,
																GroupLayout.PREFERRED_SIZE))
										.addComponent(chatboxMessageField).addComponent(paneOfChatbox))
						.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(paneOfChatbox,
																		GroupLayout.PREFERRED_SIZE,
																		boardSize * 20,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(chatboxMessageField,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED,
																		GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING,
																				false)
																				.addComponent(
																						startGameButton,
																						GroupLayout.DEFAULT_SIZE,
																						37, Short.MAX_VALUE)
																				.addComponent(
																						quitToMMButton,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)))
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING,
																				false)
																				.addGroup(
																						layout.createParallelGroup(
																								GroupLayout.Alignment.BASELINE)
																								.addComponent(
																										whosePlayerTurnItIsLabel,
																										GroupLayout.DEFAULT_SIZE,
																										25,
																										Short.MAX_VALUE)
																								.addComponent(
																										statusIcon,
																										GroupLayout.DEFAULT_SIZE,
																										GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE))
																				.addComponent(
																						minesLeftLabel,
																						GroupLayout.Alignment.TRAILING,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE))
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(boardPanel,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

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
		});
		quitToMMButton.addActionListener(new ActionListener() { // this block

					// adds the exit
					// prompt

					@Override
					public void actionPerformed(ActionEvent e) {

						String ObjButtons[] = { "Yes", "No" };
						int PromptResult = JOptionPane.showOptionDialog(null,
								"Are you sure you want to quit to main menu?", "Saperska Mustard",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons,
								ObjButtons[1]);
						if (PromptResult == JOptionPane.YES_OPTION) {
							MainMenu.run();
							dispose();
						}
					}
				});

		startGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SquareButton.gameStart();
				startGameButton.setVisible(false);
				chatbox.sendMessage("[" + usernameOfHost + " started the game!]");
				Game.start();
				if (boardSize >= 12)
					whosePlayerTurnItIsLabel.setText("<html>It's " + game.currentPlayer + "'s turn. </html>");
				else
					whosePlayerTurnItIsLabel.setText("<html>" + game.currentPlayer + "'s turn. </html>");

			}
		});

	}

	public void lookAndFeelCustomization() {

		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}

		// above is weird NetBeans stuff, let's start with the cool method below

	}

	private void makeTheTable() {

		// this method adds squares to the boardPanel
		// and sets up the Board, i guess

		GridLayout boardPanelLayout = new GridLayout(boardSize, boardSize);
		boardPanel.setLayout(boardPanelLayout);

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				SquareButton newSB = new SquareButton(game);
				newSB.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						if (e.getButton() == MouseEvent.BUTTON2) {
							if (newSB.flagged) {
								game.numberOfMines++;

							} else {
								((SquareButton) e.getSource()).setText("F");
								game.numberOfMines--;
							}
						}
					}
				});
				boardButtons[i][j] = newSB;
				boardPanel.add(boardButtons[i][j]);
			}
		}

	}

}
