

package saperskaMustard;

import javax.swing.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        menuExitGameButton = new JButton();
        menuJoinRandGameButton = new JButton();
        menuNewGameButton = new JButton();
        greetings = new JLabel();
        bragLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Saperska Mustard");
        setResizable(false);

        menuExitGameButton.setFont(new java.awt.Font("Tahoma", 0, 14)); 
        menuExitGameButton.setText("Exit");
        menuExitGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitGameButtonActionPerformed(evt);
            }
        });

        menuJoinRandGameButton.setFont(new java.awt.Font("Tahoma", 0, 14)); 
        menuJoinRandGameButton.setText("Join random game");
        menuJoinRandGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuJoinRandGameButtonActionPerformed(evt);
            }
        });

        menuNewGameButton.setFont(new java.awt.Font("Tahoma", 0, 14)); 
        menuNewGameButton.setText("New game");
        menuNewGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewGameButtonActionPerformed(evt);
            }
        });

        greetings.setFont(new java.awt.Font("Tahoma", 1, 12)); 
        greetings.setText("Welcome to Saperska Mustard!");

        bragLabel.setFont(new java.awt.Font("Tahoma", 0, 10)); 
        bragLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        bragLabel.setText("by Filip Matracki and Ksawery Jasieñski, 2016");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(greetings, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menuNewGameButton, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menuJoinRandGameButton, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(menuExitGameButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 28, Short.MAX_VALUE)
                        .addComponent(bragLabel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(greetings, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addComponent(menuNewGameButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuJoinRandGameButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuExitGameButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(bragLabel))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void menuExitGameButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                   

    	System.exit(0);
    	
    }                                                  

    private void menuJoinRandGameButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                       
        menuJoinRandGameButton.setText("<html>this does not do anything yet :(</html>");
    }                                                      

    private void menuNewGameButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        
        TheFrameInWhichYouCreateANewTable next = new TheFrameInWhichYouCreateANewTable();
        next.start();
        this.dispose();
        
    }                                                 

    public static void run() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private JLabel bragLabel;
    private JLabel greetings;
    private JButton menuExitGameButton;
    private JButton menuJoinRandGameButton;
    private JButton menuNewGameButton;
    // End of variables declaration                   
}
