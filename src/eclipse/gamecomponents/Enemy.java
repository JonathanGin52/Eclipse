package eclipse.gamecomponents;

import javafx.animation.PathTransition;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class Enemy extends GameObject {

    public boolean isAlive = true;
    boolean startAnimation = true;

    public Enemy() {
        super();
    }

    @Override
    public void update() {

    }

    public abstract PathTransition getPath();

    public void checkCollision() {
    }

}
