package eclipse.gamecomponents;

import javafx.animation.PathTransition;

/**
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class Enemy extends GameObject {
    
    boolean isAlive = true;
    
    public abstract void move(PathTransition path);
}
