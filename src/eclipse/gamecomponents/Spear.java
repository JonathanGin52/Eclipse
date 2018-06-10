package eclipse.gamecomponents;

import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Spear extends Projectile {

    private final static Image image = new Image(IMAGE_DIR + "spear.png");

    public Spear(double xPos, double yPos, VectorPath vectorPath, boolean enemyProj) {
        super(xPos - 8, yPos - 25, 16, 39, 8, image, vectorPath, enemyProj);
    }

    @Override
    public int getDamage() {
        return 1;
    }
}
