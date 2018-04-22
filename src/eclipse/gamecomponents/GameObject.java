package eclipse.gamecomponents;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class GameObject {

    protected int x;
    protected int y;
    private final int MOVE_DIST = 3; // Distance (px) moved per key press

    public abstract void update(GraphicsContext gc);

    public void moveUp() {
	y -= MOVE_DIST;
    }

    public void moveDown() {
	y += MOVE_DIST;
    }
    
    public void moveLeft() {
	x -= MOVE_DIST;
    }
    
    public void moveRight() {
	x += MOVE_DIST;
    }
}
