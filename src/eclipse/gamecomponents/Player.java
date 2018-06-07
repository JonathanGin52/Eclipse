package eclipse.gamecomponents;

import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends GameObject {

    private final Image SPRITE = new Image(IMAGE_DIR + "link.gif");
    public int bombInv = 3;
    public boolean boomerangOut = false;
    private ImageView img;
    private Health health;

    public Player() {
        super(100, 300, 50, 50, 8);
        health = new Health();
        img = new ImageView(SPRITE);
        img.setFitHeight(super.getHeight());
        img.setFitWidth(super.getWidth());
        this.getChildren().add(img);
    }

    public IntegerProperty getHealthProperty() {
        return health.healthProperty();
    }

    public int getHealth() {
        return health.getHealth();
    }

    public void loseHealth(int health) {
        this.health.loseHealth(health);
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
