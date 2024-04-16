package executables;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sendable.PlayerInfo;
import util.NetworkUtil;

import java.io.IOException;

public class MultiplayerScene {

    private final String defaultAddress = "127.0.0.1";

    @FXML
    private TextField name;
    @FXML
    private TextField server;
    @FXML
    private RadioButton trapper;
    @FXML
    private RadioButton cat;

    private Stage stage;

    public void mouseClicked(MouseEvent e) {

        stage = (Stage) ((Label) e.getSource()).getScene().getWindow();
        String text = ((Label) e.getSource()).getText();
        if(text.equals("Go Back")) {
            goBack();
        }
        else if (text.equals("Create Game")) {

            if (name.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Name not entered");
                alert.setContentText("Please enter your name");
                alert.showAndWait();
            } else {
                NetworkUtil nc;
                try {
                    nc = new NetworkUtil(server.getText(), 44444);
                    new CreateGameScene(new PlayerInfo(name.getText(),
                            cat.isSelected() ? 1 : 0, true), nc);
                } catch (Exception exception) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error loading server: " + server.getText());
                    alert.setContentText("Please try again later");
                    alert.showAndWait();
                }
            }
        } else if (text.equals("Join Game")) {
            if (name.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Name not entered");
                alert.setContentText("Please enter your name");
                alert.showAndWait();
            } else {
                NetworkUtil nc;
                try {
                    nc = new NetworkUtil(server.getText(), 44444);
                    new JoinGameScene(new PlayerInfo(name.getText(), cat.isSelected() ? 1 : 0, true),
                            nc);
                } catch (Exception exception) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error loading server: " + server.getText());
                    alert.setContentText("Please try again later");
                    alert.showAndWait();
                }
            }
        }
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

    public void enableServerAddress(ActionEvent e) {
        CheckBox checkBox = (CheckBox) e.getSource();
        if(checkBox.isSelected()) server.setEditable(true);
        else {
            server.setText(defaultAddress);
            server.setEditable(false);
        }
    }

    public void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            Scene scene = new Scene(root, 600, 600);
            scene.getStylesheets().add(getClass().getResource("MainScene.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
