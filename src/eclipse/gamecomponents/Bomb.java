package eclipse.gamecomponents;

import eclipse.gamecomponents.path.*;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bomb extends Projectile {

    private static Image[] images;
    private static final long FRAME_RATE = 40000000L; // Delay between frame in nanoseconds
    private static final int EXPLODE_DIAMETER = 300;
    private int animationFrame = 0;
    private long lastUpdate = System.nanoTime();
    private boolean explode = false;

    static {
        images = new Image[15];
        for (int i = 0; i < 15; i++) {
            images[i] = new Image(IMAGE_DIR + "bomb/bomb" + i + ".png");
        }
    }

    public Bomb(double xPos, double yPos) {
        super(xPos - 35, yPos - 35, 50, 50, 10, images[0], new Up(), false);
    }

    @Override
    public void update(long now) {
        if (animationFrame > 13) return;

        if (!explode) {
            if (yPos - speed <= 0) { // Destroy bomb if it goes off screen
                setDestroyed();
            }
            unboundedMove(getVector());
        }

        if (explode && now - lastUpdate >= FRAME_RATE) {
            animationFrame++;
            setSprite();
            lastUpdate = now;
        }
        relocate(xPos, yPos);
        if (animationFrame > 13) {
            setDestroyed();
        }
    }

    private void setSprite() {
        SPRITE.setImage(images[animationFrame]);
    }

    public void explode() {
        if (explode == true) return;

        explode = true;
        xPos += (getWidth() - EXPLODE_DIAMETER) / 2;
        yPos += -EXPLODE_DIAMETER / 2;
        dimensions = new Dimension2D(EXPLODE_DIAMETER, EXPLODE_DIAMETER);
        SPRITE.setFitHeight(dimensions.getHeight());
        SPRITE.setFitWidth(dimensions.getWidth());
        animationFrame++;
        setSprite();
        animationFrame--;
    }
}
