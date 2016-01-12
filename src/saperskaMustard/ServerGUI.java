package saperskaMustard;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by xj on 2016-01-11.
 */
public class ServerGUI extends javax.swing.JFrame {

	MinesweeperThreadedServer server;


	public ServerGUI( MinesweeperThreadedServer minesweeperThreadedServer ) {
		server = minesweeperThreadedServer;
		initComponents();
		actionListeners();
		setVisible(true);
	}
	
	private void actionListeners() {
		jTextField1.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped( KeyEvent e ) {

			}

			@Override
			public void keyReleased( KeyEvent e ) {

			}

			@Override
			public void keyPressed( KeyEvent e ) {
				if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
					String message = ( (JTextField) e.getSource() ).getText();
					message = "SERVER" + "> " + message;
					jTextField1.setText("");
					//somehow send the message to all users here XD
				}
			}
		});
	}
	
	private void initComponents() {

		jButton1 = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jButton1.setText("Exit");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent evt ) {
				jButton1ActionPerformed(evt);
			}
		});

		jLabel1.setText("The server is running.");

		jLabel2.setText("send a message to all clients:");

		jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saperskaMustard/annoying dog.gif")));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
												.addGap(0, 0, Short.MAX_VALUE)
												.addComponent(jButton1))
										.addGroup(layout.createSequentialGroup()
												.addContainerGap()
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
														.addGroup(layout.createSequentialGroup()
																.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addGap(58, 58, 58)
																.addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(9, 9, 9))
														.addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addGap(5, 5, Short.MAX_VALUE)
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1)
										.addGap(5,5, Short.MAX_VALUE)
										.addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
								.addComponent(jLabel2)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(jButton1)
								.addContainerGap())
		);

		pack();
	}// </editor-fold>

	private void jButton1ActionPerformed( java.awt.event.ActionEvent evt ) {
		System.exit(0);
	}

	public void annoy() {
		try {
			for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() ) {
				if ( "Nimbus".equals(info.getName()) ) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch ( ClassNotFoundException ex ) {
			java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch ( InstantiationException ex ) {
			java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch ( IllegalAccessException ex ) {
			java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch ( javax.swing.UnsupportedLookAndFeelException ex ) {
			java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
			}
		});
	}

	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JTextField jTextField1;

}

