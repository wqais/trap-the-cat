package executables;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sendable.PlayerInfo;
import sendable.RequestType;
import util.NetworkUtil;

import java.util.ArrayList;

public class JoinGameScene {
    private PlayerInfo selfInfo;
    private NetworkUtil server;
    public PlayerInfo[] adminsList;
    private ListView<String> listView;
    private ArrayList<Integer> mapping;
    private Pane pane;

    public JoinGameScene(PlayerInfo selfInfo, NetworkUtil server) {
        this.selfInfo = selfInfo;
        this.server = server;

        if (selfInfo.getPlayerType()==0) Main.window.setTitle("Trap The Cat - Trapper");
        else Main.window.setTitle("Trap The Cat - Cat");

        server.write(new RequestType(RequestType.JOIN_GAME));
        server.write(selfInfo);

        System.out.println(selfInfo.getName() + " joined");

        listView = new ListView<>();

        listView.setPrefWidth(400);
        listView.setPrefHeight(400);
        listView.setTranslateY(50);
        listView.setTranslateX(100);


        pane = new Pane();
        Scene scene = new Scene(pane, 600, 600);
        scene.getStylesheets().add(getClass().getResource("HighScoreScene.css").toExternalForm());
        updateAdminsList();

        pane.getChildren().add(listView);

        Label refreshLabel = new Label("Refresh");
        refreshLabel.setTranslateX(100);
        refreshLabel.setTranslateY(500);
        refreshLabel.setFont(Font.font("ubuntu", 24));

        refreshLabel.setOnMouseEntered(e -> refreshLabel.setTextFill(Color.DARKGREEN));
        refreshLabel.setOnMouseExited(e -> refreshLabel.setTextFill(Color.BLACK));
        refreshLabel.setOnMouseClicked(e -> {
            System.out.println("refreshing");
            updateAdminsList();
        });

        pane.getChildren().add(refreshLabel);

        Label requestLabel = new Label("Send Request");
        requestLabel.setTranslateX(400);
        requestLabel.setTranslateY(500);
        requestLabel.setFont(Font.font("ubuntu", 24));
        requestLabel.setOnMouseEntered(e -> requestLabel.setTextFill(Color.DARKGREEN));
        requestLabel.setOnMouseExited(e -> requestLabel.setTextFill(Color.BLACK));
        requestLabel.setOnMouseClicked(e -> {
            int idx = listView.getSelectionModel().getSelectedIndex();
            if (idx >= 0 && idx < mapping.size()) {
                idx = mapping.get(idx);

                server.write(new RequestType(RequestType.RECEIVE_PLAYER_INDEX));
                server.write(idx);

                RequestType requestType = (RequestType) server.read();

                if (requestType.getType()==RequestType.REQUEST_ACCEPTED) {
                    new GameScene(selfInfo, getAdminsList()[idx], server);
                }
            }
        });
        pane.getChildren().add(requestLabel);

        Main.window.setScene(scene);
    }

    public void updateAdminsList() {
        listView.getItems().clear();

        server.write(new RequestType(RequestType.SEND_ADMINS_LIST));
        adminsList = (PlayerInfo[]) server.read();

        mapping = new ArrayList<>();

        System.out.println("size = " + adminsList.length);
        for (int i = 0; i < adminsList.length; i++) {
            PlayerInfo playerInfo = adminsList[i];
            System.out.println("got " + playerInfo.getName() + " type = " + playerInfo.getPlayerType()
                                + " isAvailable = " + playerInfo.isAvailable());
            if (playerInfo.isAvailable() &&
                    (playerInfo.getPlayerType() ^ selfInfo.getPlayerType()) == 1) {
                listView.getItems().add(playerInfo.getName());
                mapping.add(i);
            }
        }
    }

    public PlayerInfo[] getAdminsList() {
        return adminsList;
    }
}
