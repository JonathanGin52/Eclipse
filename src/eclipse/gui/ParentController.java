package eclipse.gui;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ParentController implements Initializable {

    Main application;

    void setApp(Main application) {
        this.application = application;
    }

    // Switch active scene to game screen
    void startGame() {
        application.gotoScene("Game");
    }

    // Switch active scene to home screen
    void returnHome() {
        application.gotoScene("HomeScreen");
    }

    // Switch active scene to settings screen
    void settings() {
        application.gotoScene("Settings");
    }

    // Switch active scene to scoreboard
    void scoreboard() {
        application.gotoScene("Scoreboard");
    }

    public abstract void init();

    // Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
