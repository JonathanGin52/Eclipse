package eclipse.gamecomponents;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Player extends GameObject {

    private final Image SPRITE = new Image(IMAGE_DIR + "plane.png");
    private int lives;

    public Player() {
	xPos = 100;
	yPos = 300;
	lives = 3;
	xSpeed = 3;
	ySpeed = 3;
    }

    @Override
    public void update(GraphicsContext gc) {
	// Clear rectangle the size of image +- distance of movement
	gc.clearRect(xPos - xSpeed, yPos - ySpeed, 50 + xSpeed * 2, 50 + ySpeed * 2);
	gc.drawImage(SPRITE, xPos, yPos, 50, 50);
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
