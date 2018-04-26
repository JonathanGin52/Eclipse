package eclipse.gamecomponents;

import javafx.animation.PathTransition;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Enemy1 extends Enemy {

    Rectangle rect;
    Random rand = new Random();

    public Enemy1() {
//	super();
        rect = new Rectangle(50, 50);
        this.getChildren().add(rect);
//        if (Main.getBounds() != null) {
//            this.xPos = rand.nextInt((int) Main.getBounds().getWidth());
//            this.yPos = rand.nextInt((int) Main.getBounds().getHeight());
//        } else {
//            this.xPos = 100;
//            this.yPos = 100;
//        }
//        this.relocate(xPos, yPos);
    }

    public Enemy1(int x, int y) {
        this();
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public PathTransition getPath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
