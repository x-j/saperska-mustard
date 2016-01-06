package saperskaMustard;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class Square extends JButton{

	static ArrayList<Square>ALL_SQUARES = new ArrayList<>();
	char content = 'd';
	boolean uncovered = false;
	Board board;
	ActionListener  squareActionListener= new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			((Square) e.getSource()).uncover();		
		}
		
	};
	int i, j; //coordinates
	

	public Square(char c, int i, int j, Board board) {
		if(c=='0') content = ' ';
		else content = c;
		this.board = board;
		this.i = i;
		this.j = j;
		createButton();
		ALL_SQUARES.add(this);
	}

	private void createButton() {
		this.setFont(new Font("Tahoma", Font.BOLD, 13));
		this.setPreferredSize(new Dimension(20, 20));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.setEnabled(true);
		this.setVisible(true);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.addActionListener(squareActionListener);
	}

	void uncover() {
		reveal();
		if(content == 'm'){
			JOptionPane.showMessageDialog(this.getParent(), "Lol, you lost XD");
			for (Square square : ALL_SQUARES) {
				if(square.content == 'm') square.reveal();
				square.setEnabled(false);
			}
		}
	}

	private void reveal() {
		uncovered = true;
		this.setEnabled(false);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setText(""+content);		
	}
	
}
