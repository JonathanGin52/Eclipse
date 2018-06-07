package eclipse.gamecomponents;

import eclipse.gamecomponents.fire.FirePattern;
import eclipse.gamecomponents.fire.FireAtPlayer;
import eclipse.gamecomponents.path.Vector;
import eclipse.gamecomponents.path.VectorPath;
import eclipse.gui.Main;
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

    public SmallEnemy(int xPos, int yPos, VectorPath vectorPath, FirePattern firePattern, long startDelay) {
        super(xPos, yPos, 40, 40, 2, 2000000000L, startDelay);
        this.relocate(xPos, yPos);
        img = new ImageView(SPRITE);
        img.setFitHeight(super.getHeight());
        img.setFitWidth(super.getWidth());
        img.setVisible(false);
        this.getChildren().add(img);
        this.vectorPath = vectorPath;

        this.firePattern = firePattern;

        if (firePattern instanceof FireAtPlayer) {
            this.firePattern = new FireAtPlayer(((FireAtPlayer) firePattern).getPlayer(), this, ((FireAtPlayer) firePattern).getMargin());
        }
    }

    @Override
    public void update(long now) {
        Vector vector = vectorPath.getVector(xPos, yPos, now - startTime);
        unboundedMove(vector);

        img.setRotate(90 - Math.atan2(-vector.dy, vector.dx) * 180 / Math.PI);
        this.relocate(xPos, yPos);

        // check if the enemy is out of bounds by a sufficient margin
        if (xPos < -getWidth() || xPos > Main.getDimensions().getWidth()) {
            remove();
        }
        if (yPos < -getHeight() || yPos > Main.getDimensions().getHeight()) {
            remove();
        }

        // check if this enemy fires new projectiles
        if (lastFire + fireRate < now) {
            setFire();
            List<VectorPath> newProjVectors = firePattern.getProjectilePaths(now);
            List<Projectile> newProj = new ArrayList(newProjVectors.size());
            for (VectorPath vectorPath : newProjVectors) {
                newProj.add(new Arrow(xPos + getWidth() / 2, yPos + 1.5 * getHeight(), 5, vectorPath, true));
            }

            setNewProjectiles(newProj);

            lastFire = now;
        }

        img.setVisible(true);
    }
}
