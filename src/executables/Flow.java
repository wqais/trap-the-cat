package executables;

import game.*;
import game.player.Cat;
import game.player.Player;
import game.player.Trapper;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sendable.Cell;
import sendable.PlayerInfo;

public class Flow implements Runnable {
    private Trapper trapper;
    private Cat cat;
    private Turn turn;
    private static boolean finished;
    private String message;
    private Group root;
    private Thread thr;
    private Scene scene;
    private Label status;

    public Flow(Trapper trapper, Cat cat, Turn turn, Scene scene, Group root) {
        this.trapper = trapper;
        this.cat = cat;
        this.turn = turn;
        this.scene = scene;
        this.root = root;
        finished = false;

        status = new Label();
        status.setTranslateY(550);
        status.setPrefWidth(600);
        status.setAlignment(Pos.CENTER);
        status.setFont(Font.font("ubuntu", 18));
        root.getChildren().add(status);

        thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run() {
        while (true) {
            if (turn.getTurn()==0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        status.setText(trapper.getName() + "'s move");
                    }
                });
                trapper.makeMove();
                if (trapper.hasWon()) {
                    cat.writeToServer(new Cell(-1, -1));
                    break;
                }
            }
            else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        status.setText(cat.getName() + "'s move");
                    }
                });
                cat.makeMove();
                if (cat.hasWon()) {
                    trapper.writeToServer(new Cell(-1, -1));
                    break;
                }
            }
            turn.toggle();
        }

        finished = true;

        if (turn.getTurn()==0) {
            message = "Trapper has won!";
            System.out.println(message);
        }
        else {
            message = "Cat has won!";
            System.out.println(message);
        }

        showWinningStatus();
    }

    public static boolean isFinished() {
        return finished;
    }

    private void showWinningStatus() {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
              Rectangle rectangle = new Rectangle(0, 0, 600, 600);
              rectangle.setFill(Color.WHITE);
              rectangle.setOpacity(0.7);
              root.getChildren().add(rectangle);

              Label label = new Label(message);
              label.setFont(Font.font("ubuntu", FontWeight.EXTRA_BOLD, 48));
              label.setTranslateY(250);
              label.setPrefWidth(600);
              label.setAlignment(Pos.CENTER);
              root.getChildren().add(label);

              Label exitLabel = new Label("GO BACK");
              exitLabel.setFont(Font.font("ubuntu", 32));
              exitLabel.setPrefWidth(600);
              exitLabel.setAlignment(Pos.CENTER);
              exitLabel.setTranslateY(310);

              exitLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent event) {
                      exitLabel.setTextFill(Color.GREEN);
                      scene.setCursor(Cursor.HAND);
                  }
              });
              exitLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent event) {
                      exitLabel.setTextFill(Color.BLACK);
                      scene.setCursor(Cursor.DEFAULT);
                  }
              });
              exitLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent event) {
                      Main.mainRef.showStartScene();
                  }
              });

              root.getChildren().add(exitLabel);
          }
      });
    }
}
