package eclipse.gui;

import eclipse.gamecomponents.Enemy;
import eclipse.gamecomponents.Enemy1;
import eclipse.gamecomponents.GameObject;
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
    private List<GameObject> gameObjects;

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
            // Add enemy test
            if (code == KeyCode.Z) {
                Enemy enemy = new Enemy1();
                gameObjects.add(enemy);
                gameArea.getChildren().add(enemy);
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
//        application.setDimensions(gameArea);
    }

    private void updateScreen(Pane pane) {
        player.update();
        // Remove dead enemies and stuff
        gameObjects.forEach(obj -> {
            if (obj instanceof Player) {
                ((Player) obj).move(directionInput);
            } else if (obj instanceof Enemy) {
                Enemy enemy = (Enemy) obj;
                if (enemy.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    enemy.isAlive = false;
                }
                if (!enemy.isAlive) {
                    pane.getChildren().remove(enemy);
                    enemy = null;
                }
            }
            obj.update();
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
        gameObjects = new ArrayList<>();

        gameObjects.add(player);
        gameObjects.add(new Enemy1());

        gameArea.getChildren().addAll(gameObjects);
    }
}
