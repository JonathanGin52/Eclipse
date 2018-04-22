package eclipse.gamecomponents;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Player extends GameObject {

    private final Image SPRITE = new Image(IMAGE_DIR + "plane.png");

    public Player() {
	x = 100;
	y = 300;
	x_speed = 3;
	y_speed = 3;
    }

    @Override
    public void update(GraphicsContext gc) {
	// Clear rectangle the size of image +- distance of movement
	gc.clearRect(x - x_speed, y - y_speed, 50 + x_speed * 2, 50 + y_speed * 2);
	gc.drawImage(SPRITE, x, y, 50, 50);
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
