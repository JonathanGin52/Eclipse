package eclipse.gamecomponents;

import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Laser extends Projectile {

    private final static Image image = new Image(IMAGE_DIR + "ball.png");

    public Laser(double xPos, double yPos, VectorPath vectorPath, boolean enemyProj) {
        super(xPos - 8, yPos - 25, 11, 40, 9, image, vectorPath, enemyProj);
    }

    @Override
    public int getDamage() {
        return 1;
    }
}
