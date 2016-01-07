package saperskaMustard;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class SquareButton extends JButton {
	
	Game game;
	private static ArrayList<SquareButton>ALL_SQUAREBUTTONS = new ArrayList<>();
	static boolean gameOver = false;
	boolean uncovered = false;
	boolean flagged = false;
	int i, j;

	ActionListener squareActionListener = new ActionListener() {


		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(game.firstClick = false){
				
				//sends to server a request: i clicked square i, j
				//please create a Board which does not have a mine on i, j
				//and receives info: a Square s
				//reveal(s);
				
			}else{
				
				//sends to server a request: what is the Square on i, j
				//receives info: a Square s from server??
				//reveal(s)
				
			}
		}

	};

		
	public SquareButton(Game game) {

		this.game = game;
		this.setFont(new Font("Tahoma", Font.BOLD, 13));
		this.setPreferredSize(new Dimension(20, 20));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.setEnabled(false);
		this.setVisible(true);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.addActionListener(squareActionListener);
		ALL_SQUAREBUTTONS.add(this);
	}
	
	public static void gameStart() {
		for (SquareButton sB : ALL_SQUAREBUTTONS) 
			sB.setEnabled(true);
	}
	
	public static void notYourTurn(){
		for (SquareButton sB : ALL_SQUAREBUTTONS) 
			sB.setEnabled(false);
	}
	
	private void reveal(Square s) {
		uncovered = true;
		this.setEnabled(false);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setText(""+s.content);
		if(s.content == 'm' && gameOver == false){
			gameOver = true;
			JOptionPane.showMessageDialog(this.getParent(), "Lol, you lost XD");
			for (SquareButton sb : SquareButton.ALL_SQUAREBUTTONS) {
				sb.setEnabled(false);
			}
		}
	}

}
