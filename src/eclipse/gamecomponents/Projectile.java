package eclipse.gamecomponents;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Projectile extends GameObject {

    private final Image img = new Image(IMAGE_DIR);
    private final ImageView SPRITE = new ImageView(img);

    public Projectile(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.getChildren().add(SPRITE);
        this.relocate(xPos, yPos);
    }

    @Override
    public void update(long now) {

    }
}
