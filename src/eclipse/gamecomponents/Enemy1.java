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

    public Enemy1() {
        Rectangle rect = new Rectangle(50, 50);
        super.setDimension(new Dimension2D(50, 50));
        this.getChildren().add(rect);
        try {
            Random rand = new Random();
            this.xPos = rand.nextInt((int) (Main.getDimensions().getWidth() - super.getWidth()));
            this.yPos = rand.nextInt((int) (Main.getDimensions().getHeight() - super.getHeight()));
        } catch (NullPointerException e) {
            this.xPos = 100;
            this.yPos = 100;
        }
        this.relocate(xPos, yPos);
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
