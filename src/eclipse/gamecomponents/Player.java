package eclipse.gamecomponents;

import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Player extends GameObject {

    private final Image SPRITE = new Image(IMAGE_DIR + "plane.png");
    private ImageView img;
    private int hitpoints;

    public Player() {
        xPos = 100;
        yPos = 300;
        hitpoints = 100;
        speed = 3;
        super.setDimension(new Dimension2D(50, 50));
        img = new ImageView(SPRITE);
        img.setFitHeight(super.getHeight());
        img.setFitWidth(super.getWidth());
        this.getChildren().add(img);
    }

    @Override
    public void update(long now) {
        this.relocate(xPos, yPos);
    }

    public void move(boolean[] directions) {
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

    public void mouseMove(double x, double y){
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
}
