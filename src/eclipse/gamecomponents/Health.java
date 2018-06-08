package eclipse.gamecomponents;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Health {

    private IntegerProperty health;

    public Health() {
        health = new SimpleIntegerProperty(10);
    }

    public int getHealth() {
        return health.get();
    }

    public void gainHealth(int health) {
        this.health.set(this.getHealth() + health);
    }

    public void loseHealth(int health) {
        this.health.set(this.getHealth() - health);
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    @Override
    public String toString() {
        return health.toString();
    }
}
