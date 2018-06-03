package eclipse.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SettingsController extends ParentController {

    @FXML
    private Button home;

    @Override
    public void init() {
        application.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            KeyCode code = ke.getCode();
            if (code == KeyCode.ESCAPE) {
                returnHome();
            }
        });
        home.setOnMouseClicked(event -> returnHome());
    }
}
