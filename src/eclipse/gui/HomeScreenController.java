package eclipse.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;

import java.io.File;

public class HomeScreenController extends ParentController {

    final AudioClip START_CLIP = new AudioClip(new File("resources/audio/Start.wav").toURI().toString());
    @FXML
    private Button start;
    @FXML
    private Button settings;
    @FXML
    private Button scoreboard;

    @Override
    public void init() {
        application.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            KeyCode code = ke.getCode();
            if (code == KeyCode.SPACE) {
                startGame(true);
            }
        });
        start.setOnMouseClicked(event -> {
            START_CLIP.play();
            startGame(true);
        });
        settings.setOnMouseClicked(event -> settings(false));
        scoreboard.setOnMouseClicked(event -> scoreboard(false));
    }
}
