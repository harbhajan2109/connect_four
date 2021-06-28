package com.bagdadi.connectFour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
  public Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader= new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane gridPane=loader.load();
        controller=loader.getController();
        controller.createPlayground();
        Pane menuPane = (Pane) gridPane.getChildren().get(0);
        MenuBar menuBar=createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        menuPane.getChildren().add(menuBar);
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

private MenuBar createMenu(){
    Menu file = new Menu("File");
    MenuItem newGame = new MenuItem("New Game");
    newGame.setOnAction(event -> controller.resetGame());
    MenuItem resetGame=new MenuItem("Reset Game");
    resetGame.setOnAction(event ->controller.resetGame());
    SeparatorMenuItem separatorMenuItem= new SeparatorMenuItem();
    MenuItem exit = new MenuItem("Exit");
    exit.setOnAction(event -> exitGame());
    file.getItems().addAll(newGame,resetGame,separatorMenuItem,exit);
    Menu helpMenu = new Menu("Help");
    MenuItem aboutGame = new MenuItem("About Game ");
    aboutGame.setOnAction(event -> aboutConnect4Game());
    SeparatorMenuItem separator= new SeparatorMenuItem();
    MenuItem aboutMe = new MenuItem("About Me");
    aboutMe.setOnAction(event -> aboutMe());
    helpMenu.getItems().addAll(aboutGame,separator,aboutMe);
    MenuBar menuBar=new MenuBar();
    menuBar.getMenus().addAll(file,helpMenu);
    return menuBar;
}

    private void aboutMe() {
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Developer");
        alert.setHeaderText("Harbhajan Singh");
        alert.setContentText(" I am A pro");
        alert.show();
    }

    private void aboutConnect4Game() {
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the game");
        alert.setHeaderText("How to play ?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. " +
                "The first player can always win by playing the right moves.");
        alert.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        //todo
    }

    public static void main(String[] args) {

        launch(args);
    }
}
