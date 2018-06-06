package eclipse.gamecomponents;

import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Arrow extends Projectile {

    private final static Image image = new Image(IMAGE_DIR + "arrow.png");

    public Arrow(double xPos, double yPos, int speed, VectorPath vectorPath, boolean enemyProj) {
        super(xPos, yPos, 10, 50, speed, image, vectorPath, enemyProj);
    }
}
