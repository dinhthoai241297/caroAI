/**
  * @author		ThoaiDP
  * @copyright	2018 ThoaiDP
  * @version	1.0
  */
package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
	private JFXButton btnDebug;

	@FXML
	private JFXButton btnNext;
	
	@FXML
	private BorderPane bpDebug;
	
	@FXML
	private BorderPane bpMain;
	
	private VBox vbDebug;
	
	@FXML
	private Label lbDebug;
	
	@FXML
	private BorderPane bpRoot;
	
	@FXML
	private ScrollPane spDebug;
	
	private MainBoard mb;
	
	private AlphaBeta search;
	
	private int size;
	
	private MyButton[][] myBtn;
	
	private Stage stage;
	
	public Controller() {
		this.size = 20;
		this.mb = new MainBoard(this.size);
		search = new AlphaBeta(this.size, 3);
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void newBoard() {
		vbDebug = new VBox();
		vbDebug.setPadding(new Insets(5));
		spDebug.setContent(vbDebug);
		btnDebug.setVisible(false);
		this.myBtn = new MyButton[this.size][this.size];
		GridPane gp = new GridPane();
		gp.setVgap(3);
		gp.setHgap(3);
		gp.setPadding(new Insets(10));
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				MyButton btn = new MyButton(i, j);
				btn.setOnMouseClicked(e -> {
					if (!mb.isOver() && mb.checkClick(btn.getNode())) {
						log("Player " + mb.getTurn() + " : " + btn.getNode().getX() + " - " + btn.getNode().getY());
						mb.clearNext();
						updateView(btn, mb.set(btn.getNode()));
						if (mb.isPlayWithAI() && !mb.isOver()) {
							// AI
							Node tmp = search.getBestNode();
							log("Player " + mb.getTurn() + " : " + tmp.getX() + " - " + tmp.getY());
							mb.clearNext();
							updateView(myBtn[tmp.getX()][tmp.getY()], mb.set(tmp));
						}
					} else {
						System.out.println("disabled!");
					}
				});
				
				btn.setOnMouseEntered(e -> {
					this.lbDebug.setText(btn.getNode().getX() + " - " + btn.getNode().getY() + " : " + search.getEvaluteNode()[btn.getNode().getX()][btn.getNode().getY()]);
				});
				gp.add(btn, j, i);
				this.myBtn[i][j] = btn;
			}
		}
		mainBoard.getChildren().clear();
		mainBoard.getChildren().add(gp);
	}
	
	public void log(String st) {
		vbDebug.getChildren().add(new Label(st));
		spDebug.vvalueProperty().bind(vbDebug.heightProperty());
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
		FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("GAME file(*.pdt)", "*.pdt");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(extFilter);
		fc.setTitle("Save Game");
		fc.setInitialFileName("gameCaro.pdt");
		
		File file = fc.showSaveDialog(this.stage);
		
		System.out.println(file);
		String mes = "";
		if (file != null) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				oos.writeObject(mb);
				oos.close();
				mes = "Lưu game thành công!";
			} catch (IOException e) {
				mes = "Lưu game thất bại!";
				e.printStackTrace();
			}
		} else {
			mes = "Đường dẫn không hợp lệ!";
		}
		Alert alert = new Alert("Lưu game thành công!".equals(mes) ? AlertType.INFORMATION : AlertType.WARNING);
		alert.setTitle("Thông báo!");
		alert.setHeaderText(null);
		alert.setContentText(mes);
		alert.showAndWait();
	}

	public void load() {
		System.out.println("load");
		FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("GAME file(*.pdt)", "*.pdt");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(extFilter);
		fc.setTitle("Load Game");
		
		File file = fc.showOpenDialog(this.stage);
		String mes = "File không hợp lệ!";
		if (file == null || !file.exists() || file.isDirectory()) {
			System.out.println("file not found");
		} else {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				this.mb = (MainBoard) ois.readObject();
				ois.close();
				updateLoadGame();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void newGame() {
		refreshBoard();
		mb.newGame();
		if (mb.isPlayWithAI()) {
			search.setBoard(mb.getBoard());
		}
		vbDebug.getChildren().clear();
		lbDebug.setText("");
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
	

	private void updateLoadGame() {
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (this.mb.getBoard()[i][j] == 0) {
					this.myBtn[i][j].resetBtn();
				} else {
					this.myBtn[i][j].updateClick(this.mb.getBoard()[i][j] == 1 ? MyButton.XICON : MyButton.OICON);
				}
			}
		}
		updateView(null, this.mb.isOver());
	}
	
	public void toggleDebug() {
//		bpDebug.setVisible(!bpDebug.isVisible());
//		bpDebug.managedProperty().bind(bpDebug.visibleProperty());
//		bpRoot.managedProperty().bind(bpDebug.visibleProperty());
//		bpRoot.autosize();
	}
}
