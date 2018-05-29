package eclipse.gamecomponents;

import eclipse.gui.Main;
import javafx.animation.PathTransition;
import javafx.geometry.Dimension2D;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Enemy1 extends Enemy {


    public Enemy1(int xPos, int yPos) {
        super(xPos, yPos);
        Rectangle rect = new Rectangle(50, 50);
        dimensions = new Dimension2D(50, 50);
        this.getChildren().add(rect);
        this.relocate(xPos, yPos);
    }

    @Override
    public PathTransition getPath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
