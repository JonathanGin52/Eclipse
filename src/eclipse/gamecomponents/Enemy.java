package eclipse.gamecomponents;

import javafx.animation.PathTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public abstract class Enemy extends GameObject {

    boolean isAlive = true;
    boolean startAnimation = true;
    BooleanProperty collision;

    final ChangeListener DETECT_COLLISION = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
        isAlive = false;
    };

    public Enemy() {
//	collision.set(false);
//	collision.addListener(DETECT_COLLISION);
    }

    @Override
    public void update(Pane pane) {
        pane.getChildren().remove(this);
        if (!isAlive) {
            return;
        } else {
            pane.getChildren().add(this);
        }
        if (startAnimation) {
            startAnimation = false;
        }
    }

    public abstract PathTransition getPath();

    public void checkCollision() {
    }

}
