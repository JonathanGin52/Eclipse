package eclipse.gamecomponents;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private final int FRAME_RATE = 125000000; // Delay between frame in nanoseconds
    private int animationFrame = 0;
    private long lastUpdate = -1;
    private boolean destroy = false;

    public Bomb(double xPos, double yPos) {
        System.out.println(xPos + " " + yPos);
        this.xPos = xPos;
        this.yPos = yPos;
        this.getChildren().add(SPRITE);
    }

    public boolean isDestroyed() {
        return destroy;
    }

    @Override
    public void update(long now) {
        if (animationFrame > 13) {
            destroy = true;
        } else {
            relocate(xPos, yPos);
            if (lastUpdate == -1) {
                lastUpdate = now;
            } else if (now - lastUpdate >= FRAME_RATE) {
                setSprite();
                animationFrame++;
                lastUpdate = now;
            }
        }
    }

    private void setSprite() {
        SPRITE.setImage(new Image(IMAGE_DIR + "bomb/bomb" + animationFrame + ".png"));
    }
}
