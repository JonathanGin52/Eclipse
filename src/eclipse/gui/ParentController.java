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
    void startGame(boolean switchMusic) {
        application.gotoScene("Game", switchMusic);
    }

    // Switch active scene to home screen
    void returnHome(boolean switchMusic) {
        application.gotoScene("HomeScreen", switchMusic);
    }

    // Switch active scene to settings screen
    void settings(boolean switchMusic) {
        application.gotoScene("Settings", switchMusic);
    }

    // Switch active scene to scoreboard
    void scoreboard(boolean switchMusic) {
        application.gotoScene("Scoreboard", switchMusic);
    }

    public abstract void init();

    // Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
