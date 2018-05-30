package eclipse.gamecomponents;

import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Laser extends Projectile {

    private final static Image image = new Image(IMAGE_DIR + "laser.png");

    public Laser(double xPos, double yPos, VectorPath vectorPath, boolean enemyProj) {
        super(xPos, yPos, image, vectorPath, enemyProj);
    }
}
