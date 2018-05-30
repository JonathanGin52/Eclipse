package eclipse.gamecomponents;

import eclipse.gamecomponents.fire.*;
import eclipse.gamecomponents.path.*;
import eclipse.gui.Main;
import javafx.animation.PathTransition;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class SmallEnemy extends Enemy {

    private final static Image SPRITE = new Image(IMAGE_DIR + "plane.png");
    private ImageView img;
    private VectorPath vectorPath;
    private FirePattern firePattern;

    public SmallEnemy(int xPos, int yPos, VectorPath vectorPath, FirePattern firePattern) {
        super(xPos, yPos, 30, 30, 4);
        this.relocate(xPos, yPos);
        img = new ImageView(SPRITE);
        img.setFitHeight(super.getHeight());
        img.setFitWidth(super.getWidth());
        this.getChildren().add(img);
        this.vectorPath = vectorPath;
        this.firePattern = firePattern;
        fireRate = 500000000L;
    }

    @Override
    public PathTransition getVectorPath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(long now) {
        Vector vector = vectorPath.getVector(xPos, yPos, now - startTime);
        unboundedMove(vector);

        img.setRotate(90 - Math.atan2(-vector.dy, vector.dx) * 180 / Math.PI);
        this.relocate(xPos, yPos);

        // check if the enemy is out of bounds by a sufficient margin
        if (xPos < -50 || xPos > Main.getDimensions().getWidth() + 50) {
            remove();
        }
        if (yPos < -50 || yPos > Main.getDimensions().getHeight() + 50) {
            remove();
        }

        // check if this enemy fires new projectiles
        if (lastFire + fireRate < now) {
            setFire();
            List<VectorPath> newProjVectors = firePattern.getProjectilePaths(now);
            List<Projectile> newProj = new ArrayList(newProjVectors.size());
            for (VectorPath vectorPath : newProjVectors) {
                newProj.add(new Laser(xPos, yPos, vectorPath, true));
            }

            setNewProjectiles(newProj);

            lastFire = now;
        }
    }
}
