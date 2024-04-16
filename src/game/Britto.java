package game;

import executables.Flow;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import sendable.Cell;

public class Britto {

    private static Cell clickedCell;
    private static Object lock = new Object();

    public static void setClickedCell(Cell cell) {
        synchronized (lock) {
            clickedCell = cell;
        }
    }

    public static Cell getClickedCell() {
        synchronized (lock) {
            return clickedCell;
        }
    }

    public static int dxEven[] = {0, -1, -1, 0, 1, 1};
    public static int dyEven[] = {1, 0, -1, -1, -1, 0};

    public static int dxOdd[] = {0, -1, -1, 0, 1, 1};
    public static int dyOdd[] = {1, 1, 0, -1, 0, 1};

    private int player;
    public int getPlayer() { return player; }

    private Scene scene;

    private Circle circle;
    public Circle getCircle() {
        return circle;
    }

    private int row;
    private int column;
    public int getRow() { return row; }
    public int getColumn() { return column; }

    private boolean status = false;
    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean isStatus() {
        return status;
    }

    private Turn turn;
    private Cell catPosition;

    public boolean isValidCell() {
        if (Flow.isFinished()) return false;
        if (turn.getTurn()!=getPlayer()) return false;
        if (isStatus()==true) return false;

        if (getPlayer()==0) {
            return !status;
        }
        else {
            if (catPosition.getX()%2==0) {
                for (int i = 0; i < 6; i++) {
                    if (catPosition.getX()+dxEven[i]==row &&
                            catPosition.getY()+dyEven[i]==column) return true;
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    if (catPosition.getX()+dxOdd[i]==row &&
                            catPosition.getY()+dyOdd[i]==column) return true;
                }
            }

            return false;
        }
    }


    public Britto(Scene scene,
                  Circle circle,
                  int row,
                  int column,
                  boolean status,
                  Turn turn,
                  int player,
                  Cell catPosition) {

        this.catPosition = catPosition;
        this.player = player;
        this.scene = scene;
        this.circle = circle;
        this.row = row;
        this.column = column;
        this.status = status;
        this.turn = turn;

        circle.setCenterX(100+40*column+20*(row%2));
        circle.setCenterY(100+40*row);
        circle.setRadius(19);
        circle.setFill(Grid.OPEN);

        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (isValidCell())
                    setClickedCell(new Cell(getRow(), getColumn()));
            }
        });

        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isValidCell())
                    scene.setCursor(Cursor.HAND);
            }
        });

        circle.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scene.setCursor(Cursor.DEFAULT);
            }
        });
    }

}
