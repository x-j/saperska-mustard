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
	char content = 'd'; //'d' is a placeholder until we get the real info from Board.
	static ArrayList<SquareButton>ALL_SQUAREBUTTONS = new ArrayList<>();
	static boolean gameOver = false;
	boolean uncovered = false;
	boolean flagged = false;
	int i, j;

	ActionListener squareActionListener = new ActionListener() {


		@Override
		public void actionPerformed(ActionEvent e) {

			board.click(i, j);  //very simple - we send message to the Board that we clicked a square
//              the Board will then send this information to the server.
		}

	};  //handles regular leftclicking on this square.



	public SquareButton( Board board) {

		this.board = board;
		this.setFont(new Font("Tahoma", Font.BOLD, 13));
		this.setPreferredSize(new Dimension(20, 20));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.setEnabled(false);
		this.setVisible(true);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.addActionListener(squareActionListener);
		this.addMouseListener( new MouseListener() {

			@Override
			public void mouseReleased( MouseEvent e ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed( MouseEvent e ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited( MouseEvent e ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered( MouseEvent e ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked( MouseEvent e ) {
				if ( SwingUtilities.isRightMouseButton(e) ) {
					if ( flagged ) {
						setText("");
						board.gui.minesLeftLabel.setText(( board.gui.counterOfMinesLeft-- ) + "");

					} else {
						if ( board.gui.counterOfMinesLeft != 0 ) {
							setText("F");
							board.gui.minesLeftLabel.setText(( board.gui.counterOfMinesLeft-- ) + "");
						}
					}
				}
			}
		});    /* this MouseListener handles flagging. */
		ALL_SQUAREBUTTONS.add(this);
	}
	
	public void reveal() {
		uncovered = true;
		this.setEnabled(false);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setText(""+content);
		if(content == 'm' && gameOver == false){
			gameOver = true;
			board.gameOver = true;
			JOptionPane.showMessageDialog(this.getParent(), "Lol, you lost XD");
			for (SquareButton sb : SquareButton.ALL_SQUAREBUTTONS) {
				sb.setEnabled(false);
			}
		}
	}

	private ArrayList<SquareButton> getNeighbours() {
		ArrayList<SquareButton> list = new ArrayList<>();

		int i2 = i;
		int j2 = j;
		i2++;
		if (board.squareExists(i2, j2)) list.add(getSquareAt(i2,j2));
		j2++;
		if (board.squareExists(i2, j2)) list.add(getSquareAt(i2,j2));
		i2--;
		if (board.squareExists(i2, j2)) list.add(getSquareAt(i2,j2));
		i2--;
		if (board.squareExists(i2, j2)) list.add(getSquareAt(i2,j2));
		j2--;
		if (board.squareExists(i2, j2)) list.add(getSquareAt(i2,j2));
		j2--;
		if (board.squareExists(i2, j2)) list.add(getSquareAt(i2,j2));
		i2++;
		if (board.squareExists(i2, j2)) list.add(getSquareAt(i2,j2));
		i2++;
		if (board.squareExists(i2, j2)) list.add(getSquareAt(i2,j2));

		return list;
	}

	private SquareButton getSquareAt(int i2, int j2) {
		for (SquareButton square : ALL_SQUAREBUTTONS)
			if ( square.i == i2 && square.j == j2 ) return square;
		return null;
	}

}
