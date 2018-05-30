package eclipse.gamecomponents;

import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Laser extends Projectile {

    public Laser(double xPos, double yPos, VectorPath vectorPath, boolean enemyProj) {
        super(xPos, yPos, new Image(IMAGE_DIR + "laser.png"), vectorPath, enemyProj);
    }
}
