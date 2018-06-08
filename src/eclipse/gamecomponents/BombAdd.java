package eclipse.gamecomponents;

import javafx.scene.image.Image;

public class BombAdd extends PowerUp {

    private static final Image image = new Image(IMAGE_DIR  + "bomb_upgrade.gif");

    public BombAdd(double xPos, double yPos) {
        super(image, xPos, yPos, 50, 50);
    }
}
