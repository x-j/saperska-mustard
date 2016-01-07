package saperskaMustard;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class Square{

	static ArrayList<Square>ALL_SQUARES = new ArrayList<>();
	char content = 'd';
	boolean uncovered = false;
	Board board;
	int i, j; //coordinates
	

	public Square(char c, int i, int j, Board board) {
		if(c=='0') content = ' ';
		else content = c;
		this.board = board;
		this.i = i;
		this.j = j;
		ALL_SQUARES.add(this);
	}


	void uncover() {
		uncovered = true;
		if(content == 'm')
			board.gameOver = true;
	}


	private ArrayList<Square> getNeighbours() {
		ArrayList<Square> list = new ArrayList<>();
		
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

	private Square getSquareAt(int i2, int j2) {
		for (Square square : ALL_SQUARES) {
			if(square.i == i2 && square.j == j2) return square;
		}
		return null;
	}
	
}
