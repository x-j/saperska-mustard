package saperskaMustard;

import javax.swing.*;

public class TheFrameInWhichYouCreateANewTable extends JFrame {

	String username;
	int boardSize;
	final private static int DEFAULT_SIZE = 10;
	final private static String DEFAULT_USERNAME_FIELD_TEXT = "TEST";

	public TheFrameInWhichYouCreateANewTable() {

		initComponents();
	}

	private void initComponents() {

		theFieldInWhichYouInputYourUsername = new JTextField();
		usernameLabel = new JLabel();
		sizeLabel = new JLabel();
		createTableButton = new JButton();
		backToMMButton = new JButton();
		sizeSlider = new JSlider();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Saperska Mustard");
		setResizable(false);

		theFieldInWhichYouInputYourUsername.setText(DEFAULT_USERNAME_FIELD_TEXT);
		theFieldInWhichYouInputYourUsername.setToolTipText("enter your username here");

		usernameLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
		usernameLabel.setText("Username:");

		sizeLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
		sizeLabel.setText("Size:");

		createTableButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		createTableButton.setText("Create the table");
		createTableButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {

				createTableButtonActionPerformed(evt);
			}
		});

		backToMMButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		backToMMButton.setText("Back to Main Menu");
		backToMMButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {

				backToMMButtonActionPerformed(evt);
			}
		});

		sizeSlider.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
		sizeSlider.setMajorTickSpacing(2);
		sizeSlider.setMinimum(4);
		sizeSlider.setMaximum(30);
		sizeSlider.setPaintLabels(true);
		sizeSlider.setPaintTicks(true);
		sizeSlider.setToolTipText("the size for your board");
		sizeSlider.setValue(DEFAULT_SIZE);
		sizeSlider.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(backToMMButton,
																		GroupLayout.PREFERRED_SIZE, 160,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED,
																		63, Short.MAX_VALUE)
																.addComponent(createTableButton,
																		GroupLayout.PREFERRED_SIZE, 142,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING)
																				.addComponent(
																						usernameLabel,
																						GroupLayout.PREFERRED_SIZE,
																						66,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						sizeLabel,
																						GroupLayout.PREFERRED_SIZE,
																						66,
																						GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.UNRELATED)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING)
																				.addComponent(
																						sizeSlider,
																						GroupLayout.PREFERRED_SIZE,
																						0, Short.MAX_VALUE)
																				.addComponent(
																						theFieldInWhichYouInputYourUsername))))
								.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(theFieldInWhichYouInputYourUsername,
												GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
										.addComponent(usernameLabel, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(sizeLabel, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(sizeSlider, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(
								layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(backToMMButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(createTableButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
		setLocationRelativeTo(null);
	}

	private void backToMMButtonActionPerformed(java.awt.event.ActionEvent evt) {

		System.out.println("Going back to main menu from TheFrameInWhichYouCreateANewTable");
		MainMenu.run();
		this.dispose();

	}

	private void createTableButtonActionPerformed(java.awt.event.ActionEvent evt) {

		boardSize = sizeSlider.getValue();

		username = theFieldInWhichYouInputYourUsername.getText();

		if (username.length() < 11) {
			if (username.length() < 2)
				JOptionPane.showMessageDialog(this, "The username must be at least 2 characters long.");
			else {
				username = username.trim();
				System.out.println("The birth of a new table begins now.");
				TableGUI table = new TableGUI(username, boardSize);
				this.dispose();
			}
		} else
			JOptionPane.showMessageDialog(this, "The username must be at most 10 characters long.");
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
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(TheFrameInWhichYouCreateANewTable.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(TheFrameInWhichYouCreateANewTable.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(TheFrameInWhichYouCreateANewTable.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(TheFrameInWhichYouCreateANewTable.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {

				new TheFrameInWhichYouCreateANewTable().setVisible(true);
			}
		});
	}

	// Variables declaration
	private JButton createTableButton;
	private JButton backToMMButton;
	private JLabel sizeLabel;
	private JSlider sizeSlider;
	private JTextField theFieldInWhichYouInputYourUsername;
	private JLabel usernameLabel;
	// End of variables declaration
}
