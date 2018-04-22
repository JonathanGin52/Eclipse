package eclipse.gui;

import eclipse.gamecomponents.Enemy;
import eclipse.gamecomponents.Player;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class GameController implements Initializable {

    private Main application;
    private Timeline gameLoop;
    private GraphicsContext gc;

    private Player player;
    private List<Enemy> enemies;

    @FXML
    private AnchorPane root;
    @FXML
    private Pane pane;
    @FXML
    private Canvas gameArea;

	boolean[] inputs = new boolean[4];

    public void setApp(Main application) {
	this.application = application;
    }

    private void installKeyListener(Scene scene) {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
			KeyCode code = ke.getCode();
			System.out.println("Key pressed: " + code);

			switch(code) {
				case UP:
					inputs[0] = true;
					break;
				case DOWN:
					inputs[1] = true;
					break;
				case LEFT:
					inputs[2] = true;
					break;
				case RIGHT:
					inputs[3] = true;
					break;
			}
		});

		scene.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent ke) -> {
			KeyCode code = ke.getCode();
			System.out.println("Key released: " + code);

			switch(code) {
				case UP:
					inputs[0] = false;
					break;
				case DOWN:
					inputs[1] = false;
					break;
				case LEFT:
					inputs[2] = false;
					break;
				case RIGHT:
					inputs[3] = false;
					break;
			}
		});
    }

    void initGame(int FRAME_RATE) {
	installKeyListener(application.getScene());
	final Duration oneFrameLength = Duration.millis(1000 / FRAME_RATE);
	final KeyFrame oneFrame = new KeyFrame(oneFrameLength, new EventHandler() {
	    @Override
	    public void handle(Event event) {
		// Update screen
		updateScreen(gc);
		// Check collisions
		// Pass pixels to AI
		// Etc
	    }
	});
	gameLoop = new Timeline(FRAME_RATE, oneFrame);
	gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    public void start() {
	gameLoop.playFromStart();
    }

    public void stop() {
	gameLoop.stop();
    }

    private void updateScreen(GraphicsContext gc) {
		player.move(inputs);
		player.update(gc);
	// Remove dead enemies and stuff
	enemies.forEach((Enemy enemy) -> { // This is java8 stream syntax combined with a lamda expression, it's really compact and legible so imma use it a lot
	    System.out.println(enemy);
	}); // This is filler
    }

    // Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	player = new Player();
	enemies = new ArrayList<>();

	gameArea.setHeight(600); // Hard-coded width and height oops
	gameArea.setWidth(800);
	gc = gameArea.getGraphicsContext2D();
    }
}
