package eclipse.gamecomponents;

import javafx.animation.PathTransition;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class Enemy extends GameObject {

    private boolean startAnimation = true;
    private boolean isAlive = true;
    long startingAge = -1;

    public Enemy(double xPos, double yPos) {
        super(xPos, yPos, 50, 50, 6);
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

    public boolean isAlive() {
        return isAlive;
    }
}
