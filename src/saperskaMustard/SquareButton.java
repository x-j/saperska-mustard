package saperskaMustard;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class SquareButton extends JButton {

    Board board;
    int content = -1; //-1 is a placeholder until we get the real info from Board.
    static ArrayList<SquareButton> ALL_SQUAREBUTTONS = new ArrayList<>();
    static boolean gameOver = false;
    boolean uncovered = false;
    boolean flagged = false;
    int i, j;
    private static String filenames[] = new String[12];
    private static Icon icons[] = new ImageIcon[12];

    ActionListener squareActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            System.out.println("A button at " + i + ", " + j + " was pressed by " + board.clientUsername);
            board.click(i, j);  //very simple - we send message to the Board that we clicked a square
//              the Board will then send this information to the server.
        }

    };  //handles regular leftclicking on this square.
    MouseListener squareRClickListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e) && board.hasBegun) {
                if (flagged) {
                    flagged = false;
                    ((SquareButton) e.getSource()).setDisabledIcon(null);
                    ((SquareButton) e.getSource()).setIcon(null);
                    ((SquareButton) e.getSource()).setEnabled(true);
                    board.gui.minesLeftLabel.setText((++board.gui.counterOfMinesLeft) + " mines left.");

                } else {
                    if (board.gui.counterOfMinesLeft != 0) {
                        flagged = true;
                        ((SquareButton) e.getSource()).setEnabled(false);
                        ((SquareButton) e.getSource()).drawIcon(Board.FLAG);
                        board.gui.minesLeftLabel.setText((--board.gui.counterOfMinesLeft) + " mines left.");
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };  /* this MouseListener handles flagging. */

    private void drawIcon(int arg) {
        ImageIcon newIcon = new ImageIcon("/icons/placeholder.jpg");
        this.setDisabledIcon(newIcon);
    }

    public SquareButton(final Board board, int i, int j) {

        this.board = board;
        this.i = i;
        this.j = j;
        this.setFont(new Font("Tahoma", Font.BOLD, 13));
        this.setPreferredSize(new Dimension(20, 20));
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.setEnabled(false);
        this.setVisible(true);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.addActionListener(squareActionListener);
        this.addMouseListener(squareRClickListener);
        ALL_SQUAREBUTTONS.add(this);
    }

    public void reveal() {
        uncovered = true;
        this.setDisabledIcon(icons[content]);
        this.setEnabled(false);
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        if (content == Board.MINE && !gameOver) {
            gameOver = true;
            board.gameOver = true;
            JOptionPane.showMessageDialog(this.getParent(), "Lol, you lost XD");
            for (SquareButton sb : SquareButton.ALL_SQUAREBUTTONS) {
                sb.setEnabled(false);
            }
        }
    }

    public static void setUpIcons() {

        for (int i = 0; i < 9; i++) filenames[i] = "/icons/" + i + ".jpg";
        filenames[9] = "/icons/flag.jpg";
        filenames[10] = "/icons/mine.jpg";
        filenames[11] = "/icons/placeholder.jpg";

        for (int i = 0; i < 12; i++) {
            icons[11] = new ImageIcon(filenames[11]);  //later change this to filenames[i]
            icons[11].getClass().getResource(filenames[11]);
        }
    }   //todo once we get the actual graphics, change this method

}