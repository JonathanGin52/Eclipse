package eclipse.gamecomponents;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class GameObject {

    public final static String IMAGE_DIR = "file:src/eclipse/images/";
    int x;
    int y;
    int x_speed; // Distance (px) moved per key press
    int y_speed; // Distance (px) moved per key press

    public abstract void update(GraphicsContext gc);

    public void moveUp() {
	y -= y_speed;
    }

    public void moveDown() {
	y += y_speed;
    }

    public void moveLeft() {
	x -= x_speed;
    }

    public void moveRight() {
	x += x_speed;
    }
}
