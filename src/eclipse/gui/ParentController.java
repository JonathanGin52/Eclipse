package eclipse.gui;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ParentController implements Initializable {

    Main application;

    void setApp(Main application) {
        this.application = application;
    }

    // Switches active scene from start screen to Game
    void startGame() {
        application.gotoScene("Game");
    }

    void returnHome() {
        application.gotoScene("HomeScreen");
    }

    // Switch active scene from start screen to settings screen
    void settings() {
        application.gotoScene("Settings");
    }

    public abstract void init();

    // Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
