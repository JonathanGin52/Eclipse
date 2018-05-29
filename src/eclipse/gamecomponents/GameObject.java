package eclipse.gamecomponents;

import eclipse.gamecomponents.path.Vector;
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
    double containerHeight = Main.getDimensions().getHeight();
    double containerWidth = Main.getDimensions().getWidth();
    Dimension2D dimensions; // Dimensions of the gameObject

    GameObject(double xPos, double yPos, double width, double height, int speed) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.dimensions = new Dimension2D(width, height);
        this.speed = speed;
    }

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

    public void unboundedMove(double dx, double dy) {
        xPos += speed * dx;
        yPos += speed * dy;
    }

    public void unboundedMove(Vector vector) {
        // find direction vector
        double mag = Math.sqrt(vector.dx * vector.dx + vector.dy * vector.dy);
        Vector direction = new Vector(vector.dx / mag, vector.dy / mag);
        unboundedMove(direction.dx, direction.dy);
    }

    public boolean checkIntersection(GameObject obj) {
        return this.getBoundsInParent().intersects(obj.getBoundsInParent());
    }

    public double getWidth() {
        return dimensions.getWidth();
    }

    public double getHeight() {
        return dimensions.getHeight();
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
