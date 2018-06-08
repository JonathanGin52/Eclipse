package eclipse.gui;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameOverController extends ParentController {

    @FXML
    private VBox box;
    @FXML
    private Button start;
    @FXML
    private Button home;

    @Override
    public void init() {
        FadeTransition ft = new FadeTransition(Duration.millis(10500), box);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        // Add button actions
        start.setOnMouseClicked(event -> startGame(true));
        home.setOnMouseClicked(event -> returnHome(true));

    }
}
