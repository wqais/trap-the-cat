package executables;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import sendable.PlayerInfo;
import sendable.RequestType;
import util.NetworkUtil;

import java.util.Optional;

public class WaitingThread implements Runnable {
    private static boolean answer;
    private PlayerInfo selfInfo;
    private NetworkUtil server;

    public WaitingThread(PlayerInfo selfInfo, NetworkUtil server) {
        this.selfInfo = selfInfo;
        this.server = server;
    }

    @Override
    public void run() {
        PlayerInfo othersInfo;

        while (true) {
            othersInfo = (PlayerInfo) server.read();
            answer = false;

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Look, a Confirmation Dialog");
                    alert.setContentText("Are you ok with this?");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK){
                        answer = true;
                    } else {
                        answer = false;
                    }
                }
            });

            if (answer){
                System.out.println("request accepted");
                server.write(new RequestType(RequestType.REQUEST_ACCEPTED));
                break;
            } else {
                System.out.println("request denied");
                server.write(new RequestType(RequestType.REQUEST_DENIED));
            }
        }

        System.out.println("playing with " + othersInfo.getName());

        new GameScene(selfInfo, othersInfo, server);
    }
}
