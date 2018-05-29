package eclipse.gamecomponents;

import eclipse.gamecomponents.path.*;
import eclipse.gui.Main;
import javafx.animation.PathTransition;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Enemy1 extends Enemy {

    private final Image SPRITE = new Image(IMAGE_DIR + "plane.png");
    private ImageView img;
    private VectorPath vectorPath;

    public Enemy1(int xPos, int yPos) {
        super(xPos, yPos);
        dimensions = new Dimension2D(50, 50);
        this.relocate(xPos, yPos);
        img = new ImageView(SPRITE);
        img.setFitHeight(super.getHeight());
        img.setFitWidth(super.getWidth());
        this.getChildren().add(img);

        // RANDOM PATH
        Random random = new Random();
        switch (random.nextInt(6)) {
            case 0:
                vectorPath = new Down();
                break;
            case 1:
                vectorPath = new Left();
                break;
            case 2:
                vectorPath = new Right();
                break;
            case 3:
                vectorPath = new SineDown();
                break;
            case 4:
                vectorPath = new UShapeLeft();
                break;
            case 5:
                vectorPath = new UShapeRight();
                break;
        }
    }

    @Override
    public PathTransition getVectorPath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(long now) {
        if (startingAge == -1) startingAge = now;

        Vector vector = vectorPath.getVector(xPos, yPos, now - startingAge);
        unboundedMove(vector);

        img.setRotate(90 - Math.atan2(-vector.dy, vector.dx) * 180 / Math.PI);

        // check if the enemy is out of bounds by a sufficient margin
        if (xPos < -50 || xPos > Main.getDimensions().getWidth() + 50) {
            remove();
        }
        if (yPos < -50 || yPos > Main.getDimensions().getHeight() + 50) {
            remove();
        }
        this.relocate(xPos, yPos);
    }
}
