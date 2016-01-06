package saperskaMustard;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {

	Square[][] squares;
	public int numberOfMines = 0;
	public int flagsDeployed = 0;
	int boardSize;
	Player currentPlayer;

	public Board(int boardSize, String usernameOfHost) {
		this.boardSize = boardSize;
		currentPlayer = new Player(usernameOfHost);
		squares = new Square[boardSize][boardSize];
		numberOfMines = (int) (Math.pow(boardSize, 2) * 0.18);
		char[] tempArrayThatHelpsSetUpBombs = new char[(int) Math.pow(boardSize, 2)];
		char[][] anotherTempArray = new char[boardSize][boardSize];
		Arrays.fill(tempArrayThatHelpsSetUpBombs, '-');
		System.out.println("SettingUpBombs...");
		setUpBombs(tempArrayThatHelpsSetUpBombs, anotherTempArray);
		setUpNumbers(anotherTempArray);
		createSquares(anotherTempArray);
	}

	private void setUpNumbers(char[][] anotherTempArray) {

		for (int i = 0; i < anotherTempArray.length; i++) {
			for (int j = 0; j < anotherTempArray.length; j++) {
				if (anotherTempArray[i][j] != 'm') {
					ArrayList<Character> neighbours = getNeighbours(i, j, anotherTempArray);
					int mineCounter = 0;
					for (char c : neighbours)
						if (c == 'm') mineCounter++;
					anotherTempArray[i][j] = (char) (mineCounter + '0');
				}
			}
		}
	}

	private void setUpBombs(char[] tempArrayThatHelpsSetUpBombs, char[][] anotherTempArray) {

		for (int i = 0; i < numberOfMines; i++) {
			int index;
			do {
				System.out.println("randomizing an index for a mine now");
				index = (int) (Math.random() * tempArrayThatHelpsSetUpBombs.length);
			} while (tempArrayThatHelpsSetUpBombs[index] == 'm');
			tempArrayThatHelpsSetUpBombs[index] = 'm';
		}
		int i = 0;
		for (int j = 0; j < tempArrayThatHelpsSetUpBombs.length; j++) {
			if (j % boardSize == 0 && j != 0) i++;
			if (tempArrayThatHelpsSetUpBombs[j] == 'm') anotherTempArray[i][j % boardSize] = 'm';
		}

	}

	private ArrayList<Character> getNeighbours(int i, int j, char[][] anotherTempArray) {

		ArrayList<Character> neighbours = new ArrayList<>();

		i++;
		if (squareExists(i, j)) neighbours.add(anotherTempArray[i][j]);
		j++;
		if (squareExists(i, j)) neighbours.add(anotherTempArray[i][j]);
		i--;
		if (squareExists(i, j)) neighbours.add(anotherTempArray[i][j]);
		i--;
		if (squareExists(i, j)) neighbours.add(anotherTempArray[i][j]);
		j--;
		if (squareExists(i, j)) neighbours.add(anotherTempArray[i][j]);
		j--;
		if (squareExists(i, j)) neighbours.add(anotherTempArray[i][j]);
		i++;
		if (squareExists(i, j)) neighbours.add(anotherTempArray[i][j]);
		i++;
		if (squareExists(i, j)) neighbours.add(anotherTempArray[i][j]);

		return neighbours;
	}

	private boolean squareExists(int i, int j) {
		if (i < 0 || j < 0) return false;
		if (i >= boardSize || j >= boardSize) return false;
		return true;
	}

	private void createSquares(char[][] arr) {
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {
				Square newSquare = new Square(arr[i][j], i, j, this);
				squares[i][j] = newSquare;
			}
		}

	}

}
