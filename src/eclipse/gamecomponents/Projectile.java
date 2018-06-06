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

    Projectile(double xPos, double yPos, Image image, VectorPath vectorPath, boolean enemyProj) {
        super(xPos, yPos, 25, 50, 10);
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

    public boolean isEnemyProj() {
        return enemyProj;
    }

    @Override
    public void update(long now) {
        age = now - startTime;

        Vector vector = getVector();
        unboundedMove(vector);
        SPRITE.setRotate(90 - Math.atan2(-vector.dy, vector.dx) * 180 / Math.PI);

        if (xPos < -50 || xPos > Main.getDimensions().getWidth() + 50 || yPos < -50 || yPos > Main.getDimensions().getHeight() + 50) setDestroyed();

        relocate(xPos, yPos);
    }
}
