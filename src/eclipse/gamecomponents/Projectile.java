package eclipse.gamecomponents;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Projectile extends GameObject {

    private Image img;
    final ImageView SPRITE = new ImageView(img);
    private boolean destroy = false;

    Projectile(double xPos, double yPos, Image image) {
        super(xPos, yPos, 50, 50, 8);
        this.img = image;
        SPRITE.setImage(img);
        SPRITE.setFitHeight(dimensions.getHeight());
        SPRITE.setFitWidth(dimensions.getWidth());
        this.getChildren().add(SPRITE);
        this.relocate(xPos, yPos);
    }

    public boolean isDestroyed() {
        return destroy;
    }

    public void setDestroyed() {
        destroy = true;
    }

    @Override
    public void update(long now) {
        if (yPos - speed <= 0) { // Destroy projectile if it goes off screen
            destroy = true;
        }
        move(0, -1);

        relocate(xPos, yPos);
    }
}
