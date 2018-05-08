package eclipse.gamecomponents;

import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class Bomb extends GameObject {

    private final double BOMB_RADIUS = 20;
    // There has to be a better place for this vvv
    /*private final Image FRAME00 = new Image(IMAGE_DIR + "bomb/bomb0.png");
    private final Image FRAME01 = new Image(IMAGE_DIR + "bomb/bomb1.png");
    private final Image FRAME02 = new Image(IMAGE_DIR + "bomb/bomb2.png");
    private final Image FRAME03 = new Image(IMAGE_DIR + "bomb/bomb3.png");
    private final Image FRAME04 = new Image(IMAGE_DIR + "bomb/bomb4.png");
    private final Image FRAME05 = new Image(IMAGE_DIR + "bomb/bomb5.png");
    private final Image FRAME06 = new Image(IMAGE_DIR + "bomb/bomb6.png");
    private final Image FRAME07 = new Image(IMAGE_DIR + "bomb/bomb7.png");
    private final Image FRAME08 = new Image(IMAGE_DIR + "bomb/bomb8.png");
    private final Image FRAME09 = new Image(IMAGE_DIR + "bomb/bomb9.png");
    private final Image FRAME10 = new Image(IMAGE_DIR + "bomb/bomb10.png");
    private final Image FRAME11 = new Image(IMAGE_DIR + "bomb/bomb11.png");
    private final Image FRAME12 = new Image(IMAGE_DIR + "bomb/bomb12.png");
    private final Image FRAME13 = new Image(IMAGE_DIR + "bomb/bomb13.png");*/
    private final ImageView SPRITE = new ImageView(new Image(IMAGE_DIR + "bomb/bomb.png"));
    private final long FRAME_RATE = 125000000L; // Delay between frame in nanoseconds
    private int animationFrame = 0;
    private long lastUpdate = -1;
    private boolean destroy = false;
    private boolean explode = false;

    public Bomb(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        speed = 8;
        super.setDimension(new Dimension2D(50, 50));
        SPRITE.setFitHeight(super.getHeight());
        SPRITE.setFitWidth(super.getWidth());
        setSprite();
        this.getChildren().add(SPRITE);
    }

    public boolean isDestroyed() {
        return destroy;
    }

    @Override
    public void update(long now) {
        if (lastUpdate == -1) {
            lastUpdate = now;
        }

        if (!explode) {
            if (yPos - speed <= 0) { // Destroy bomb if it goes off screen
                destroy = true;
            }
            move(0, -1);
        }

        if (explode && now - lastUpdate >= FRAME_RATE){
            animationFrame++;
            setSprite();
            lastUpdate = now;
        }

        relocate(xPos, yPos);

        if (animationFrame > 13) {
            destroy = true;
        }
    }

    private void setSprite() {
        SPRITE.setImage(new Image(IMAGE_DIR + "bomb/bomb" + animationFrame + ".png"));
    }

    public void explode() {
        explode = true;
    }
}
