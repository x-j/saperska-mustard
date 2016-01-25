package saperskaMustard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ServerGUI extends javax.swing.JFrame {

    private JPanel mainPanel;
    private JTextField messageInputField;
    private JButton exitButton;
    private JLabel theServerRunningLabel;
    private JLabel dog;
    private JLabel explaininglabel;
    private JScrollPane statusPane;
    private JTextArea textArea;
    private JLabel clientsConnectedLabel;
    private JScrollBar verticalScrollBar;


    public ServerGUI() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        initComponents();
        addActionListeners();
        setVisible(true);
        setResizable(false);
        setTitle("Multiplayer Minesweeper server");
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        textArea = new JTextArea();
        statusPane.add(textArea);
        statusPane.getViewport().setView(textArea);
        textArea.setVisible(true);
        textArea.setEditable(false);
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
                    MinesweeperThreadedServer.sendToAll(message);

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
        if (!newStatus.endsWith(": ") && !newStatus.endsWith("> ")) {
            textArea.append("~" + newStatus + "\n");
            textArea.setCaretPosition(textArea.getText().length());
        }
    }

    public void updateClientsConnectedLabel() {
        String newText = clientsConnectedLabel.getText();
        newText = newText.substring(newText.indexOf(' '));
        newText = MinesweeperThreadedServer.getAllClients().size() + newText;
        clientsConnectedLabel.setText(newText);
    }
}
