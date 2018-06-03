package eclipse.gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Score {

    private String name = "";
    private IntegerProperty score;

    public Score() {
        score = new SimpleIntegerProperty(0);
    }

    public Score(int initialValue) {
        score = new SimpleIntegerProperty(initialValue);
    }

    public Score(String name, int value) {
        this.name = name;
        score = new SimpleIntegerProperty(value);
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(int score) {
        this.score.set(this.getScore() + score);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    @Override
    public String toString() {
        return (name == "" ? "Score" : name) + ": " + getScore();
    }
}
