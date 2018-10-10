/**
 * Author: Travis Banken
 * Board.java
 * 
 * 
 */

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

public class Board {
	private final int boardSize = 10;
	private int numBombs = 10;
	//private int bombsLeft = 5;
	private int[][] currentBoard = new int[boardSize][boardSize];
	
	public Board() {
		initializeBoard();
	}
	
//	public int getBombsLeft() {
//		return bombsLeft;
//	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	public int[][] getCurrentBoard() {
		return currentBoard;
	}
	

	
	private void initializeBoard() {
		Random rand = new Random();
		while (numBombs > 0) {
			int bombPosX = rand.nextInt(boardSize);
			int bombPosY = rand.nextInt(boardSize);
			if (currentBoard[bombPosY][bombPosX] != -1) {
				System.out.println("Bomb placed at [" + bombPosX + ", " + bombPosY + "]");
				currentBoard[bombPosY][bombPosX] = -1;
				numBombs--;
			}
		}
		
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (currentBoard[j][i] != -1) {
					currentBoard[j][i] = checkNearTiles(i, j);
				}
			}
		}
		
//		// debug: print out board
//		for (int i = 0; i < boardSize; i++) {
//			for (int j = 0; j < boardSize; j++) {
//				System.out.print(currentBoard[j][i]);
//			}
//			System.out.println();
//		}
		
	}
	
	private int checkNearTiles(int x, int y) {
		int count = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!outOfBounds(i, j, x, y) && currentBoard[i + y][j + x] == -1) {
					count++;
				}
			}
		}
		return count;
	}
	
	private boolean outOfBounds(int i, int j, int x, int y) {
		if (i + y < 0 || i + y >= boardSize) {
			return true;
		}
		if (j + x < 0 || j + x >= boardSize) {
			return true;
		}
		return false;
	}
	
//	private void placeFlag(int X, int Y) {
//		currentBoard[Y][X] = 2;
//		bombsLeft--;
//	}
	
	
}
