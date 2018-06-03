package eclipse.gamecomponents;

import javafx.animation.PathTransition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class Enemy extends GameObject {

    long fireRate;
    long lastFire = Long.MIN_VALUE;
    private boolean isAlive = true;
    private boolean fire = false;
    private List<Projectile> newProjectiles = new ArrayList<>();

    public Enemy(double xPos, double yPos, int width, int height, int speed) {
        super(xPos, yPos, width, height, speed);

        fire = true;
    }

    @Override
    public abstract void update(long now);

    public abstract PathTransition getVectorPath();

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
        if (fire) fire = false;
        return newProjectiles;
    }

    public void setNewProjectiles(List<Projectile> list) {
        newProjectiles = new ArrayList(list);
    }

    public boolean isAlive() {
        return isAlive;
    }
}
