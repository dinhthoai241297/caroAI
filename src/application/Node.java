/**
  * @author		ThoaiDP
  * @copyright	2018 ThoaiDP
  * @version	1.0
  */
package application;

import java.io.Serializable;

public class Node implements Serializable {
	
	private int x;
	private int y;
	private int val;
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Node(int x, int y, int val) {
		this.x = x;
		this.y = y;
		this.val = val;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}
}
