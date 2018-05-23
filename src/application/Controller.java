/**
  * @author		ThoaiDP
  * @copyright	2018 ThoaiDP
  * @version	1.0
  */
package application;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Controller {

	@FXML
	private Pane mainBoard;

	@FXML
	private ToggleGroup playMode;

	@FXML
	private Label lbMes;
	
	@FXML
	private JFXButton btnPre;

	@FXML
	private JFXButton btnNext;
	
	private MainBoard mb;
	
	private AlphaBeta search;
	
	private int size;
	
	private MyButton[][] myBtn;
	
	public Controller() {
		this.size = 20;
		this.mb = new MainBoard(this.size);
		search = new AlphaBeta(this.size, 3);
	}
	
	public void newBoard() {
		this.myBtn = new MyButton[this.size][this.size];
		GridPane gp = new GridPane();
		gp.setVgap(3);
		gp.setHgap(3);
		gp.setPadding(new Insets(15, 5, 5, 15));
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				MyButton btn = new MyButton(i, j);
				btn.setOnMouseClicked(e -> {
					if (!mb.isOver() && mb.checkClick(btn.getNode())) {
						mb.clearNext();
						updateView(btn, mb.set(btn.getNode()));
						if (mb.isPlayWithAI() && !mb.isOver()) {
							// AI
							Node tmp = search.getBestNode();
							mb.clearNext();
							updateView(myBtn[tmp.getX()][tmp.getY()], mb.set(tmp));
						}
					} else {
						System.out.println("disabled!");
					}
				});
				gp.add(btn, j, i);
				this.myBtn[i][j] = btn;
			}
		}
		mainBoard.getChildren().clear();
		mainBoard.getChildren().add(gp);
	}
	
	public void refreshBoard() {
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.myBtn[i][j].resetBtn();
			}
		}
	}

	public void pre() {
		System.out.println("pre");
		for (int i = 0; i < (mb.isPlayWithAI() ? 2 : 1); i++) {
			Node tmp = mb.preNode();
			this.myBtn[tmp.getX()][tmp.getY()].resetBtn();
			updateView(null, false);
		}
	}

	public void next() {
		System.out.println("next");
		for (int i = 0; i < (mb.isPlayWithAI() ? 2 : 1); i++) {
			Node tmp = mb.nextNode();
			updateView(myBtn[tmp.getX()][tmp.getY()], mb.set(tmp));
		}
	}

	public void save() {
		System.out.println("save");

	}

	public void load() {
		System.out.println("load");
	}

	public void newGame() {
		refreshBoard();
		mb.newGame();
		if (mb.isPlayWithAI()) {
			search.setBoard(mb.getBoard());
		}
		updateView(null, false);
	}

	public void playModePP() {
		System.out.println("pp");
		mb.setPlayWithAITmp(false);
	}

	public void playModePA() {
		System.out.println("pa");
		mb.setPlayWithAITmp(true);
	}
	
	public void history() {
		System.out.println("history");
	}
	
	public void updateView(MyButton btn, boolean won) {
		if (btn != null) {
			btn.updateClick(this.mb.getTurn() == 2 ? MyButton.XICON : MyButton.OICON);
		}
		lbMes.setText(won ? "Game Over!" : this.mb.getTurn() == 1 ? "Player 1" : "Player 2");
		btnPre.setDisable(mb.nullPre());
		btnNext.setDisable(mb.nullNext());
	}
}
