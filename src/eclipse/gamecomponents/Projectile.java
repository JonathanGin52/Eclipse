package eclipse.gamecomponents;

import eclipse.gamecomponents.path.Vector;
import eclipse.gamecomponents.path.VectorPath;
import eclipse.gui.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Projectile extends GameObject {

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

    public abstract int getDamage();

    public Vector getVector() {
        return vectorPath.getVector(xPos, yPos, age);
    }

    public void setVector(VectorPath vectorPath) {
        this.vectorPath = vectorPath;
    }

    public boolean isEnemyProj() {
        return enemyProj;
    }

    // Move the projectile
    @Override
    public void update(long now) {
        age = now - startTime;

        Vector vector = getVector();
        unboundedMove(vector);

        SPRITE.setRotate(90 - Math.atan2(-vector.dy, vector.dx) * 180 / Math.PI);

        if (xPos < -getWidth() || xPos > Main.getDimensions().getWidth() || yPos < -getHeight() - 500 || yPos > Main.getDimensions().getHeight()) {
            setDestroyed();
        }

        relocate(xPos, yPos);
    }
}
