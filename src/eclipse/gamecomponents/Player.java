package eclipse.gamecomponents;

import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Player extends GameObject {

    private final Image SPRITE = new Image(IMAGE_DIR + "plane.png");
    private ImageView img;
    private int hitpoints;

    public Player() {
        xPos = 100;
        yPos = 300;
        hitpoints = 100;
        xSpeed = 3;
        ySpeed = 3;
        super.setDimension(new Dimension2D(50, 50));
        img = new ImageView(SPRITE);
        img.setFitHeight(super.getHeight());
        img.setFitWidth(super.getWidth());
        this.getChildren().add(img);
    }

    @Override
    public void update(long now) {
        this.relocate(xPos, yPos);
    }

    public void move(boolean[] directions) {
        if (directions[0]) {
            moveLeft();
        }
        if (directions[1]) {
            moveUp();
        }
        if (directions[2]) {
            moveRight();
        }
        if (directions[3]) {
            moveDown();
        }
    }
}
