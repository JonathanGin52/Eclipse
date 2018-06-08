package eclipse.gamecomponents;

import eclipse.gamecomponents.fire.FirePattern;
import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Spammer extends Enemy {

    private final static Image IMAGE = new Image(IMAGE_DIR + "enemy2.gif");

    public Spammer(int xPos, int yPos, VectorPath vectorPath, FirePattern firePattern, long startDelay) {
        super(IMAGE, 1, 100, xPos, yPos, 50, 50, vectorPath, firePattern, 1.5, 0.5, startDelay);
    }
}
