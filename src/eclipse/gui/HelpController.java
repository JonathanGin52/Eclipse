package eclipse.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class HelpController extends ParentController {

    @FXML
    private Slider volumeSlider;
    @FXML
    private Button home;

    @Override
    public void init() {
        application.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            KeyCode code = ke.getCode();
            if (code == KeyCode.ESCAPE) {
                returnHome(false);
            }
        });
        home.setOnMouseClicked(event -> returnHome(false));
        volumeSlider.adjustValue(application.getVolume());
        volumeSlider.valueProperty().addListener(c -> application.setVolume(volumeSlider.getValue()));
    }
}
