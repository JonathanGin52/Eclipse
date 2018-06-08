package eclipse.gamecomponents;

import eclipse.gamecomponents.fire.FireAtPlayer;
import eclipse.gamecomponents.fire.FirePattern;
import eclipse.gamecomponents.path.VectorPath;
import javafx.scene.image.Image;

public class Turret extends Enemy {

    private final static Image IMAGE = new Image(IMAGE_DIR + "eyeball.gif");

    public Turret(int xPos, int yPos, VectorPath vectorPath, FirePattern firePattern, long startDelay) {
        super(IMAGE, 50, 5000, xPos, yPos, 60, 60, vectorPath, firePattern, 0.1, 2.5, startDelay);
        setFirePattern(new FireAtPlayer(((FireAtPlayer) firePattern).getPlayer(), this, 10));
    }
}