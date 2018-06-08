package eclipse.gamecomponents;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class PowerUp extends GameObject {

    private ImageView img;

    public PowerUp(Image image, double xPos, double yPos, int width, int height) {
        super(xPos, yPos, width, height, 0);

        this.relocate(xPos, yPos);
        img = new ImageView(image);
        img.setFitHeight(super.getHeight());
        img.setFitWidth(super.getWidth());
        this.getChildren().add(img);
    }

    @Override
    public void update(long now) {}
}
