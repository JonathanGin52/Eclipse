package eclipse.gamecomponents;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bomb extends Projectile {

    private final double BOMB_RADIUS = 20;
    private final long FRAME_RATE = 125000000L; // Delay between frame in nanoseconds
    private int animationFrame = 0;
    private long lastUpdate = -1;
    private boolean explode = false;

    public Bomb(double xPos, double yPos) {
        super(xPos, yPos, new Image(IMAGE_DIR + "bomb/bomb.png"));
    }

    @Override
    public void update(long now) {
        if (lastUpdate == -1) {
            lastUpdate = now;
        }

        if (!explode) {
            if (yPos - speed <= 0) { // Destroy bomb if it goes off screen
                setDestroyed();
            }
            move(0, -1);
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
        SPRITE.setImage(new Image(IMAGE_DIR + "bomb/bomb" + animationFrame + ".png"));
    }

    public void explode() {
        explode = true;
    }
}
