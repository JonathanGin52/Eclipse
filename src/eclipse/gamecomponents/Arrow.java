package eclipse.gamecomponents;

import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Arrow extends Projectile {

    private final static Image image = new Image(IMAGE_DIR + "arrow.png");

    public Arrow(double xPos, double yPos, int speed, VectorPath vectorPath, boolean enemyProj) {
        super(xPos - 8, yPos - 25, 8, 40, speed, image, vectorPath, enemyProj);
    }

    @Override
    public int getDamage() {
        return 1;
    }
}
