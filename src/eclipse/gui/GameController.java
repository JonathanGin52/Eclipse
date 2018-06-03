package eclipse.gui;

import eclipse.gamecomponents.*;
import eclipse.gamecomponents.path.Up;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FXML Controller class
 *
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class GameController extends ParentController {

    private AnimationTimer gameLoop;
    private List<GameObject> gameObjects;
    private Score score;
    private Player player;
    private boolean[] directionInput = new boolean[4];
    private double mouseX, mouseY;
    private boolean mouseMove;
    private LevelReader levelReader;

    @FXML
    private AnchorPane root;
    @FXML
    private Pane gameArea;
    @FXML
    private Label scoreLabel;

    @Override
    public void init() {
        player = new Player(); // I'm not sure if proper practice tells us to initialize objects here, or if we can just do it at declaration ^^^
        score = new Score(0);
        gameObjects = new ArrayList<>();

        // Add change listener to score property. When change is detected, update scoreLabel
        score.scoreProperty().addListener((o) -> scoreLabel.setText("Score: " + String.format("%06d", score.getScore())));

        gameObjects.add(player);
        gameArea.getChildren().addAll(gameObjects);

        levelReader = new LevelReader("level1.txt");

        installKeyListener(application.getScene());
        installMouseListener(application.getScene());
        setupGameLoop();
        // Start the game
        gameLoop.start();
    }

    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update screen
                updateScreen(now);
                // Pass pixels to AI
                // Etc
            }
        };
    }

    private void installMouseListener(Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, (MouseEvent me) -> {
            mouseX = me.getSceneX();
            mouseY = me.getSceneY();
            mouseMove = true;
        });
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
                toAdd.add(new Laser(player.getMidpointX(), player.getMidpointY(), new Up(), false));
            }
            if (code == KeyCode.B) {
                System.out.println("Boom boom");
                toAdd.add(new Bomb(player.getMidpointX(), player.getMidpointY()));
            }
            if (code == KeyCode.L) { // Debugging purposes
                System.out.println(gameObjects);
                System.out.println(gameArea.getChildren());
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

    private void updateScreen(long now) {
        List<GameObject> toAdd = new ArrayList<>(); // Adding to list to prevent concurrency issues (altering list during loop)
        List<GameObject> toRemove = new ArrayList<>(); // Adding toRemove list to prevent concurrency issues (altering list during loop)

        for (GameObject obj : gameObjects) {
            if (obj instanceof Player) {
                if (mouseMove) {
                    ((Player) obj).mouseMove(mouseX, mouseY);
                } else {
                    ((Player) obj).keyMove(directionInput);
                }
            } else if (obj instanceof Enemy) {
                Enemy enemy = (Enemy) obj;
                // Check intersection of projectiles
                List<GameObject> proj = gameObjects.stream().filter(gameObject -> gameObject instanceof Projectile && !((Projectile) gameObject).isDestroyed() && gameObject.checkIntersection(obj) && !((Projectile) gameObject).isEnemyProj()).collect(Collectors.toList());
                if (!proj.isEmpty()) {
                    // allow bombs to boom and kill multiple, lasers to vanish and kill one
                    // this implementation assumes bomb and laser do not strike at the same time
                    if (!(proj.get(0) instanceof Bomb)) {
                        ((Projectile) proj.get(0)).setDestroyed();
                    }

                    score.add(enemy.kill());
                }

                if (enemy.fire()) {
                    toAdd.addAll(enemy.getNewProjectiles());
                }

                if (!enemy.isAlive()) {
                    toRemove.add(enemy);
                }
            } else if (obj instanceof Projectile) {
                if (((Projectile) obj).isDestroyed()) {
                    toRemove.add(obj);
                } else {
                    List<GameObject> collideEnemy = gameObjects.stream().filter(gameObject -> gameObject instanceof Enemy && gameObject.checkIntersection(obj)).collect(Collectors.toList());
                    if (obj instanceof Bomb) { // Bomb
                        Bomb bomb = (Bomb) obj;
                        if (!collideEnemy.isEmpty()) {
                            bomb.explode();
                        }
                    } else { // Laser
                        Laser laser = (Laser) obj;
                    }
                }
            }
            obj.update(now);
        }

        toAdd.addAll(levelReader.getNewObjects(now));

        gameArea.getChildren().addAll(toAdd);
        gameObjects.addAll(toAdd);
        gameArea.getChildren().removeAll(toRemove);
        gameObjects.removeAll(toRemove);
    }

    public void start() {
        gameLoop.start();
    }

    public void stop() {
        gameLoop.stop();
    }

    private void gameOver() {
        gameLoop.stop();

        int finalScore = score.getScore();
        try (BufferedReader br = new BufferedReader(new FileReader(application.SCORE_FILE))) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
