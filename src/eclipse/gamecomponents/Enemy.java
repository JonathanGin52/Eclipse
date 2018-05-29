package eclipse.gamecomponents;

import javafx.animation.PathTransition;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class Enemy extends GameObject {

    private boolean startAnimation = true;
    private boolean isAlive = true;

    public Enemy(double xPos, double yPos) {
        super(xPos, yPos, 50, 50, 8);
    }

    @Override
    public void update(long now) {

    }

    public abstract PathTransition getPath();

    // Return score associated with killing this enemy
    public int kill() {
        isAlive = false;
        return 100;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
