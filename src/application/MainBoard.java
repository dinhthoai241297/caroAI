/**
  * @author		ThoaiDP
  * @copyright	2018 ThoaiDP
  * @version	1.0
  */
package application;

import java.util.Stack;

public class MainBoard {

	public static final int PR1 = 1;
	public static final int PR2 = 2;

	private int turn;
	private int[][] board;
	private int size;
	private boolean playWithAI;
	private boolean playWithAITmp;
	private boolean isOver;

	private Stack<Node> preStack;
	private Stack<Node> nextStack;

	public MainBoard(int size) {
		this.size = size;
		this.playWithAITmp = false;
	}

	public void newGame() {
		this.turn = PR1;
		this.board = new int[this.size][this.size];
		this.isOver = false;
		this.preStack = new Stack<>();
		this.nextStack = new Stack<>();
		this.playWithAI = this.playWithAITmp;
	}

	public boolean set(Node node) {
		boolean won = false;
		int x = node.getX(), y = node.getY();
		this.board[x][y] = this.turn;
		won = checkWin(node);
		this.preStack.add(node);
		if (won) {
			this.isOver = true;
		}
		switchTurn();
		return won;
	}

	public Node preNode() {
		if (!this.preStack.isEmpty()) {
			Node tmp = this.preStack.pop();
			this.nextStack.add(tmp);
			switchTurn();
			this.isOver = false;
			this.board[tmp.getX()][tmp.getY()] = 0;
			return tmp;
		} else {
			return null;
		}
	}

	public Node nextNode() {
		if (!this.nextStack.isEmpty()) {
			Node tmp = this.nextStack.pop();
			this.preStack.add(tmp);
			return tmp;
		} else {
			return null;
		}
	}

	public boolean nullPre() {
		return this.preStack.isEmpty();
	}

	public boolean nullNext() {
		return this.nextStack.isEmpty();
	}

	public boolean checkValid(int x, int y) {
		return x > -1 && x < this.size && y > -1 && y < this.size;
	}

	public boolean checkClick(Node node) {
		int x = node.getX(), y = node.getY();
		return checkValid(x, y) && this.board[x][y] == 0;
	}

	public void switchTurn() {
		this.turn = this.turn == 1 ? 2 : 1;
	}

	public boolean checkWin(Node node) {
		int x = node.getX(), y = node.getY();
		// dọc
		int count1, count2;
		count1 = 1;
		count2 = 0;
		for (int i = 1; i < 7; i++) {
			if (checkValid(x - i, y)) {
				if (this.board[x - i][y] == this.turn) {
					count1++;
				} else {
					if (this.board[x - i][y] != 0) {
						count2++;
					}
					break;
				}
			}
		}

		for (int i = 1; i < 7; i++) {
			if (checkValid(x + i, y)) {
				if (this.board[x + i][y] == this.turn) {
					count1++;
				} else {
					if (this.board[x + i][y] != 0) {
						count2++;
					}
					break;
				}
			}
		}

		if (count1 == 5 && count2 < 2) {
			return true;
		}

		// ngang
		count1 = 1;
		count2 = 0;

		for (int i = 1; i < 7; i++) {
			if (checkValid(x, y - i)) {
				if (this.board[x][y - i] == this.turn) {
					count1++;
				} else {
					if (this.board[x][y - i] != 0) {
						count2++;
					}
					break;
				}
			}
		}

		for (int i = 1; i < 7; i++) {
			if (checkValid(x, y + i)) {
				if (this.board[x][y + i] == this.turn) {
					count1++;
				} else {
					if (this.board[x][y + i] != 0) {
						count2++;
					}
					break;
				}
			}
		}

		if (count1 == 5 && count2 < 2) {
			return true;
		}

		// chéo \

		count1 = 1;
		count2 = 0;

		for (int i = 1; i < 7; i++) {
			if (checkValid(x - i, y - i)) {
				if (this.board[x - i][y - i] == this.turn) {
					count1++;
				} else {
					if (this.board[x - i][y - i] != 0) {
						count2++;
					}
					break;
				}
			}
		}

		for (int i = 1; i < 7; i++) {
			if (checkValid(x + i, y + i)) {
				if (this.board[x + i][y + i] == this.turn) {
					count1++;
				} else {
					if (this.board[x + i][y + i] != 0) {
						count2++;
					}
					break;
				}
			}
		}

		if (count1 == 5 && count2 < 2) {
			return true;
		}

		// chéo /

		count1 = 1;
		count2 = 0;

		for (int i = 1; i < 7; i++) {
			if (checkValid(x + i, y - i)) {
				if (this.board[x + i][y - i] == this.turn) {
					count1++;
				} else {
					if (this.board[x + i][y - i] != 0) {
						count2++;
					}
					break;
				}
			}
		}

		for (int i = 1; i < 7; i++) {
			if (checkValid(x - i, y + i)) {
				if (this.board[x - i][y + i] == this.turn) {
					count1++;
				} else {
					if (this.board[x - i][y + i] != 0) {
						count2++;
					}
					break;
				}
			}
		}

		if (count1 == 5 && count2 < 2) {
			return true;
		}

		return false;
	}

	public int getTurn() {
		return this.turn;
	}

	public boolean isPlayWithAI() {
		return playWithAI;
	}

	public void setPlayWithAI(boolean playWithAI) {
		this.playWithAI = playWithAI;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	public void clearNext() {
		this.nextStack.clear();
	}

	public boolean isPlayWithAITmp() {
		return playWithAITmp;
	}

	public void setPlayWithAITmp(boolean playWithAITmp) {
		this.playWithAITmp = playWithAITmp;
	}

	public int[][] getBoard() {
		return this.board;
	}
}
