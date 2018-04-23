package eclipse.gamecomponents;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class GameObject {

    public final static String IMAGE_DIR = "file:src/eclipse/images/";
    int xPos;
    int yPos;
    int xSpeed; // Distance (px) moved per key press
    int ySpeed; // Distance (px) moved per key press

    public abstract void update(GraphicsContext gc);

    public void moveUp() {
	yPos -= ySpeed;
    }

    public void moveDown() {
	yPos += ySpeed;
    }

    public void moveLeft() {
	xPos -= xSpeed;
    }

    public void moveRight() {
	xPos += xSpeed;
    }
}
