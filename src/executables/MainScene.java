package executables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sendable.HighScore;
import sendable.PlayerInfo;
import sendable.RequestType;
import util.NetworkUtil;

import java.io.IOException;

public class MainScene {

    Stage stage;
    @FXML
    private Pane pane;


    public void mouseClicked(MouseEvent e) {

        stage = (Stage) ((Label) e.getSource()).getScene().getWindow();
        String text = ((Label) e.getSource()).getText();
        if(text.equals("Multiplayer")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("MultiplayerScene.fxml"));
                stage.setScene(new Scene(root, 600, 600));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (text.equals("Single Player")) {
            new GameScene();
        } else if (text.equals("High Scores")) {
            System.out.println("clicked high scores");
            showHighScoreScene();
        }
        else if(text.equals("Exit")) gotoExit();
        else if(text.equals("About")) gotoAboutScene();
        //if(text.equals("About")) gotoAbout(stage);
        //else if(text.equals("Exit")) gotoExit();

    }

    public void mouseEntered(MouseEvent e) {
        Scene scene = ((Label) e.getSource()).getScene();
        scene.setCursor(Cursor.HAND);
        Label label = (Label) e.getSource();
        label.setTextFill(Color.DARKGREEN);
    }

    public void mouseExited(MouseEvent e) {
        Scene scene = ((Label) e.getSource()).getScene();
        scene.setCursor(Cursor.DEFAULT);
        Label label = (Label) e.getSource();
        label.setTextFill(Color.BLACK);
    }

    public void gotoExit() {
        System.exit(0);
    }

    public void gotoAboutScene() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("AboutScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 600, 600);
        scene.getStylesheets().add(getClass().getResource("AboutScene.css").toExternalForm());
        stage.setScene(scene);
    }

    public void showHighScoreScene() {
        Group group = new Group();
        Scene scene = new Scene(group, 600,600);
        scene.getStylesheets().add(getClass().getResource("HighScoreScene.css").toExternalForm());
        Main.window.setScene(scene);

        Label serverAdress = new Label("Use server address");
        serverAdress.setTranslateX(30);
        serverAdress.setTranslateY(10);
        serverAdress.setStyle(" -fx-font-size: 18; -fx-font-family: ubuntu ");
        group.getChildren().add(serverAdress);

        TextField textField = new TextField("127.0.0.1");
        textField.setTranslateX(30);
        textField.setTranslateY(40);
        textField.setStyle(" -fx-font-size: 18; -fx-font-family: ubuntu ");
        group.getChildren().add(textField);

        Label showScore = new Label("Refresh");
        showScore.setTranslateX(330);
        showScore.setTranslateY(40);
        showScore.setStyle(" -fx-font-size: 18; -fx-font-family: ubuntu ");

        showScore.setOnMouseEntered(e -> {
            showScore.setTextFill(Color.DARKGREEN);
            scene.setCursor(Cursor.HAND);
        });
        showScore.setOnMouseExited(e -> {
            showScore.setTextFill(Color.BLACK);
            scene.setCursor(Cursor.DEFAULT);
        });
        showScore.setOnMouseClicked(e -> {
            //showHighScoreScene();
            loadHighScore(group, textField.getText());
        });

        group.getChildren().add(showScore);



        Label label = new Label("Go Back");

        label.setOnMouseEntered(e -> {
            label.setTextFill(Color.DARKGREEN);
            scene.setCursor(Cursor.HAND);
        });
        label.setOnMouseExited(e -> {
            label.setTextFill(Color.BLACK);
            scene.setCursor(Cursor.DEFAULT);
        });
        label.setOnMouseClicked(e -> Main.mainRef.showStartScene());

        label.setFont(Font.font("ubuntu", 24));
        label.setPrefWidth(600);
        label.setAlignment(Pos.CENTER);
        label.setTranslateY(530);
        group.getChildren().add(label);

    }

    void loadHighScore(Group group, String address) {
        NetworkUtil nc;
        try {
            nc= new NetworkUtil(address, 44444);
        } catch (Exception exception ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading server: " + address);
            alert.setContentText("Please try again later");
            alert.showAndWait();
            return;
        }

        nc.write(new RequestType(RequestType.SHOW_HIGH_SCORE));

        HighScore[] trapperHighScores = (HighScore[]) nc.read();

        TableColumn trapperCol = new TableColumn("Trappers");
        trapperCol.setPrefWidth(150);
        trapperCol.setCellValueFactory(
                new PropertyValueFactory<HighScore, Integer>("name"));

        TableColumn scoreCol = new TableColumn("Moves");
        scoreCol.setPrefWidth(100);
        scoreCol.setCellValueFactory(
                new PropertyValueFactory<HighScore, Integer>("moves"));

        ObservableList<HighScore> observableTrappers = FXCollections.observableArrayList();
        for (HighScore score : trapperHighScores) {
            System.out.println("name = " + score.getName() + " moves = " + score.getMoves());
            observableTrappers.add(score);
        }

        TableView<HighScore> trapperScoreTable = new TableView<>();
        trapperScoreTable.setItems(observableTrappers);
        trapperScoreTable.getColumns().addAll(trapperCol, scoreCol);
        trapperScoreTable.setEditable(false);
        trapperScoreTable.setTranslateX(30);
        trapperScoreTable.setTranslateY(100);

        ObservableList<HighScore> observableCats = FXCollections.observableArrayList();
        HighScore[] catsHighScores = (HighScore[]) nc.read();
        for (HighScore score : catsHighScores) {
            System.out.println("name = " + score.getName() + " moves = " + score.getMoves());
            observableCats.add(score);
        }

        TableView<HighScore> catScoreTable = new TableView<>();
        catScoreTable.setItems(observableCats);

        TableColumn catsCol = new TableColumn("Cats");
        catsCol.setPrefWidth(150);
        catsCol.setCellValueFactory(
                new PropertyValueFactory<HighScore, Integer>("name"));

        TableColumn scoreCol2 = new TableColumn("Moves");
        scoreCol2.setPrefWidth(100);
        scoreCol2.setCellValueFactory(
                new PropertyValueFactory<HighScore, Integer>("moves"));

        catScoreTable.getColumns().addAll(catsCol, scoreCol2);
        //catScoreTable.setEditable(false);
        catScoreTable.setTranslateX(320);
        catScoreTable.setTranslateY(100);

        group.getChildren().addAll(trapperScoreTable, catScoreTable);
    }
}
