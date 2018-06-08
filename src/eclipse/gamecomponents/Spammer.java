package eclipse.gamecomponents;

import eclipse.gamecomponents.fire.FirePattern;
import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Spammer extends Enemy {

    private final static Image IMAGE = new Image(IMAGE_DIR + "enemy2.gif");

    public Spammer(int xPos, int yPos, VectorPath vectorPath, FirePattern firePattern, long startDelay) {
        super(IMAGE, 3, 300, xPos, yPos, 50, 50, vectorPath, firePattern, 0.5, 1, startDelay);
    }

    @Override
    public Projectile getProjectile(double xPos, double yPos, int speed, VectorPath vectorPath) {
        return new Dagger(xPos, yPos, speed, vectorPath, true);
    }
}
