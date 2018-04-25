package eclipse.gamecomponents;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class GameObject extends Parent {

    public final static String IMAGE_DIR = "file:src/eclipse/images/";
    int xPos;
    int yPos;
    int xSpeed; // Distance (px) moved per key press
    int ySpeed; // Distance (px) moved per key press

    public abstract void update(Pane pane);

    public void moveLeft() {
        xPos -= xSpeed;
    }

    public void moveUp() {
        yPos -= ySpeed;
    }

    public void moveRight() {
        xPos += xSpeed;
    }

    public void moveDown() {
        yPos += ySpeed;
    }

}
