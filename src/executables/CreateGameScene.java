package executables;

import baksho.ConfirmBox;
import game.player.Player;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sendable.PlayerInfo;
import sendable.RequestType;
import util.NetworkUtil;

import java.util.Optional;
import java.util.Scanner;

public class CreateGameScene {
    private PlayerInfo selfInfo;
    private NetworkUtil server;
    private PlayerInfo othersInfo;
    private boolean answer = false;

    public CreateGameScene(PlayerInfo selfInfo, NetworkUtil server) {
        this.selfInfo = selfInfo;
        this.server = server;

        if (selfInfo.getPlayerType()==0) Main.window.setTitle("Trap The Cat - Trapper");
        else Main.window.setTitle("Trap The Cat - Cat");

        server.write(new RequestType(RequestType.CREATE_GAME));
        server.write(selfInfo);

        System.out.println(selfInfo.getName() + " created");

        Pane pane = new Pane();

        Rectangle rectangle = new Rectangle(150, 200, 300, 200);
        rectangle.setFill(Color.BLACK);

        Label label = new Label("WAITING");
        //label.setFont();
        label.setTranslateY(290);
        label.setPrefWidth(600);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("ubuntu", FontWeight.BOLD, 24));
        label.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(rectangle, label);

        Scene scene = new Scene(pane, 600, 600);

        Main.window.setScene(scene);

        System.out.println(selfInfo.getName());
        System.out.println(selfInfo.getPlayerType());

        Task<PlayerInfo> task = new Task<PlayerInfo>() {
            @Override
            protected PlayerInfo call() throws Exception {
                while (true) {
                    othersInfo = (PlayerInfo) server.read();
                    System.out.println(othersInfo.getName()+" sent request");

                    Platform.runLater(() -> {
                        // Update your GUI here.;
                        synchronized (this) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText(othersInfo.getName() + " wants to play with you.");
                            alert.setContentText("Do you want to play?");

                            ButtonType acceptType = new ButtonType("Accept");
                            ButtonType rejectType = new ButtonType("Reject");

                            alert.getButtonTypes().setAll(acceptType, rejectType);

                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.get() == acceptType){
                                answer = true;
                            } else {
                                answer = false;
                            }
                        }

                    });

                    Thread.sleep(500);
                    synchronized (this) {
                        if (answer){
                            System.out.println("request accepted");
                            server.write(new RequestType(RequestType.REQUEST_ACCEPTED));
                            return othersInfo;
                        } else {
                            System.out.println("request denied");
                            server.write(new RequestType(RequestType.REQUEST_DENIED));
                        }
                    }

                }
            }
        };

        task.setOnSucceeded(e -> {
            PlayerInfo other = task.getValue();
            // update UI with result
            System.out.println("playing with " + othersInfo.getName());

            new GameScene(selfInfo, other, server);
        });

        new Thread(task).start();

    }
}
