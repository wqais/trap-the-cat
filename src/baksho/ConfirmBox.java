package baksho;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ConfirmBox {
    static boolean result;

    public static boolean display(String title, String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage boxWindow = new Stage();
                boxWindow.initModality(Modality.APPLICATION_MODAL);
                boxWindow.setTitle(title);
                Label label = new Label(message);
                label.setFont(Font.font("ubuntu", 16));
                VBox layout = new VBox(20);
                layout.setAlignment(Pos.CENTER);
                layout.setPadding(new Insets(20, 20, 20, 20));

                HBox hBox = new HBox(20);
                Button yesButton = new Button("Yes");
                Button noButton = new Button("No");
                yesButton.setFont(Font.font("ubuntu", 16));
                noButton.setFont(Font.font("ubuntu", 16));

                hBox.getChildren().addAll(yesButton, noButton);
                hBox.setAlignment(Pos.CENTER);

                result = false;

                yesButton.setOnAction(e -> {
                    result = true;
                    boxWindow.close();
                });

                noButton.setOnAction(e -> {
                    result = false;
                    boxWindow.close();
                });

                layout.getChildren().addAll(label, hBox);
                Scene scene = new Scene(layout);

                boxWindow.setScene(scene);
                boxWindow.showAndWait();
            }
        });


        return result;
    }
}
