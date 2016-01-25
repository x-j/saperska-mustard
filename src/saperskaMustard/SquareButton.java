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

    // TODO: 2016-01-23 CHANGE THE MIDDLE MOUSE BUTTON CLICK LISTENER TO BE MORE TRUE WITH ORIGINAL MINESWEEPER

    public static ArrayList<SquareButton> ALL_SQUAREBUTTONS = new ArrayList<>();

    private static String filenames[] = new String[12];
    private static Icon icons[] = new ImageIcon[12];

    private Board board;
    private int content = -1; // 11 is a placeholder until we get the real info from Board.
    private boolean uncovered = false;
    private boolean flagged = false;
    private int i, j;

    ActionListener squareActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isEnabled()) {
                System.out.println("A button at " + i + ", " + j + " was pressed by " + board.getClientUsername());
                board.makeClick(i, j); // very simple - we send message to the Board that we clicked a square
                // the Board will then send this information to the server.
            }
        }

    }; // handles regular leftclicking on this square.
    MouseListener squareOtherClickListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e) && board.hasBegun() && !uncovered && !board.isGameOver()) {
                if (flagged) {
                    flagged = false;

                    ((SquareButton) e.getSource()).clean();
                    ((SquareButton) e.getSource()).setEnabled(true);
                    board.getGui().incrementCounterOfMinesLeft();
                    board.getGui().getMinesLeftLabel().setText((board.getGui().getCounterOfMinesLeft()) + " mines left.");

                } else {
                    if (board.getGui().getCounterOfMinesLeft() != 0) {
                        flagged = true;
                        ((SquareButton) e.getSource()).setEnabled(false);
                        ((SquareButton) e.getSource()).drawIcon(Board.FLAG);
                        board.getGui().decrementCounterOfMinesLeft();
                        board.getGui().getMinesLeftLabel().setText((board.getGui().getCounterOfMinesLeft()) + " mines left.");
                    }
                }
            }

            if (SwingUtilities.isMiddleMouseButton(e) && uncovered && !board.isGameOver()) {

                board.uncoverEmptyAdjacent(i, j);

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
    }; // this MouseListener handles flagging and middle mouse button clicks.

    public SquareButton(final Board board, int i, int j) {

        this.board = board;
        this.i = i;
        this.j = j;
        this.setFont(new Font("Tahoma", Font.BOLD, 13));
        this.setPreferredSize(new Dimension(25, 25));
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.setEnabled(false);
        this.setVisible(true);
        this.setSize(25, 25);
        this.setMaximumSize(new Dimension(25, 25));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.addActionListener(squareActionListener);
        this.addMouseListener(squareOtherClickListener);
        ALL_SQUAREBUTTONS.add(this);
    }

    public static void setUpIcons() {

        for (int i = 0; i < 9; i++)
            filenames[i] = "/icons/" + i + ".jpg";

        filenames[9] = "/icons/flag.jpg";
        filenames[10] = "/icons/mine.jpg";
        filenames[11] = "/icons/placeholder.jpg";

        for (int i = 0; i < 12; i++) {
            icons[i] = new ImageIcon(filenames[11]); // later change this to filenames[i]
            icons[i].getClass().getResource(filenames[11]);
        }
    } // todo once we get the actual graphics, change this method

    private void drawIcon(int arg) {

        if (arg != 0 && arg <= 8) this.setText("<html><font color = \"red\">" + arg + "</font></html>");
        else if (arg == 9)
            setText("<html><font color = \"blue\">F</font></html>");    //I HAVE NO IDEA WHY THIS IS NOT WORKING
        else if (arg == 10) setText("<html><font color = \"black\">M</font></html>");
        else if (arg == 0) setText("");
        this.setIcon(icons[arg]);
        this.setPressedIcon(icons[arg]);
        this.setDisabledIcon(icons[arg]);
    }

    private void clean() {

        setText("");
        setDisabledIcon(null);
        setIcon(null);

    }

    public void reveal() {
        this.board.getGui().pack();
        if (!flagged) {
            uncovered = true;
            this.drawIcon(content);
            this.setEnabled(false);
            this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            board.uncoverEmptyAdjacent(i, j);
            if (content == Board.MINE && !board.isGameOver()) board.loseTheGame();
        }
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public boolean isUncovered() {
        return uncovered;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}