package eclipse.gui;

import eclipse.gamecomponents.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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

    private Main application;
    private AnimationTimer gameLoop;
    private List<GameObject> gameObjects;
    private Score score;
    private Player player;
    private boolean[] directionInput = new boolean[4];
    private double mouseX, mouseY;
    private boolean mouseMove;

    @FXML
    private AnchorPane root;
    @FXML
    private Pane gameArea;
    @FXML
    private Label scoreLabel;

    public void setApp(Main application) {
        this.application = application;
    }

    private void installKeyListener(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            List<GameObject> toAdd = new ArrayList<>();
            KeyCode code = ke.getCode();
            if (code.isArrowKey()) {
                directionInput[code.ordinal() - 16] = true;
                mouseMove = false;
            }
            if (code == KeyCode.SPACE) {
                System.out.println("Pew pew");
            }
            if (code == KeyCode.B) {
                System.out.println("Boom boom");
                toAdd.add(new Bomb(player.getMidpointX(), player.getMidpointY()));
            }
            if (code == KeyCode.L) { // Debugging purposes
                System.out.println(gameObjects);
                System.out.println(gameArea.getChildren());
            }
            // Add enemy test
            if (code == KeyCode.J) {
                toAdd.add(new Enemy1());
            }
            gameArea.getChildren().addAll(toAdd);
            gameObjects.addAll(toAdd);
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent ke) -> {
            KeyCode code = ke.getCode();
            if (code.isArrowKey()) {
                directionInput[code.ordinal() - 16] = false;
            }
        });
    }

    private void installMouseListener(Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, (MouseEvent me) -> {
            mouseX = me.getSceneX();
            mouseY = me.getSceneY();

            mouseMove = true;
        });
    }

    void initGame(final int FRAME_RATE) {
        installKeyListener(application.getScene());
        installMouseListener(application.getScene());
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update screen
                updateScreen(now);
                // Pass pixels to AI
                // Etc
            }
        };
//        application.setDimensions(gameArea);
    }

    private void updateScreen(long now) {
        List<GameObject> toRemove = new ArrayList<>(); // Adding toRemove list to prevent concurrency issues (altering list during loop)
        for (GameObject obj : gameObjects) {
            if (obj instanceof Player) {
                if (mouseMove) {
                    ((Player) obj).mouseMove(mouseX, mouseY);
                } else {
                    ((Player) obj).move(directionInput);
                }
            } else if (obj instanceof Enemy) {
                Enemy enemy = (Enemy) obj;
                if (player.checkIntersection(enemy)) {
                    score.add(enemy.kill());
                    toRemove.add(enemy);
                }
            } else if (obj instanceof Bomb) {
                if (((Bomb) obj).isDestroyed()) {
                    toRemove.add(obj);
                }
            }
            obj.update(now);
        }
        gameArea.getChildren().removeAll(toRemove);
        gameObjects.removeAll(toRemove);
    }

    public void start() {
        gameLoop.start();
    }

    public void stop() {
        gameLoop.stop();
    }

    // Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        player = new Player();
        score = new Score(0);
        // Add change listener to score property. When change is detected, update scoreLabel
        score.scoreProperty().addListener((o) -> scoreLabel.setText("Score: " + String.format("%06d", score.getScore())));

        gameObjects = new ArrayList<>();

        gameObjects.add(player);

        gameArea.getChildren().addAll(gameObjects);
    }
}
