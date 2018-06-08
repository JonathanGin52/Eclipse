package eclipse.gamecomponents;

import javafx.scene.image.Image;

public class ArrowPowerUp extends PowerUp {

    private static final Image image = new Image(IMAGE_DIR  + "arrow_upgrade.png");

    public ArrowPowerUp(double xPos, double yPos) {
        super(image, xPos, yPos);
    }
}
