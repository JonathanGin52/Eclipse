package eclipse.gamecomponents;

import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Dagger extends Projectile {

    private final static Image image = new Image(IMAGE_DIR + "dagger.png");

    public Dagger(double xPos, double yPos, VectorPath vectorPath, boolean enemyProj) {
        super(xPos - 8, yPos - 25, 20, 40, 4, image, vectorPath, enemyProj);
    }

    @Override
    public int getDamage() {
        return 1;
    }
}
