package eclipse.gamecomponents;

import eclipse.gui.Main;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class GameObject extends Parent {

    final static String IMAGE_DIR = "file:src/eclipse/images/";
    double xPos;
    double yPos;
    int speed; // Base speed
    private double containerHeight = Main.getDimensions().getHeight();
    private double containerWidth = Main.getDimensions().getWidth();
    private Dimension2D dimension; // Dimensions of the gameObject

    public abstract void update(long now);

    public void move(double dx, double dy) {
        double newXPos = xPos + speed * dx;
        if (0 <= newXPos && (newXPos + getWidth() <= containerWidth)) {
            xPos = newXPos;
        }

        double newYPos = yPos + speed * dy;
        if (0 <= newYPos && (newYPos + getHeight() <= containerHeight)) {
            yPos = newYPos;
        }
    }

    public boolean checkIntersection(GameObject obj) {
        return this.getBoundsInParent().intersects(obj.getBoundsInParent());
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

    public double getMidpointX() {
        return xPos - this.getWidth() / 2d;
    }

    public double getMidpointY() {
        return yPos - this.getHeight() / 2d;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName(); // idk what other info should go here
    }
}
