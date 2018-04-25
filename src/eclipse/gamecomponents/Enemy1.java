package eclipse.gamecomponents;

import javafx.animation.PathTransition;
import javafx.scene.shape.Rectangle;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Enemy1 extends Enemy {

    Rectangle rect;

    public Enemy1() {
//	super();
        rect = new Rectangle(50, 50);
        this.getChildren().add(rect);
        this.xPos = 200;
        this.yPos = 200;
        this.relocate(xPos, yPos);
    }

    @Override
    public PathTransition getPath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
