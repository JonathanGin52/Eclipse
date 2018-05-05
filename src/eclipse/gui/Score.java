package eclipse.gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Score {

    private IntegerProperty score;

    public Score() {
        score = new SimpleIntegerProperty(0);
    }

    public Score(int initialValue) {
        score = new SimpleIntegerProperty(initialValue);
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public void add(int score) {
        this.score.set(this.getScore() + score);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    @Override
    public String toString() {
        return "Score: " + getScore();
    }
}
