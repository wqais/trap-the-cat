package game;

import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import sendable.Cell;

public class Grid {

    static final Paint BLOCKED = Color.rgb(84, 99, 17);
    static final Paint OPEN = Color.rgb(218, 247, 12);
    static final Paint BILAIASE = Color.RED;

    private final int offsetX = 35;
    private final int offsetY = 35;
    private ImageView catImage;
    private Scene scene;
    private Britto[][] brittos;
    private Cell catPosition;
    private Group root;
    private Turn turn;

    private int player;

    public Grid(Scene scene, Group root, Turn turn, int player) {

        this.turn = turn;
        this.player = player;
        this.scene = scene;
        this.root = root;
        catImage = new ImageView(new Image(getClass().getResourceAsStream("frames/0.png")));
        catImage.setPreserveRatio(true);

        brittos = new Britto[11][11];

        catPosition = new Cell(5, 5);

        for(int i = 0; i < 11; i++) {
            for(int j = 0; j < 11; j++) {
                Circle circle = new Circle();
                brittos[i][j] = new Britto(this.scene, circle, i, j, false, turn, player, catPosition);
                this.root.getChildren().add(brittos[i][j].getCircle());
            }
        }

        catImage.setX(brittos[5][5].getCircle().getCenterX()-offsetX);
        catImage.setY(brittos[5][5].getCircle().getCenterY()-offsetY);

        this.root.getChildren().add(catImage);
    }

    public void block(int x, int y) {
        brittos[x][y].setStatus(true);
        brittos[x][y].getCircle().setFill(BLOCKED);
    }

    public void moveTo(int x, int y) {

        int oldX = catPosition.getX();
        int oldY = catPosition.getY();

        //brittos[oldX][oldY].getCircle().setFill(OPEN);
        brittos[oldX][oldY].setStatus(false);
        catPosition.setX(x);
        catPosition.setY(y);
        brittos[x][y].setStatus(true);
        //brittos[x][y].getCircle().setFill(BILAIASE);

        double dx = brittos[x][y].getCircle().getCenterX()-brittos[oldX][oldY].getCircle().getCenterX();
        double dy = brittos[x][y].getCircle().getCenterY()-brittos[oldX][oldY].getCircle().getCenterY();

        if (dy > 0) {
            if (dx > 0) {
                catImage.setScaleX(-1.0);
                catImage.setRotate(45);
            }
            else {
                catImage.setScaleX(1.0);
                catImage.setRotate(-45);
            }
        } else if (dy < 0) {
            if (dx > 0) {
                catImage.setScaleX(-1.0);
                catImage.setRotate(-45);
            }
            else {
                catImage.setScaleX(1.0);
                catImage.setRotate(45);
            }
        }
        else {
            if (dx < 0) {
                catImage.setScaleX(1.0);
                catImage.setRotate(0);
            }
            else {
                catImage.setScaleX(-1.0);
                catImage.setRotate(0);
            }
        }

        CatAnimation animation = new CatAnimation(catImage, dx, dy);

        animation.play();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Cell getCatPosition() {
        return catPosition;
    }

    public boolean getStausOfBritto(int i, int j) {
        return brittos[i][j].isStatus();
    }

    public void moveToWithoutGraphics(int x, int y) {
        int oldX = catPosition.getX();
        int oldY = catPosition.getY();
        //brittos[oldX][oldY].getCircle().setFill(OPEN);
        brittos[oldX][oldY].setStatus(false);
        catPosition.setX(x);
        catPosition.setY(y);
        brittos[x][y].setStatus(true);
    }

    public void blockWithoutGraphics(int x, int y) {
        brittos[x][y].setStatus(true);
    }

    public void unblockWithoutGraphics(int x, int y) {
        brittos[x][y].setStatus(false);
    }
}
