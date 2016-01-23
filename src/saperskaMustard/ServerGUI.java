package saperskaMustard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by xj on 2016-01-11.
 */
public class ServerGUI extends javax.swing.JFrame {

    MinesweeperThreadedServer server;
    private JPanel mainPanel;
    private JTextField messageInputField;
    private JButton exitButton;
    private JLabel theServerRunningLabel;
    private JLabel dog;
    private JLabel explaininglabel;
    private JScrollPane statusPane;
    private JTextArea textArea;


    public ServerGUI(MinesweeperThreadedServer minesweeperThreadedServer) {
        server = minesweeperThreadedServer;
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        initComponents();
        addActionListeners();
        setVisible(true);
        setResizable(false);
        setTitle("Saperska Mustard server");
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultLookAndFeelDecorated(true);
        add(mainPanel);
        pack();
    }

    private void addActionListeners() {
        messageInputField.addKeyListener(new KeyListener() {

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
                    message = "SERVER> " + message;
                    messageInputField.setText("");
                    server.sendToAll(message);

                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }

    public void addStatus(String newStatus) {
        textArea.append("~" + newStatus + "\n");
        statusPane.getVerticalScrollBar().setValue(statusPane.getVerticalScrollBar().getMaximum());
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

