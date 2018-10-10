/**
 * Author: Travis Banken
 * MineSweeperMain.java
 * 
 * DRAING AN IMAGE:
 * graphicsContext.drawImage(image, x, y, w, h);
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MineSweeperMain extends Application {
	private static int NUM_RECT = -1;
	private static int RECT_SIZE = 80;
	private static int CANVAS_SIZE = -1;
	private static final int TEXT_SIZE = 20;
	
	// images for the tiles
	private static Image testImg;
	private static Image bombImg;
	private static Image flagImg;
	private static Image emptyImg;
	private static Image oneImg;
	private static Image twoImg;
	private static Image threeImg;
	private static Image fourImg;
	private static Image fiveImg;
	private static Image sixImg;
	private static Image sevenImg;
	private static Image eightImg;
	
	private static BorderPane bp;
	private static Button flagBtn;
	private static Board board;
	private static FakeBoard history;
	private static boolean flagState = false;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		board = new Board();
		NUM_RECT = board.getBoardSize();
		history = new FakeBoard(NUM_RECT);
		CANVAS_SIZE = NUM_RECT * RECT_SIZE;
		
		setupImages();
		GraphicsContext gc = setupStage(primaryStage);
		primaryStage.show();
		initializeBoard(gc);
		runGame(gc, board);
	}
	
	public void initializeBoard(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
		
		for (int x = 0; x < NUM_RECT; x++) {
			for (int y = 0; y < NUM_RECT; y++) {
				gc.setFill(Color.CADETBLUE);
				gc.fillRect(x * RECT_SIZE, y * RECT_SIZE, RECT_SIZE - 5, RECT_SIZE - 5);
			}
		}
	}
	
	public void runGame(GraphicsContext gc, Board board) {
		bp.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			double x = event.getX();
			double y = event.getY() - RECT_SIZE;
			
			System.out.println("x = " + x + ", y = " + y);
			
			if (y < 0) {
				return;
			}
			
			int xPos = (int)(x / RECT_SIZE) * RECT_SIZE;
			int yPos = (int)(y / RECT_SIZE) * RECT_SIZE;
			
			System.out.println("Clicked postion (" + xPos/RECT_SIZE + ", " + yPos/RECT_SIZE + ")");
			
			if (flagState) {
				gc.drawImage(flagImg, xPos, yPos, RECT_SIZE - 5, RECT_SIZE - 5);
			}
			else {
				// bomb hit
				if (board.getCurrentBoard()[yPos/RECT_SIZE][xPos/RECT_SIZE] == -1) {
					gc.drawImage(bombImg, xPos, yPos, RECT_SIZE - 5, RECT_SIZE - 5);
					Alert alert = new Alert(AlertType.INFORMATION, "YOU LOSE!", ButtonType.CLOSE);
					alert.showAndWait();
				}
				// no bomb
				else {
					int count = board.getCurrentBoard()[yPos/RECT_SIZE][xPos/RECT_SIZE];
					//System.out.println("COUNT = " + count);
					if (count == 0) {
						findNearTiles(gc, xPos, yPos);
						history.resetHistory();
					}
					else {
						placeImgTile(gc, xPos, yPos, count);
					}
				}
			}
		});
		
		flagBtn.setOnAction(event -> {
			flagState = !flagState;
		});
	}
	
	public void placeImgTile(GraphicsContext gc, int x, int y, int count) {
		switch (count) {
		case 0:
			gc.drawImage(emptyImg, x, y, RECT_SIZE - 5, RECT_SIZE - 5);
			break;
		case 1:
			gc.drawImage(oneImg, x, y, RECT_SIZE - 5, RECT_SIZE - 5);
			break;
		case 2:
			gc.drawImage(twoImg, x, y, RECT_SIZE - 5, RECT_SIZE - 5);
			break;
		case 3:
			gc.drawImage(threeImg, x, y, RECT_SIZE - 5, RECT_SIZE - 5);
			break;
		case 4:
			gc.drawImage(fourImg, x, y, RECT_SIZE - 5, RECT_SIZE - 5);
			break;
		case 5:
			gc.drawImage(fiveImg, x, y, RECT_SIZE - 5, RECT_SIZE - 5);
			break;
		case 6:
			gc.drawImage(sixImg, x, y, RECT_SIZE - 5, RECT_SIZE - 5);
			break;
		case 7:
			gc.drawImage(sevenImg, x, y, RECT_SIZE - 5, RECT_SIZE - 5);
			break;
		case 8:
			gc.drawImage(eightImg, x, y, RECT_SIZE - 5, RECT_SIZE - 5);
			break;
		}
		
	}
	
	public void findNearTiles(GraphicsContext gc, int x, int y) {
		int tileNum = board.getCurrentBoard()[y/RECT_SIZE][x/RECT_SIZE];
		if (tileNum != 0) {
			placeImgTile(gc, x, y, tileNum);
			return;
		}
		
		placeImgTile(gc, x, y, tileNum);
		history.mark(y/RECT_SIZE, x/RECT_SIZE);
		
		
		// could cause out of bounds problems
		if (x/RECT_SIZE - 1 >= 0 && !history.marked(y/RECT_SIZE, (x/RECT_SIZE - 1))) {
			findNearTiles(gc, (x/RECT_SIZE - 1)*RECT_SIZE, y);
		}
		if (x/RECT_SIZE + 1 < NUM_RECT && !history.marked(y/RECT_SIZE, (x/RECT_SIZE + 1))) {
			findNearTiles(gc, (x/RECT_SIZE + 1)*RECT_SIZE, y);
		}
		if (y/RECT_SIZE - 1 >= 0 && !history.marked((y/RECT_SIZE - 1), x/RECT_SIZE)) {
			findNearTiles(gc, x, (y/RECT_SIZE - 1)*RECT_SIZE);
		}
		if (y/RECT_SIZE + 1 < NUM_RECT && !history.marked((y/RECT_SIZE + 1), x/RECT_SIZE)) {
			findNearTiles(gc, x, (y/RECT_SIZE + 1)*RECT_SIZE);
		}
	}
	
	public GraphicsContext setupStage(Stage primaryStage) {
		bp = new BorderPane();
		
		Canvas canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
		
		HBox top = new HBox(2);
		
		TextArea text = new TextArea();
		text.setPrefSize((NUM_RECT * RECT_SIZE)/2, RECT_SIZE);
		text.setEditable(false);
		
		flagBtn = new Button("Toggle Flag");
		flagBtn.setPrefSize((NUM_RECT * RECT_SIZE)/2, RECT_SIZE);
		
		top.getChildren().add(text);
		top.getChildren().add(flagBtn);
		
		bp.setCenter(canvas);
		bp.setTop(top);
		
		primaryStage.setTitle("Mine Sweeper");
		primaryStage.setScene(new Scene(bp));
		
		return canvas.getGraphicsContext2D();
		
	}
	
	public void setupImages() {
		try {
			testImg = new Image(new FileInputStream("Pics\\Test.png"));
			bombImg = new Image(new FileInputStream("Pics\\Bomb.png"));
			flagImg = new Image(new FileInputStream("Pics\\Flag.png"));
			emptyImg = new Image(new FileInputStream("Pics\\Empty.png"));
			oneImg = new Image(new FileInputStream("Pics\\One.png"));
			twoImg = new Image(new FileInputStream("Pics\\Two.png"));
			threeImg = new Image(new FileInputStream("Pics\\Three.png"));
			fourImg = new Image(new FileInputStream("Pics\\Four.png"));
			fiveImg = new Image(new FileInputStream("Pics\\Five.png"));
			sixImg = new Image(new FileInputStream("Pics\\Six.png"));
			sevenImg = new Image(new FileInputStream("Pics\\Seven.png"));
			eightImg = new Image(new FileInputStream("Pics\\Eight.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
