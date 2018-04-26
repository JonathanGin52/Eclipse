package eclipse.gamecomponents;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Player extends GameObject {

    private final Image SPRITE = new Image(IMAGE_DIR + "plane.png");
    ImageView img;
    private int lives;

    public Player() {
        xPos = 100;
        yPos = 300;
        lives = 3;
        xSpeed = 3;
        ySpeed = 3;
        img = new ImageView(SPRITE);
        img.setFitWidth(50);
        img.setFitHeight(50);
        this.getChildren().add(img);

    }

    @Override
    public void update() {
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
