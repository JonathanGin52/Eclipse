package eclipse.gamecomponents;

import javafx.scene.image.Image;

public class HeartAdd extends PowerUp {

    private static final Image image = new Image(IMAGE_DIR + "health_upgrade.gif");

    public HeartAdd(double xPos, double yPos) {
        super(image, xPos, yPos, 20, 20);
    }
}
