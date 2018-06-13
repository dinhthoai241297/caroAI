/**
  * @author		ThoaiDP
  * @copyright	2018 ThoaiDP
  * @version	1.0
  */
package application;

import com.jfoenix.controls.JFXButton;

public class MyButton extends JFXButton {

	private Node node;
	
	private int size = 28;

	public static final String XICON = "application/x.png";
	public static final String OICON = "application/o.png";

	public MyButton(int x, int y) {
		node = new Node(x, y);
		this.getStyleClass().add("node");
		setPrefSize(this.size, this.size);
	}

	public void updateClick(String icon) {
		setStyle("-fx-background-image: url('" + icon + "')");
	}

	public Node getNode() {
		return this.node;
	}
	
	public void resetBtn() {
		this.setStyle("");
	}
}
