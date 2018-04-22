package eclipse.gamecomponents;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Player extends GameObject {

    private final static Image SPRITE = new Image("file:plane.png");
    
    public Player() {
	x = 100;
	y = 300;
    }

    @Override
    public void update(GraphicsContext gc) {
	gc.clearRect(0, 0, 800, 600);
	gc.drawImage(SPRITE, x, y, 50, 50);
    }

}
