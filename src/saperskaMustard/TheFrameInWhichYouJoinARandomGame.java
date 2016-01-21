package saperskaMustard;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by xj on 12/01/2016.
 */
public class TheFrameInWhichYouJoinARandomGame extends JFrame {

	String username;
	public final static int DEFAULT_PORT = 5000;
	final private static String DEFAULT_USERNAME_FIELD_TEXT = "TEST";
	public static String DEFAULT_IP_ADDRESS;

	public TheFrameInWhichYouJoinARandomGame() {
		setPreferredSize(new Dimension(230, 220));
		setResizable(false);
		try {
			DEFAULT_IP_ADDRESS = InetAddress.getLocalHost().toString();
			DEFAULT_IP_ADDRESS = DEFAULT_IP_ADDRESS.substring(DEFAULT_IP_ADDRESS.indexOf("/") + 1);
		} catch ( UnknownHostException e ) {
			e.printStackTrace();
		}
		initComponents();
	}

	// method initComponents created mostly using NetBeans, so it contains some weird stuff, for example Group Layout
	private void initComponents() {

		theFieldInWhichYouInputYourUsername = new JTextField();
		usernameLabel = new JLabel();
		ipLabel = new JLabel();
		joinGameButton = new JButton();
		backToMMButton = new JButton();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Saperska Mustard");

		theIpField.setText(DEFAULT_IP_ADDRESS);

		theFieldInWhichYouInputYourUsername.setText(DEFAULT_USERNAME_FIELD_TEXT);
		theFieldInWhichYouInputYourUsername.setToolTipText("enter your username here");

		usernameLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
		usernameLabel.setText("Username:");

		ipLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
		ipLabel.setText("IP of server:");

		joinGameButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		joinGameButton.setText("<html>Join a random game</html>");
		joinGameButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed( java.awt.event.ActionEvent evt ) {

				joinGameButtonActionPerformed(evt);
			}
		});

		backToMMButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		backToMMButton.setText("Back to Main Menu");
		backToMMButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed( java.awt.event.ActionEvent evt ) {

				backToMMButtonActionPerformed(evt);
			}
		});

		ipTextField = new JTextField(DEFAULT_IP_ADDRESS);
		ipTextField.setToolTipText("enter the IP of the server here");

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(ipLabel)
										.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
								.addGap(18)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addComponent(theFieldInWhichYouInputYourUsername, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
										.addComponent(ipTextField, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
								.addGap(84))
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addGap(39)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(joinGameButton, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(backToMMButton, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGap(110))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(usernameLabel)
										.addComponent(theFieldInWhichYouInputYourUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(11)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(ipLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
										.addComponent(ipTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(15)
								.addComponent(joinGameButton)
								.addGap(15)
								.addComponent(backToMMButton)
								.addGap(15))
		);
		getContentPane().setLayout(layout);

		pack();
		setLocationRelativeTo(null);
	}

	private void backToMMButtonActionPerformed( java.awt.event.ActionEvent evt ) {

		System.out.println("Going back to main menu from TheFrameInWhichYouCreateANewTable");
		MainMenu.run();
		this.dispose();

	}

	private void joinGameButtonActionPerformed( java.awt.event.ActionEvent evt ) {

		//watch out, this event handles joining to an already existing Game.

		String ip = theIpField.getText();
		username = theFieldInWhichYouInputYourUsername.getText();
		username = username.trim();

		if ( username.length() < 11 ) {
			if ( username.length() < 2 )
				JOptionPane.showMessageDialog(this, "The username must be at least 2 characters long.");
			else if ( username.startsWith("@") ) {
				JOptionPane.showMessageDialog(this, "The username cannot start with '@'.");
			} else {

				System.out.println("Should be establishing connection with server now.");

				try {
					Client clientConnection = new Client(false, ip, DEFAULT_PORT, username, -1);//starting server connection as client (joing exisitng lobby), boardSize is -1 to indicate that we're not host
					this.dispose();
				} catch ( Exception e ) {
					JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Saperska Mustard", JOptionPane.ERROR_MESSAGE);
				}

			}
		} else
			JOptionPane.showMessageDialog(this, "The username must be at most 10 characters long.");
	}

	public void start() {

		try {
			for ( UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() ) {
				if ( "Nimbus".equals(info.getName()) ) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch ( ClassNotFoundException ex ) {
			java.util.logging.Logger.getLogger(TheFrameInWhichYouCreateANewTable.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch ( InstantiationException ex ) {
			java.util.logging.Logger.getLogger(TheFrameInWhichYouCreateANewTable.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch ( IllegalAccessException ex ) {
			java.util.logging.Logger.getLogger(TheFrameInWhichYouCreateANewTable.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch ( UnsupportedLookAndFeelException ex ) {
			java.util.logging.Logger.getLogger(TheFrameInWhichYouCreateANewTable.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {

				new TheFrameInWhichYouJoinARandomGame().setVisible(true);
			}
		});
	}

	private JTextField theIpField = new JTextField();
	private JButton joinGameButton;
	private JButton backToMMButton;
	private JLabel ipLabel;
	private JTextField theFieldInWhichYouInputYourUsername;
	private JLabel usernameLabel;
	private JTextField ipTextField;


}
