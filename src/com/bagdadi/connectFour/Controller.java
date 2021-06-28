package com.bagdadi.connectFour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
	private static final int COLUMN = 7;
	private static final int ROW=6;
	private static final int diameter = 80;
	private static final String discColor1="#20303E";
	private static final String discColor2="#4CAA88";
	private static String player1="PLAYER 1";
	private static String player2="PLAYER 2";
	boolean isPlayer1Turn= true;
	private Disc[][] insertedDiscArray=new Disc[ROW][COLUMN];
	@FXML
	public GridPane rootGrid;
	@FXML
	public Pane discPane;
	@FXML
	public TextField playerOneTextField;
	@FXML
	public  TextField playerTwoTextField;
	@FXML
	public Button setNamesButton;

	private boolean isAllowedToInsert=true;
	public Label playerNameLabel;
	public void createPlayground()
	{
		Shape rectangle= gameStructuralGrid();
		rootGrid.add(rectangle,0, 1);
		List<Rectangle> rectangleList = clickableRectangle();
		for (Rectangle rectangle1:rectangleList) {
			rootGrid.add(rectangle1,0,1);
			setNamesButton.setOnAction(event -> {
				convert();
			});
		}

	}

	private void convert() {
           String input1=playerOneTextField.getText();
           String input2=playerTwoTextField.getText();
           if(isPlayer1Turn){
           	playerNameLabel.setText(input1);
           }
           else{
           	playerNameLabel.setText(input2);
           }
	}

	private Shape gameStructuralGrid(){
			Shape rectangle = new Rectangle((COLUMN+1)*diameter,(ROW+1)*diameter);
			for (int row=0; row<ROW;row++)
			{
				for (int col=0;col<COLUMN;col++)
				{
					Circle circle= new Circle();
					circle.setRadius(diameter/2);
					circle.setCenterX(diameter/2);
					circle.setCenterY(diameter/2);
					circle.setSmooth(true);
					circle.setTranslateX(col*(diameter+5)+diameter/4);
					circle.setTranslateY(row*(diameter+5)+diameter/4);
					rectangle= Shape.subtract(rectangle,circle);
				}
			}
			rectangle.setFill(Color.WHITE);
			return rectangle;
		}
private List<Rectangle> clickableRectangle() {
	List<Rectangle> rectangleList = new ArrayList<>();
	for (int col = 0; col < COLUMN; col++) {
		Rectangle rectangle1 = new Rectangle(diameter, (ROW + 1) * diameter);
		rectangle1.setFill(Color.TRANSPARENT);
		rectangle1.setTranslateX(col * (diameter + 5) + diameter / 4);
		rectangle1.setOnMouseEntered(event -> rectangle1.setFill(Color.valueOf("#eeeeee26")));
		rectangle1.setOnMouseExited(event -> rectangle1.setFill(Color.TRANSPARENT));
		final int column=col;
		rectangle1.setOnMouseClicked(event -> {
			if(isAllowedToInsert) {
				isAllowedToInsert=false;
				insertDisc(new Disc(isPlayer1Turn), column);
			}
		});
		rectangleList.add(rectangle1);
	}
	return rectangleList;
}
private void insertDisc(Disc disc,int column){
		int row =ROW-1;
		while (row>=0){
			if(getDiscIfPresent(row,column)==null){
				break;
			}
				row--;
			if(row<0)
			{
				return;
			}

		}
           insertedDiscArray[row][column]=disc;//for structural changes for developers
           disc.setTranslateX(column * (diameter + 5) + diameter / 4);
	TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
	translateTransition.setToY(row * (diameter + 5) + diameter / 4);
	final int currentRow=row;
	translateTransition.setOnFinished(event ->{
		isAllowedToInsert=true;
		if(gameEnded(currentRow,column)) {
			gameOver();
			return;
		}
		isPlayer1Turn=!isPlayer1Turn;
		playerNameLabel.setText(isPlayer1Turn?playerOneTextField.getText():playerTwoTextField.getText());

	});
	translateTransition.play();
           discPane.getChildren().add(disc);
}

	private boolean gameEnded(int row, int column) {
	List<Point2D> verticlePoints=	IntStream.rangeClosed(row-3,row+3)//for verticle case points ...4 points pair
			.mapToObj(r-> new Point2D(r,column))//store points in array ie Point2D
			.collect(Collectors.toList());
		List<Point2D> horizontalPoints=	IntStream.rangeClosed(column-3,column+3)//for horizontal case points ...4 points pair
				.mapToObj(col-> new Point2D(row,col))//store points in array ie Point2D
				.collect(Collectors.toList());

		Point2D startPoint1= new Point2D(row-3,column+3);
		List<Point2D> diagonal1Points=IntStream.rangeClosed(0,6)
				.mapToObj(i->startPoint1.add(i,-i))
				.collect(Collectors.toList());

		Point2D startPoint2= new Point2D(row-3,column-3);
		List<Point2D> diagonal2Points=IntStream.rangeClosed(0,6)
				.mapToObj(i->startPoint2.add(i,i))
				.collect(Collectors.toList());
		boolean isEnded= checkCombinations(verticlePoints) || checkCombinations(horizontalPoints)
				  ||checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);
		return isEnded;
	}

	private boolean checkCombinations(List<Point2D>Points)
	{
		int chain = 0;
		for (Point2D point : Points)
		{
			int rowIndexForArray = (int) point.getX();
			int columnIndexForArray = (int) point.getY();
			Disc disc = getDiscIfPresent(rowIndexForArray,columnIndexForArray);
			if (disc != null && disc.isPlayer1Move == isPlayer1Turn) {
               chain++;
               if(chain==4) {
               	return true;
               }
			}else {
				chain=0;
			}
		}
			return false;
	}private void gameOver(){
		String winner= isPlayer1Turn?playerOneTextField.getText():playerTwoTextField.getText();

		System.out.println("Winner is: " + winner);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect Four ");
		alert.setHeaderText("Winner is: " + winner);
		ButtonType yesBtn =new ButtonType("Yes");
		ButtonType noBtn=new ButtonType("No,Exit");
		alert.getButtonTypes().setAll(yesBtn,noBtn);
		Platform.runLater(()-> {
			Optional<ButtonType> btnClicked = alert.showAndWait();
			if (btnClicked.isPresent() && btnClicked.get()==yesBtn) {
				resetGame();
			} else {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	public void resetGame() {
		discPane.getChildren().clear();
		for (int row = 0; row < insertedDiscArray.length ; row++) {
			for (int col = 0; col<insertedDiscArray[row].length; col++) {
				insertedDiscArray[row][col]=null;
			}
		}
		isPlayer1Turn=true;
		playerNameLabel.setText(player1);
		createPlayground();
	}

	private Disc getDiscIfPresent(int row,int column)
{
		if(row>=ROW || row<0 || column>=COLUMN || column<0)
		return null;
		return insertedDiscArray[row][column];
}

	private static class Disc extends Circle{
		private  final boolean isPlayer1Move;
		public Disc(boolean isPlayer1Move){
			this.isPlayer1Move=isPlayer1Move;
			setRadius(diameter/2);
			setFill(isPlayer1Move? Color.valueOf(discColor1):Color.valueOf(discColor2));
			setCenterY(diameter/2);
			setCenterX(diameter/2);

		}
}
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
