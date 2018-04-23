package eclipse.gamecomponents;

import javafx.scene.shape.Path;

/**
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class Enemy extends GameObject {
    
    boolean isAlive = true;
    
    public abstract void move(Path path);
}
