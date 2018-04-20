package eclipse.gui;

import eclipse.gamecomponents.Player;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class GameController implements Initializable {

    private Main application;

    @FXML
    private AnchorPane root;

    public void setApp(Main application) {
	this.application = application;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	Player player = new Player();
	player.setHeight(50);
	player.setWidth(50);
	root.getChildren().add(player);

	Path path = new Path();
	path.getElements().add(new MoveTo(20, 20));
	path.getElements().add(new CubicCurveTo(30, 10, 380, 120, 200, 120));
	path.getElements().add(new CubicCurveTo(200, 120, 110, 240, 380, 240));
	path.setOpacity(0.5);
	path.setLayoutX(100);
	path.setLayoutY(100);
	root.getChildren().add(path);

	PathTransition pathTransition = new PathTransition();

	pathTransition.setDuration(Duration.seconds(8.0));
	pathTransition.setDelay(Duration.seconds(.5));
	pathTransition.setPath(path);
	pathTransition.setNode(player);
	pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	pathTransition.setCycleCount(Timeline.INDEFINITE);
	pathTransition.setAutoReverse(true);
	pathTransition.play();
    }

}
