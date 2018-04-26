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

    public boolean isAlive = true;
    final ChangeListener DETECT_COLLISION = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
        isAlive = false;
    };
    boolean startAnimation = true;
    BooleanProperty collision;

    public Enemy() {
//	collision.set(false);
//	collision.addListener(DETECT_COLLISION);
    }

    @Override
    public void update() {
    }

    public abstract PathTransition getPath();

    public void checkCollision() {
    }

}
