package eclipse.gui;

import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ScoreboardController extends ParentController {

    @FXML
    private TableView table;
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn scoreCol;
    @FXML
    private Button home;

    @Override
    public void init() {
        final ObservableList<Score> scores = FXCollections.observableArrayList(application.getScores());
        nameCol.setCellValueFactory(new PropertyValueFactory<Score, String>("name"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<Score, IntegerProperty>("score"));
        table.setItems(scores);

        application.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            KeyCode code = ke.getCode();
            if (code == KeyCode.ESCAPE) {
                returnHome();
            }
        });
        home.setOnMouseClicked(event -> returnHome());
    }
}
