package executables;

import game.*;
import game.player.Cat;
import game.player.Trapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sendable.Cell;
import server.Game;
import util.NetworkUtil;

import java.util.Scanner;


public class Main extends Application {
    public static Stage window;
    public static Main mainRef;
    private int option;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainRef = this;
        window = primaryStage;
        window.setResizable(false);

        showStartScene();
        window.show();
    }

    public void showStartScene() {
        try {
            window.setTitle("Trap The Cat");
            Image image = new Image(getClass().getResourceAsStream("icon1.png"));
            window.getIcons().add(image);
            Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            Scene scene = new Scene(root, 600, 600);
            scene.getStylesheets().add(getClass().getResource("MainScene.css").toExternalForm());
            window.setScene(scene);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}