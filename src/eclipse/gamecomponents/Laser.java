package eclipse.gamecomponents;

import javafx.scene.image.Image;

public class Laser extends Projectile {

    public Laser(double xPos, double yPos) {
        super(xPos, yPos, new Image(IMAGE_DIR + "laser.png"));
    }
}
