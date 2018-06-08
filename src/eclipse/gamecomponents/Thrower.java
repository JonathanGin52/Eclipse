// This Enemy throws targeted projectiles at the player

package eclipse.gamecomponents;

import eclipse.gamecomponents.fire.FireAtPlayer;
import eclipse.gamecomponents.fire.FirePattern;
import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

import java.util.Random;

public class Thrower extends Enemy {

    private final static Image IMAGE = new Image(IMAGE_DIR + "enemy1.gif");
    private final static Random random = new Random();

    public Thrower(int xPos, int yPos, VectorPath vectorPath, FirePattern firePattern, long startDelay) {
        super(IMAGE, 1, 100, xPos, yPos, 50, 50, vectorPath, firePattern, 2, 1, startDelay);
        setFirePattern(new FireAtPlayer(((FireAtPlayer) firePattern).getPlayer(), this, 20));
    }

    @Override
    public Projectile getProjectile(double xPos, double yPos, int speed, VectorPath vectorPath) {
        return new Spear(xPos, yPos, speed, vectorPath, true);
    }

    public boolean dropItem() {
        return random.nextDouble() < 0.2;
    }
}
