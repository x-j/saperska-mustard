package saperskaMustard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by xj on 12/01/2016.
 */
public class TheFrameInWhichYouJoinARandomGame extends JFrame {

    String username;
    int boardSize;
    public final static int DEFAULT_PORT = 5000;
    final private static int DEFAULT_SIZE = 10;
    final private static String DEFAULT_USERNAME_FIELD_TEXT = "TEST";
    public static String DEFAULT_IP_ADDRESS;


    public TheFrameInWhichYouJoinARandomGame() {
        setType(Window.Type.UTILITY);
        setPreferredSize(new Dimension(230, 250));
        setResizable(false);
        try {
            DEFAULT_IP_ADDRESS = InetAddress.getLocalHost().toString();
            DEFAULT_IP_ADDRESS = DEFAULT_IP_ADDRESS.substring(DEFAULT_IP_ADDRESS.indexOf("/") + 1);
        } catch ( UnknownHostException e ) {
            e.printStackTrace();
        }
//		silly constructor, has only one method inside
        initComponents();
    }

    // method initComponents created mostly using NetBeans, so it contains some weird stuff, for example Group Layout
    private void initComponents() {

        theFieldInWhichYouInputYourUsername = new JTextField();
        usernameLabel = new JLabel();
        sizeLabel = new JLabel();
        createTableButton = new JButton();
        backToMMButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Saperska Mustard");

        theIpField.setText(DEFAULT_IP_ADDRESS);

        theFieldInWhichYouInputYourUsername.setText(DEFAULT_USERNAME_FIELD_TEXT);
        theFieldInWhichYouInputYourUsername.setToolTipText("enter your username here");

        usernameLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        usernameLabel.setText("Username:");

        sizeLabel.setFont(new java.awt.Font("Tahoma", 1, 12));
        sizeLabel.setText("IP of server:");

        createTableButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        createTableButton.setText("Create the table");
        createTableButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed( java.awt.event.ActionEvent evt ) {

                createTableButtonActionPerformed(evt);
            }
        });

        backToMMButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        backToMMButton.setText("Back to Main Menu");
        backToMMButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed( java.awt.event.ActionEvent evt ) {

                backToMMButtonActionPerformed(evt);
            }
        });

        label = new JLabel();
        label.setText("Size:");
        label.setFont(new Font("Tahoma", Font.BOLD, 12));

        textField = new JTextField(DEFAULT_IP_ADDRESS);
        textField.setToolTipText("enter the IP of the server here");

        spinner = new JSpinner();
        spinner.setVisible(false);
        sizeLabel.setVisible(false);
        spinner.setModel(new SpinnerNumberModel(10, 6, 40, 1));

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(label, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sizeLabel)
                                        .addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(theFieldInWhichYouInputYourUsername, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                                        .addComponent(textField, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                                        .addComponent(spinner, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                                .addGap(84))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(39)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(createTableButton, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(sizeLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(31)
                                .addComponent(createTableButton)
                                .addGap(18)
                                .addComponent(backToMMButton)
                                .addGap(24))
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

    private void createTableButtonActionPerformed( java.awt.event.ActionEvent evt ) {

        boardSize = (int) spinner.getValue();
        String ip = theIpField.getText();
        username = theFieldInWhichYouInputYourUsername.getText();
        username = username.trim();

        if ( username.length() < 11 ) {
            if ( username.length() < 2 )
                JOptionPane.showMessageDialog(this, "The username must be at least 2 characters long.");
            else {

                System.out.println("The birth of a new table begins now.");
                System.out.println("Should be establishing connection with server now.");

                //and sending to the server boardSize, and username of host
                //and an ip address?
                try {
                    Client hostConnection = new Client(false, DEFAULT_IP_ADDRESS, DEFAULT_PORT, username, boardSize);//starting server connection as host (new lobby)
                } catch ( IOException e ) {
                    e.printStackTrace();
                }

                Board board = new Board(boardSize, username, username);
                TableGUI table = new TableGUI(username, boardSize, board);
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

                new TheFrameInWhichYouCreateANewTable().setVisible(true);
            }
        });
    }

    private JTextField theIpField = new JTextField();
    private JButton createTableButton;
    private JButton backToMMButton;
    private JLabel sizeLabel;
    private JTextField theFieldInWhichYouInputYourUsername;
    private JLabel usernameLabel;
    private JLabel label;
    private JTextField textField;
    private JSpinner spinner;


}
