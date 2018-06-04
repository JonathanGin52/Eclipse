package eclipse.gui;

import eclipse.gamecomponents.*;
import eclipse.gamecomponents.path.Up;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameController extends ParentController {

    final AudioClip PAUSE_OPEN_CLIP = new AudioClip(new File("resources/audio/PauseMenu_Open.wav").toURI().toString());
    final AudioClip PAUSE_CLOSE_CLIP = new AudioClip(new File("resources/audio/PauseMenu_Close.wav").toURI().toString());
    final AudioClip ARROW_CLIP = new AudioClip(new File("resources/audio/Arrow_Shoot.wav").toURI().toString());
    boolean paused = false;
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
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            MouseButton mb = me.getButton();
            GameObject toAdd = null;
            if (mb == MouseButton.PRIMARY) {
                System.out.println("Pew pew");
                toAdd = new Laser(player.getMidpointX(), player.getMidpointY(), new Up(), false);
            } else if (mb == MouseButton.SECONDARY) {
                System.out.println("Boom boom");
                toAdd = new Bomb(player.getMidpointX(), player.getMidpointY());
            }
            if (toAdd != null) {
                gameArea.getChildren().add(toAdd);
                gameObjects.add(toAdd);
            }
        });
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, (MouseEvent me) -> {
            MouseButton mb = me.getButton();
            GameObject toAdd = null;
            if (mb == MouseButton.PRIMARY) {
                System.out.println("Pew pew");
                toAdd = new Laser(player.getMidpointX(), player.getMidpointY(), new Up(), false);
            } else if (mb == MouseButton.SECONDARY) {
                System.out.println("Boom boom");
                toAdd = new Bomb(player.getMidpointX(), player.getMidpointY());
            }
            if (toAdd != null) {
                gameArea.getChildren().add(toAdd);
                gameObjects.add(toAdd);
            }
        });
    }

    private void installKeyListener(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            GameObject toAdd = null;
            KeyCode code = ke.getCode();
            if (code.isArrowKey()) {
                directionInput[code.ordinal() - 16] = true;
                mouseMove = false;
            }
            if (code == KeyCode.SPACE) {
                System.out.println("Pew pew");
                ARROW_CLIP.play();
                toAdd = new Laser(player.getMidpointX(), player.getMidpointY(), new Up(), false);
            }
            if (code == KeyCode.B) {
                System.out.println("Boom boom");
                toAdd = new Bomb(player.getMidpointX(), player.getMidpointY());
            }
            if (code == KeyCode.L) { // Debugging purposes
                System.out.println(gameObjects);
                System.out.println(gameArea.getChildren());
            }
            if (code == KeyCode.ESCAPE) { // Debugging - instalose
                gameOver();
            }
            if (code == KeyCode.P) {
                if (paused == true) {
                    PAUSE_CLOSE_CLIP.play();
                    System.out.println("Resumed");
                    gameLoop.start();
                } else {
                    PAUSE_OPEN_CLIP.play();
                    System.out.println("Paused");
                    gameLoop.stop();
                }
                paused = !paused;
            }
            if (toAdd != null) {
                gameArea.getChildren().add(toAdd);
                gameObjects.add(toAdd);
            }
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
        AudioClip gameOverWAV = new AudioClip(new File("resources/audio/Game_Over.mp3").toURI().toString());
        gameOverWAV.play();
        gameLoop.stop();

        List<Score> scores = application.getScores();
        int finalScore = this.score.getScore();
        for (int i = 0; i < scores.size(); i++) {
            if (finalScore >= scores.get(i).getScore()) {
                // Popup to get name of player
                TextInputDialog dialog = new TextInputDialog("John");
                dialog.setTitle("Text Input Dialog");
                dialog.setHeaderText("Look, a Text Input Dialog");
                dialog.setContentText("Please enter your name:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> score.setName(name));

                scores.remove(9);
                scores.add(i, this.score);

                break;
            }
        }

        returnHome(); // Return to main screen
    }
}
