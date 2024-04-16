package game;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class CatAnimation extends Transition {
    private ImageView imageView;
    private final int lastIndex = 25;
    private final int animationDuration = 900;

    private double dx;
    private double dy;

    private double x;
    private double y;

    public CatAnimation(ImageView imageView, double dx, double dy) {
        this.imageView = imageView;
        this.dx = dx;
        this.dy = dy;

        x = imageView.getX();
        y = imageView.getY();

        setInterpolator(Interpolator.LINEAR);
        setCycleDuration(Duration.millis(animationDuration));
    }

    @Override
    protected void interpolate(double fraction) {
        int index = (int) Math.floor(fraction*lastIndex);
        //System.out.println(index);

        imageView.setX(x + fraction * dx);
        imageView.setY(y + fraction * dy);
        imageView.setImage(new Image(getClass().getResourceAsStream("frames/" + index + ".png")));
    }
}
