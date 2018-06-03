package eclipse.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class HomeScreenController extends ParentController {

    @FXML
    private Button start;
    @FXML
    private Button settings;

    @Override
    public void init() {
        application.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            KeyCode code = ke.getCode();
            if (code == KeyCode.SPACE) {
                startGame();
            }
        });
        start.setOnMouseClicked(event -> startGame());
        settings.setOnMouseClicked(event -> settings());
    }
}
