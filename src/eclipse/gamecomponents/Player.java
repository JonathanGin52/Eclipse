package eclipse.gamecomponents;

import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends GameObject {

    private final Image SPRITE = new Image(IMAGE_DIR + "link.gif");
    private final long INSIDE_TICK_RATE = 1000000000;
    public int arrowLevel = 1;
    public int boomerangLevel = 1;
    public int bombInv = 3;
    public int boomerangOut = 0;
    public boolean insideEnemy = false;
    private ImageView img;
    private Health health;
    private long lastTick = Long.MIN_VALUE;

    public Player() {
        super(100, 300, 40, 60, 8);
        health = new Health();
        img = new ImageView(SPRITE);
        img.setFitHeight(super.getHeight());
        img.setFitWidth(super.getWidth());
        this.getChildren().add(img);
    }

    public IntegerProperty getHealthProperty() {
        return health.healthProperty();
    }

    // Called when the player is inside an enemy
    // When inside an enemy, damage is taken periodically
    // Returns true if damage is taken, false if damage is not taken
    public boolean isInsideEnemy() {
        insideEnemy = true;

        long time = System.nanoTime();
        if (time > lastTick + INSIDE_TICK_RATE) {
            loseHealth(1);
            lastTick = time;
            return true;
        }

        return false;
    }

    public int getHealth() {
        return health.getHealth();
    }

    public void gainHealth(int health) {
        this.health.gainHealth(health);
    }

    public void loseHealth(int health) {
        this.health.loseHealth(health);
    }

    public long getArrowDelay() {
        if (arrowLevel < 2) return 400000000L; // level 1
        else if (arrowLevel < 4) return 300000000L; // levels 2-3
        else return 200000000L;
    }

    @Override
    public void update(long now) {
        this.relocate(xPos, yPos);
    }

    public void keyMove(boolean[] directions) {
        double d = 1 / Math.sqrt(2); // dx and dy when moving diagonally
        if (directions[0] && directions[1]) { // Left and Up
            move(-d, -d);
        } else if (directions[1] && directions[2]) { // Up and Right
            move(d, -d);
        } else if (directions[2] && directions[3]) { // Right and Down
            move(d, d);
        } else if (directions[0] && directions[3]) { // Left and Down
            move(-d, d);
        } else { // Movement in a single direction
            if (directions[0]) { // Left
                move(-1, 0);
            }
            if (directions[1]) { // Up
                move(0, -1);
            }
            if (directions[2]) { // Right
                move(1, 0);
            }
            if (directions[3]) { // Down
                move(0, 1);
            }
        }
    }

    public void mouseMove(double x, double y) {
        // Player moves directly towards cursor
        // Extra work is done in this method to avoid jittery motion
        x = x - getWidth() / 2;
        y = y - getHeight() / 2;

        double slope = (yPos - y) / (xPos - x);
        double dx = Math.sqrt(1 / (1 + slope * slope));
        if (xPos > x) {
            dx *= -1;
        }

        if (Math.abs(x - xPos) >= speed) {
            move(dx, 0);
        }
        if (Math.abs(y - yPos) >= speed) {
            move(0, dx * slope);
        }
    }

    @Override
    public String toString() {
        return "[" + this.xPos + ", " + this.yPos + "] Health: " + this.health;
    }
}
