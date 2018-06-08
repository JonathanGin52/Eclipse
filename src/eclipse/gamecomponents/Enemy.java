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

public abstract class Enemy extends GameObject {

    private long fireRate;
    private int hitPoints;
    private int killScore;
    private long lastFire;
    private ImageView img;
    private VectorPath vectorPath;
    FirePattern firePattern;
    private boolean isAlive = true;
    private boolean fire = false;
    private List<Projectile> newProjectiles = new ArrayList<>();

    public Enemy(Image image, int hitpoints, int killScore, double xPos, double yPos, int width, int height, VectorPath vectorPath, FirePattern firePattern, double speed, double fireRate, long startDelay) {
        super(xPos, yPos, width, height, speed);

        this.hitPoints = hitpoints;
        this.killScore = killScore;

        this.fireRate = (long) (1000000000d / fireRate); // fireRate is a per second amount
        this.lastFire = System.nanoTime() - this.fireRate + startDelay;
        this.fire = true;
        this.relocate(xPos, yPos);

        img = new ImageView(image);
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
        if (xPos < -getWidth() || xPos > Main.getDimensions().getWidth() || yPos < -getHeight() || yPos > Main.getDimensions().getHeight()) {
            remove();
        }

        // check if this enemy fires new projectiles
        if (lastFire + fireRate < now) {
            setFire();
            List<VectorPath> newProjVectors = firePattern.getProjectilePaths(now);
            List<Projectile> newProj = new ArrayList(newProjVectors.size());
            for (VectorPath vectorPath : newProjVectors) {
                newProj.add(getProjectile(xPos + getWidth() / 2, yPos + 1.5 * getHeight(), 5, vectorPath));
            }

            setNewProjectiles(newProj);

            lastFire = now;
        }

        img.setVisible(true);
    }

    public abstract Projectile getProjectile(double xPos, double yPos, int speed, VectorPath vectorPath);

    public abstract boolean dropItem();

    // Return score associated with killing this enemy
    public void hit(int damage) {
        hitPoints -= damage;

        if (hitPoints <= 0) {
            isAlive = false;
        }
    }

    public int getKillScore() {
        return killScore;
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

    public void setFirePattern(FirePattern firePattern) {
        this.firePattern = firePattern;
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
