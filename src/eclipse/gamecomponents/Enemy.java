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
public class Enemy extends GameObject {

    private final static Image YELLOW = new Image(IMAGE_DIR + "enemy1.gif");
    private final static Image GREY = new Image(IMAGE_DIR + "enemy2.gif");

    long fireRate;
    long lastFire;
    private ImageView img;
    private VectorPath vectorPath;
    private FirePattern firePattern;
    private boolean isAlive = true;
    private boolean fire = false;
    private List<Projectile> newProjectiles = new ArrayList<>();

    public Enemy(String name, int xPos, int yPos, VectorPath vectorPath, FirePattern firePattern, long startDelay) {
        super((double) xPos, (double) yPos, 50, 50, 1.5);
        this.fireRate = 2000000000L;
        this.lastFire = System.nanoTime() - 2000000000L + startDelay;
        this.fire = true;
        this.relocate(xPos, yPos);

        if (name.equals("yellow")) {
            img = new ImageView(YELLOW);
        } else if (name.equals("grey")) {
            img = new ImageView(GREY);
        }

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

    // Return score associated with killing this enemy
    public int kill() {
        isAlive = false;
        return 100;
    }

    // Enemy is dead because it hasn't been killed while on screen
    public void remove() {
        isAlive = false;
    }

    // returns whether Enemy is firing a projectile at this frame
    public boolean fire() {
        return fire;
    }

    public void setFire() {
        fire = true;
    }

    public List<Projectile> getNewProjectiles() {
        fire = false;
        return newProjectiles;
    }

    public void setNewProjectiles(List<Projectile> list) {
        newProjectiles = new ArrayList(list);
    }

    public boolean isAlive() {
        return isAlive;
    }
}
