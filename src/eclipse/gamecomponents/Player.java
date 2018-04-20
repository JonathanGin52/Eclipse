package eclipse.gamecomponents;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Player extends GameObject {

    Circle circle;
    int x;
    int y;

    public Player() {
	circle = new Circle(50);
	x = 0;
	y = 0;
	circle.setFill(Color.ALICEBLUE);
    }
}
