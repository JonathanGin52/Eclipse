package eclipse.gamecomponents;

import eclipse.gamecomponents.path.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bomb extends Projectile {

    private static Image[] images;
    private final double BOMB_RADIUS = 20;
    private final long FRAME_RATE = 125000000L; // Delay between frame in nanoseconds
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
        super(xPos, yPos, 50, 50, 5, images[0], new Up(), false);
    }

    @Override
    public void update(long now) {
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
        explode = true;
    }
}
