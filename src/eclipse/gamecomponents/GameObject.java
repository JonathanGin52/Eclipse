package eclipse.gamecomponents;

import eclipse.gui.Main;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class GameObject extends Parent {

    public final static String IMAGE_DIR = "file:src/eclipse/images/";
    int xPos;
    int yPos;
    int xSpeed; // Distance (px) moved per key press
    int ySpeed; // Distance (px) moved per key press
    private double containerWidth;
    private double containerHeight;
    private Dimension2D dimension;

    public GameObject() {
        Dimension2D containerDimensions = Main.getDimensions();
        this.containerHeight = containerDimensions.getHeight();
        this.containerWidth = containerDimensions.getWidth();
    }

    public abstract void update();

    public void moveLeft() {
        if (xPos - xSpeed < 0) {
            xPos = 0;
        } else {
            xPos -= xSpeed;
        }
    }

    public void moveUp() {
        if (yPos - ySpeed < 0) {
            yPos = 0;
        } else {
            yPos -= ySpeed;
        }
    }

    public void moveRight() {
        if (xPos + xSpeed + getWidth() > containerWidth) {
            xPos = (int) (containerWidth - getWidth());
        } else {
            xPos += xSpeed;
        }
    }

    public void moveDown() {
        if (yPos + ySpeed + getHeight() > containerHeight) {
            yPos = (int) (containerHeight - getHeight());
        } else {
            yPos += ySpeed;
        }
    }

    public Dimension2D getDimension() {
        return dimension;
    }

    public void setDimension(Dimension2D dimension) {
        this.dimension = dimension;
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getHeight() {
        return dimension.getHeight();
    }
}
