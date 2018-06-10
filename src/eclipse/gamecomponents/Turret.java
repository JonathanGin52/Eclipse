package eclipse.gamecomponents;

import eclipse.gamecomponents.fire.FireAtPlayer;
import eclipse.gamecomponents.fire.FirePattern;
import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Turret extends Enemy {

    private final static Image IMAGE = new Image(IMAGE_DIR + "eyeball.gif");

    public Turret(int xPos, int yPos, VectorPath vectorPath, FirePattern firePattern, long startDelay) {
        super(IMAGE, 30, 5000, xPos, yPos, 60, 60, vectorPath, firePattern, 0.07, 1.5, startDelay);
        setFirePattern(new FireAtPlayer(((FireAtPlayer) firePattern).getPlayer(), this, 10));
    }

    @Override
    public Projectile getProjectile(double xPos, double yPos, VectorPath vectorPath) {
        return new Laser(xPos, yPos, vectorPath, true);
    }

    public boolean dropItem() {
        return true;
    }
}
