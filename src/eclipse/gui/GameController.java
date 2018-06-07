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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameController extends ParentController {

    private final AudioClip PAUSE_OPEN_CLIP = new AudioClip(new File("resources/audio/PauseMenu_Open.wav").toURI().toString());
    private final AudioClip PAUSE_CLOSE_CLIP = new AudioClip(new File("resources/audio/PauseMenu_Close.wav").toURI().toString());
    private final AudioClip ARROW_CLIP = new AudioClip(new File("resources/audio/Arrow_Shoot.wav").toURI().toString());
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
        score.scoreProperty().addListener(e -> scoreLabel.setText("Score: " + String.format("%06d", score.getScore())));
        player.getHealthProperty().addListener(e -> {
            if (checkGameOver()) {
                gameOver();
            }
            System.out.println(player.getHealthProperty());
        });

        gameObjects.add(player);
        gameArea.getChildren().addAll(gameObjects);

        levelReader = new LevelReader("level1.txt");

        installKeyListener(application.getScene());
        installMouseListener(application.getScene());
        setupGameLoop();
        // Start the game
        gameLoop.start();
    }

    private boolean checkGameOver() {
        if (player.getHealth() <= 0) {
            return true;
        }
        return false;
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
        // Would be nice if we could figure out how to fire and move at the same time (mouse only)
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            MouseButton mb = me.getButton();
            GameObject toAdd = null;
            if (mb == MouseButton.PRIMARY) {
                toAdd = shootArrow();
            } else if (mb == MouseButton.SECONDARY) {
                toAdd = launchBomb();
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
                toAdd = shootArrow();
            }
            if (code == KeyCode.V) {
                toAdd = shootBoomerang();
            }
            if (code == KeyCode.B) {
                toAdd = launchBomb();
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

    private GameObject shootArrow() {
        ARROW_CLIP.play();
        System.out.println("Pew pew");

        return new Arrow(player.getMidpointX(), player.getY(), 10, new Up(), false);
    }

    private GameObject shootBoomerang() {
        // Play clip
        // TODO

        System.out.println("boomerang throw");
        return new Boomerang(player.getMidpointX(), player.getY(), 7, false, player, gameObjects, 1);
    }

    private GameObject launchBomb() {
        if (player.bombInv <= 0) {
            System.out.println("No more bombs");
            return null;
        }
        player.bombInv--;
        System.out.println("Boom boom");
        return new Bomb(player.getMidpointX(), player.getY());
    }

    private void updateScreen(long now) {
        List<GameObject> toAdd = new ArrayList<>(); // Adding to list to prevent concurrency issues (altering list during loop)
        List<GameObject> toRemove = new ArrayList<>(); // Adding toRemove list to prevent concurrency issues (altering list during loop)

        for (GameObject obj : gameObjects) {
            if (obj instanceof Player) {
                toRemove.addAll(updatePlayer((Player) obj));
            } else if (obj instanceof Enemy) {
                Optional.ofNullable(updateEnemy((Enemy) obj)).ifPresent(toAdd::addAll); // Add if not null
                if (!((Enemy) obj).isAlive()) { // Remove enemy if dead
                    toRemove.add(obj);
                }
            } else if (obj instanceof Projectile) {
                Projectile proj = (Projectile) obj;
                if (proj.isDestroyed()) {
                    toRemove.add(proj);
                } else if (proj instanceof Bomb) {
                    updateProjectile((Bomb) proj);
                }
                // Arrow update is handled by Enemy and Player
            }
            // Visual updates
            obj.update(now);
        }
        // Add spawned enemies from levelReader
        toAdd.addAll(levelReader.getNewObjects(now, player));

        gameArea.getChildren().addAll(toAdd);
        gameObjects.addAll(toAdd);
        gameArea.getChildren().removeAll(toRemove);
        gameObjects.removeAll(toRemove);
    }

    // Returns list of things toRemove
    private List<GameObject> updatePlayer(Player player) {
        List<GameObject> toRemove = new LinkedList<>();
        // Player movement
        if (mouseMove) {
            (player).mouseMove(mouseX, mouseY);
        } else {
            (player).keyMove(directionInput);
        }

        // Check if player was hit by anything (enemy, projectile, powerup)
        List<GameObject> hits = gameObjects.stream().filter(this::playerIntersection).collect(Collectors.toList());
        if (!hits.isEmpty()) {
            for (GameObject obj : hits) {
                System.out.println("hit");
                if (obj instanceof PowerUp) {
                    // TODO
                } else if (obj instanceof Boomerang) {
                    if (((Boomerang) obj).getRemove() == true) {
                        toRemove.add(obj);
                    } else {
                        continue;
                    }
                } else {
                    // Lose 2 health if hit by enemy bullet, lose 1 if collision with enemy
                    player.loseHealth(obj instanceof Projectile ? 2 : 1);

                    if (obj instanceof  Enemy) {
                        ((Enemy) obj).remove();
                    }
                    toRemove.add(obj);
                }
            }
        }
        return toRemove;
    }

    private boolean playerIntersection(GameObject gameObject) {
        if (gameObject instanceof PowerUp || gameObject instanceof Enemy) {
            return player.checkIntersection(gameObject);
        } else if (gameObject instanceof Projectile) {
            return player.checkIntersection(gameObject) && (gameObject instanceof  Boomerang || ((Projectile) gameObject).isEnemyProj());
        }
        return false;
    }

    private List<GameObject> updateEnemy(Enemy enemy) {
        // Check intersection of projectiles
        List<GameObject> proj = gameObjects.stream().filter(gameObject -> gameObject instanceof Projectile && !((Projectile) gameObject).isDestroyed() && gameObject.checkIntersection(enemy) && !((Projectile) gameObject).isEnemyProj()).collect(Collectors.toList());
        if (!proj.isEmpty()) {
            // allow bombs to boom and kill multiple, lasers to vanish and kill one
            // this implementation assumes bomb and laser do not strike at the same time
            if (!(proj.get(0) instanceof Bomb)) {
                if (proj.get(0) instanceof Boomerang) {
                    ((Boomerang) proj.get(0)).setHitEnemy(enemy);
                } else {
                    ((Projectile) proj.get(0)).setDestroyed();
                }
            }

            score.add(enemy.kill());
        }

        if (enemy.fire()) {
            // Very hacky, would prefer a cleaner more efficient soln
            return enemy.getNewProjectiles().stream().map(e -> (GameObject) e).collect(Collectors.toList());
        }
        return null;
    }

    private void updateProjectile(Bomb bomb) {
        List<GameObject> collideEnemy = gameObjects.stream().filter(gameObject -> gameObject instanceof Enemy && gameObject.checkIntersection(bomb)).collect(Collectors.toList());
        if (!collideEnemy.isEmpty()) {
            bomb.explode();
        }
    }

    public void start() {
        gameLoop.start();
    }

    public void stop() {
        gameLoop.stop();
    }

    private void gameOver() {
        gameLoop.stop();
        application.stopMusic();
        MediaPlayer gameOverWAV = new MediaPlayer(new Media(new File("resources/audio/Game_Over.mp3").toURI().toString()));
        gameOverWAV.play();
        gameOverWAV.setOnEndOfMedia(() -> {
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
                    // Remove lowest score and add current score
                    scores.remove(9);
                    scores.add(i, this.score);
                    break;
                }
            }
            returnHome(true); // Return to main screen

        });
    }
}
