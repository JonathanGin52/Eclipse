package eclipse.gamecomponents;

import eclipse.gamecomponents.path.*;
import eclipse.gui.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Projectile extends GameObject {

    private Image img;
    final ImageView SPRITE = new ImageView(img);
    private boolean destroy = false;
    private VectorPath vectorPath;
    private boolean enemyProj;

    Projectile(double xPos, double yPos, int width, int height, int speed, Image image, VectorPath vectorPath, boolean enemyProj) {
        super(xPos, yPos, width, height, speed);
        this.img = image;
        SPRITE.setImage(img);
        SPRITE.setFitHeight(dimensions.getHeight());
        SPRITE.setFitWidth(dimensions.getWidth());
        this.vectorPath = vectorPath;
        this.enemyProj = enemyProj;
        this.getChildren().add(SPRITE);
        this.relocate(xPos, yPos);
    }

    public boolean isDestroyed() {
        return destroy;
    }

    public void setDestroyed() {
        destroy = true;
    }

    public Vector getVector() {
        return vectorPath.getVector(xPos, yPos, age);
    }

    public void setVector(VectorPath vectorPath) {
        this.vectorPath = vectorPath;
    }

    public boolean isEnemyProj() {
        return enemyProj;
    }

    @Override
    public void update(long now) {
        age = now - startTime;

        Vector vector = getVector();
        unboundedMove(vector);

//        SPRITE.setRotate(90 - Math.atan2(-vector.dy, vector.dx) * 180 / Math.PI);

        if (xPos < -500 || xPos > Main.getDimensions().getWidth() + 500 || yPos < -500 || yPos > Main.getDimensions().getHeight() + 500) setDestroyed();

        relocate(xPos, yPos);
    }
}
