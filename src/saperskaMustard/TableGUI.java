package saperskaMustard;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class TableGUI extends JFrame {

	String usernameOfHost;
	int boardSize;
	Board board;
	
	private JPanel boardPanel;
    private JTextArea chatboxArea;
    private JTextField chatboxMessageField;
    private JLabel minesLeftLabel;
    private JScrollPane paneOfChatbox;
    private JButton quitToMMButton;
    private JButton startGameButton;
    private JLabel statusIcon;
    private JLabel whosePlayerTurnItIsLabel;

	public TableGUI(String username, int boardSize) {

		this.usernameOfHost = username;
		this.boardSize = boardSize;
		
		board = new Board(boardSize);

		setTitle("Saperska Mustard - " + usernameOfHost + "'s room");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {	//this block adds the exit prompt
			public void windowClosing(WindowEvent we) {
				String ObjButtons[] = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(null,
						"Are you sure you want to exit?", "Saperska Mustard",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[1]);
				if (PromptResult == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		setResizable(false);
		initComponents();
		this.start();

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

        whosePlayerTurnItIsLabel.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        whosePlayerTurnItIsLabel.setText("<html>It's "+usernameOfHost+"'s turn. </html>");
        whosePlayerTurnItIsLabel.setPreferredSize(new java.awt.Dimension(boardSize*15/2, 15));

        boardPanel.setPreferredSize(new java.awt.Dimension(boardSize*25, boardSize*25));
        
        boardPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        boardPanel.setForeground(new java.awt.Color(0, 0, 5));

        GridLayout boardPanelLayout = new GridLayout();
        boardPanel.setLayout(boardPanelLayout);

        quitToMMButton.setText("Quit");

        startGameButton.setText("Start game");

        statusIcon.setHorizontalAlignment(SwingConstants.CENTER);
        statusIcon.setText("status");

        minesLeftLabel.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        minesLeftLabel.setText((board.numberOfMines - board.flagsDeployed)+" mines left");

        chatboxArea.setColumns(boardSize);
        chatboxArea.setRows(boardSize);
        paneOfChatbox.setViewportView(chatboxArea);
        paneOfChatbox.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        paneOfChatbox.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(whosePlayerTurnItIsLabel, GroupLayout.PREFERRED_SIZE, boardSize*15/2, GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(statusIcon, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(minesLeftLabel, GroupLayout.PREFERRED_SIZE, boardSize*10, GroupLayout.PREFERRED_SIZE))
                    .addComponent(boardPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startGameButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(quitToMMButton, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
                    .addComponent(chatboxMessageField)
                    .addComponent(paneOfChatbox))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(paneOfChatbox, GroupLayout.PREFERRED_SIZE, boardSize*20, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chatboxMessageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(startGameButton, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                            .addComponent(quitToMMButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(whosePlayerTurnItIsLabel, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                                .addComponent(statusIcon, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(minesLeftLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boardPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
        
        quitToMMButton.addActionListener(new ActionListener() { //this block adds the exit prompt
			
			@Override
			public void actionPerformed(ActionEvent e) {
						String ObjButtons[] = { "Yes", "No" };
						int PromptResult = JOptionPane.showOptionDialog(null,
								"Are you sure you want to exit?", "Saperska Mustard",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.WARNING_MESSAGE, null, ObjButtons,
								ObjButtons[1]);
						if (PromptResult == JOptionPane.YES_OPTION) 
							System.exit(0);
						
					}
    	});
	}

	public void start() {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(TableGUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}

		// above is weird NetBeans stuff, let's start with the cool method below
		makeTheTable();
	}

	private void makeTheTable() {

		//this method adds squares to the boardPanel 
		
	}

}
