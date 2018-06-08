package eclipse.gamecomponents;

import javafx.scene.image.Image;

public class BoomerangPowerUp extends PowerUp {

    private static final Image image = new Image(IMAGE_DIR  + "boomerang_upgrade.png");

    public BoomerangPowerUp(double xPos, double yPos) {
        super(image, xPos, yPos);
    }
}
