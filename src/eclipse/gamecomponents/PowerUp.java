package eclipse.gamecomponents;

import eclipse.gamecomponents.path.Vector;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class PowerUp extends GameObject {

    private static Player player;
    private ImageView img;

    public PowerUp(Image image, double xPos, double yPos, int width, int height) {
        super(xPos, yPos, width, height, 1.5);

        this.relocate(xPos, yPos);
        img = new ImageView(image);
        img.setFitHeight(super.getHeight());
        img.setFitWidth(super.getWidth());
        this.getChildren().add(img);
    }

    public static void setPlayer(Player player) {
        PowerUp.player = player;
    }

    @Override
    public void update(long now) {
        unboundedMove(new Vector(player.xPos - xPos, player.yPos - yPos));
        this.relocate(xPos, yPos);
    }
}
