package eclipse.gui;

import eclipse.gamecomponents.Enemy;
import eclipse.gamecomponents.Enemy1;
import eclipse.gamecomponents.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class GameController implements Initializable {

    boolean[] directionInput = new boolean[4];
    private Main application;
    private Timeline gameLoop;
    private Player player;
    private List<Enemy> enemies;

    @FXML
    private AnchorPane root;
    @FXML
    private Pane gameArea;

    public void setApp(Main application) {
        this.application = application;
    }

    private void installKeyListener(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            KeyCode code = ke.getCode();
            if (code.isArrowKey()) {
                directionInput[code.ordinal() - 16] = true;
            }
            if (code == KeyCode.SPACE) {
                System.out.println("Pew pew");
            }
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent ke) -> {
            KeyCode code = ke.getCode();
            if (code.isArrowKey()) {
                directionInput[code.ordinal() - 16] = false;
            }
        });
    }

    void initGame(final int FRAME_RATE) {
        installKeyListener(application.getScene());
        final Duration oneFrameLength = Duration.millis(1000 / FRAME_RATE);
        final KeyFrame oneFrame = new KeyFrame(oneFrameLength, event -> {
            // Update screen
            updateScreen(gameArea);
            // Check collisions
            checkCollisions();
            // Pass pixels to AI
            // Etc
        });
        gameLoop = new Timeline(FRAME_RATE, oneFrame);
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    private void updateScreen(Pane pane) {
        player.move(directionInput);
        player.update(pane);
        // Remove dead enemies and stuff
        enemies.forEach((Enemy enemy) -> { // This is java8 stream syntax combined with a lambda expression, it's really compact and legible so imma use it a lot
            enemy.update(pane);
        });
    }

    private void checkCollisions() {

    }

    public void start() {
        gameLoop.playFromStart();
    }

    public void stop() {
        gameLoop.stop();
    }

    // Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        player = new Player();
        enemies = new ArrayList<>();
        enemies.add(new Enemy1());
    }
}
