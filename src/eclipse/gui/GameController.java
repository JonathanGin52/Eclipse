package eclipse.gui;

import eclipse.gamecomponents.*;
import eclipse.gamecomponents.path.Up;
import eclipse.gamecomponents.path.UpLeft;
import eclipse.gamecomponents.path.UpRight;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GameController extends ParentController {

    private final Image FULL_HEART = new Image("file:resources/images/" + "heart.png");
    private final Image HALF_HEART = new Image("file:resources/images/" + "halfheart.png");

    private final AudioClip PAUSE_OPEN_CLIP = new AudioClip(new File("resources/audio/PauseMenu_Open.wav").toURI().toString());
    private final AudioClip PAUSE_CLOSE_CLIP = new AudioClip(new File("resources/audio/PauseMenu_Close.wav").toURI().toString());
    private final AudioClip ARROW_CLIP = new AudioClip(new File("resources/audio/Arrow_Shoot.wav").toURI().toString());
    private final AudioClip BOOMERANG_OUT = new AudioClip(new File("resources/audio/Boomerang_Start.wav").toURI().toString());

    private boolean paused = false;
    private double volume;
    private AnimationTimer gameLoop;
    private List<GameObject> gameObjects;
    private Score score;
    private Player player;
    private boolean[] directionInput = new boolean[4];
    private boolean pressedArrow = false;
    private boolean pressedBomb = false;
    private boolean pressedBoomerang = false;
    private double mouseX, mouseY;
    private boolean mouseMove;
    private LevelReader levelReader;
    private Random random = new Random();

    @FXML
    private AnchorPane root;
    @FXML
    private Pane gameArea;
    @FXML
    private HBox hearts;
    @FXML
    private Label highscoreLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelLabel;

    @Override
    public void init() {
        player = new Player();
        score = new Score(0);
        gameObjects = new ArrayList<>();
        levelReader = new LevelReader("level1.txt");

        // Add change listeners to score and health properties. When change is detected, update their respective labels
        score.scoreProperty().addListener(e -> scoreLabel.setText("Score: " + String.format("%06d", score.getScore())));
        player.getHealthProperty().addListener(e -> {
            if (checkGameOver()) {
                gameOver();
            } else {
                updateHearts();
            }
        });

        // Initialize HUD elements
        highscoreLabel.setText("High Score: " + String.format("%06d", application.getScores().get(0).getScore()));
        levelLabel.setText("Level: " + String.format("%02d", levelReader.getLevel()));
        updateHearts();

        gameObjects.add(player);
        gameArea.getChildren().addAll(gameObjects);

        installKeyListener(application.getScene());
        installMouseListener(application.getScene());
        setupGameLoop();

        // Setup audio
        volume = application.getVolume();
        BOOMERANG_OUT.setVolume(volume);
        PAUSE_OPEN_CLIP.setVolume(volume);
        PAUSE_CLOSE_CLIP.setVolume(volume);
        ARROW_CLIP.setVolume(volume);

        // Start the game
        gameLoop.start();
    }

    private void updateHearts() {
        hearts.getChildren().clear();
        int health = player.getHealth();
        // Update heart label
        while (health > 0) {
            ImageView img;
            if (health >= 2) {
                img = new ImageView(FULL_HEART);
                health--;
            } else {
                img = new ImageView(HALF_HEART);
            }
            health--;
            img.setFitWidth(15);
            img.setFitHeight(15);
            hearts.getChildren().add(img);
        }
    }

    private boolean checkGameOver() {
        return player.getHealth() <= 0;
    }

    private void setupGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update screen
                updateScreen(now);
            }
        };
    }

    private void installMouseListener(Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, (MouseEvent me) -> {
            mouseX = me.getSceneX();
            mouseY = me.getSceneY();
            mouseMove = true;
        });

        // Registers mouse pressed + move
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, (MouseEvent me) -> {
            mouseX = me.getSceneX();
            mouseY = me.getSceneY();
            mouseMove = true;
        });

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            MouseButton mb = me.getButton();

            List<GameObject> toAdd = new ArrayList<>();

            if (!player.insideEnemy) {
                if (mb == MouseButton.PRIMARY) {
                    toAdd.addAll(shootArrow());
                } else if (mb == MouseButton.SECONDARY) {
                    toAdd.addAll(launchBomb());
                }
            }

            if (!toAdd.isEmpty()) {
                gameArea.getChildren().addAll(toAdd);
                gameObjects.addAll(toAdd);
            }
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

            if (!player.insideEnemy) {
                if (code == KeyCode.SPACE && !pressedArrow) {
                    pressedArrow = true;
                    toAdd.addAll(shootArrow());
                }
                if (code == KeyCode.V && !pressedBoomerang) {
                    pressedBoomerang = true;
                    toAdd.addAll(shootBoomerang());
                }
                if (code == KeyCode.B && !pressedBomb) {
                    pressedBomb = true;
                    toAdd = launchBomb();
                }
            }

            if (code == KeyCode.L) { // Debugging purposes
                System.out.println(gameObjects);
                System.out.println(gameArea.getChildren());
            }
            if (code == KeyCode.ESCAPE) { // Debugging - instalose
                gameOver();
            }
            if (code == KeyCode.Q) { // Testing purposes
                player.arrowLevel++;
            }
            if (code == KeyCode.W) {
                player.boomerangLevel++;
            }

            if (code == KeyCode.P) {
                if (paused) {
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
                gameArea.getChildren().addAll(toAdd);
                gameObjects.addAll(toAdd);
            }
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent ke) -> {
            KeyCode code = ke.getCode();
            if (code.isArrowKey()) {
                directionInput[code.ordinal() - 16] = false;
            }
            if (code == KeyCode.SPACE) {
                pressedArrow = false;
            }
            if (code == KeyCode.V) {
                pressedBoomerang = false;
            }
            if (code == KeyCode.B) {
                pressedBomb = false;
            }
        });
    }

    private List<GameObject> shootArrow() {
        ARROW_CLIP.play();

        List<GameObject> toAdd = new ArrayList<>();
        switch (player.arrowLevel) {
            case 1:
                toAdd.add(new Arrow(player.getMidpointX(), player.getY(), 5, new Up(), false));
                break;
            case 2:
                toAdd.add(new Arrow(player.getMidpointX(), player.getY(), 10, new Up(), false));
                break;
            case 3:
                toAdd.add(new Arrow(player.getMidpointX() - 10, player.getY(), 10, new Up(), false));
                toAdd.add(new Arrow(player.getMidpointX() + 10, player.getY(), 10, new Up(), false));
                break;
            case 4:
                toAdd.add(new Arrow(player.getMidpointX() - 10, player.getY(), 20, new Up(), false));
                toAdd.add(new Arrow(player.getMidpointX() + 10, player.getY(), 20, new Up(), false));
                break;
            default:
                toAdd.add(new Arrow(player.getMidpointX() - 10, player.getY(), 25, new Up(), false));
                toAdd.add(new Arrow(player.getMidpointX() + 10, player.getY(), 25, new Up(), false));
                toAdd.add(new Arrow(player.getMidpointX() - 10, player.getY(), 25, new UpLeft(), false));
                toAdd.add(new Arrow(player.getMidpointX() + 10, player.getY(), 25, new UpRight(), false));
                break;
        }
        return toAdd;
    }

    private List<GameObject> shootBoomerang() {
        List<GameObject> toAdd = new ArrayList<>();
        if (player.boomerangOut) {
            System.out.println("Can't throw multiple boomerangs at once");
            return toAdd;
        }

        player.boomerangOut = true;
        BOOMERANG_OUT.play();

        System.out.println("boomerang throw");

        switch (player.boomerangLevel) {
            case 1:
                toAdd.add(new Boomerang(player.getMidpointX(), player.getY(), 5, false, player, gameObjects, 1));
                break;
            case 2:
                toAdd.add(new Boomerang(player.getMidpointX(), player.getY(), 5, false, player, gameObjects, 2));
                break;
            case 3:
                toAdd.add(new Boomerang(player.getMidpointX(), player.getY(), 5, false, player, gameObjects, 10));
                break;
            case 4:
                toAdd.add(new Boomerang(player.getMidpointX(), player.getY(), 10, false, player, gameObjects, 10));
                break;
            default:
                toAdd.add(new Boomerang(player.getMidpointX() - 20, player.getY(), 10, false, player, gameObjects, 10));
                toAdd.add(new Boomerang(player.getMidpointX() + 20, player.getY(), 10, false, player, gameObjects, 10));
                break;
        }

        return toAdd;
    }

    private List<GameObject> launchBomb() {
        List<GameObject> toAdd = new ArrayList<>(1);

        if (player.bombInv <= 0) {
            System.out.println("No more bombs");
            return toAdd;
        }
        player.bombInv--;
        System.out.println("Boom boom");

        toAdd.add(new Bomb(player.getMidpointX(), player.getY()));
        return toAdd;
    }

    private void updateScreen(long now) {
        List<GameObject> toAdd = new ArrayList<>(); // Adding to list to prevent concurrency issues (altering list during loop)
        List<GameObject> toRemove = new ArrayList<>(); // Adding toRemove list to prevent concurrency issues (altering list during loop)

        for (GameObject obj : gameObjects) {
            if (obj instanceof Player) {
                toRemove.addAll(updatePlayer((Player) obj));
            } else if (obj instanceof Enemy) {
                toAdd.addAll(updateEnemy((Enemy) obj));
                if (!((Enemy) obj).isAlive()) { // Remove enemy if dead
                    toRemove.add(obj);
                }
            } else if (obj instanceof Projectile) {
                Projectile proj = (Projectile) obj;
                if (proj.isDestroyed()) {
                    if (proj instanceof Boomerang) {
                        player.boomerangOut = false;
                    }
                    toRemove.add(proj);
                } else if (proj instanceof Bomb) {
                    updateProjectile((Bomb) proj);
                }
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
        player.insideEnemy = false;
        List<GameObject> hits = gameObjects.stream().filter(this::playerIntersection).collect(Collectors.toList());
        if (!hits.isEmpty()) {
            for (GameObject obj : hits) {
                System.out.println("hit");
                if (obj instanceof PowerUp) {
                    if (obj instanceof ArrowPowerUp) {
                        player.arrowLevel++;
                    } else if (obj instanceof BoomerangPowerUp) {
                        player.boomerangLevel++;
                    } else {
                        player.bombInv++;
                    }
                    toRemove.add(obj);
                } else if (obj instanceof Boomerang) {
                    if (((Boomerang) obj).getRemove()) {
                        toRemove.add(obj);
                        player.boomerangOut = false;
                    }
                } else {
                    // Periodically lose health when inside enemy
                    if (obj instanceof Enemy) {
                        player.isInsideEnemy();
                    }

                    // Lose 2 health if hit by enemy projectile
                    if (obj instanceof Projectile) {
                        player.loseHealth(2);
                        toRemove.add(obj);
                    }
                }
            }
        }
        return toRemove;
    }

    private boolean playerIntersection(GameObject gameObject) {
        if (gameObject instanceof PowerUp || gameObject instanceof Enemy) {
            return player.checkIntersection(gameObject);
        } else if (gameObject instanceof Projectile) {
            return player.checkIntersection(gameObject) && (gameObject instanceof Boomerang || ((Projectile) gameObject).isEnemyProj());
        }
        return false;
    }

    private List<GameObject> updateEnemy(Enemy enemy) {
        List<GameObject> toAdd = new ArrayList<>();

        // Check intersection of projectiles
        List<GameObject> proj = gameObjects.stream().filter(gameObject -> gameObject instanceof Projectile && !((Projectile) gameObject).isDestroyed() && gameObject.checkIntersection(enemy) && !((Projectile) gameObject).isEnemyProj()).collect(Collectors.toList());
        if (!proj.isEmpty()) {
            // allow bombs to boom and kill multiple, lasers to vanish and kill one
            // this implementation assumes bomb and laser do not strike at the same time
            for (GameObject object : proj) {
                Projectile projectile = (Projectile) object;

                if (!(projectile instanceof Bomb)) {
                    if (projectile instanceof Boomerang) {
                        ((Boomerang) projectile).setHitEnemy(enemy);
                    } else {
                        projectile.setDestroyed();
                    }
                }

                enemy.hit(projectile.getDamage());
            }

            if (!enemy.isAlive()) {
                score.add(enemy.getKillScore());

                // Maybe add a projectile
                if (enemy.dropItem()) {
                    // Decide a projectile
                    Decide:
                    while (true) {
                        switch (random.nextInt(3)) {
                            case 0: // Drop arrow upgrade
                                if (player.arrowLevel == 5) continue;
                                toAdd.add(new ArrowPowerUp(enemy.getX(), enemy.getY()));
                                break Decide;
                            case 1:
                                if (player.boomerangLevel == 5) continue;
                                toAdd.add(new BoomerangPowerUp(enemy.getX(), enemy.getY()));
                                break Decide;
                            case 2:
                                toAdd.add(new BombAdd(enemy.getX(), enemy.getY()));
                                break Decide;
                        }
                    }
                }
            }
        }

        if (enemy.fire()) {
            return enemy.getNewProjectiles().stream().map(e -> (GameObject) e).collect(Collectors.toList());
        }

        return toAdd;
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
        System.out.println("Game over");
        gameLoop.stop();
        application.stopMusic();
        MediaPlayer gameOverWAV = new MediaPlayer(new Media(new File("resources/audio/Game_Over.mp3").toURI().toString()));
        gameOverWAV.setVolume(volume);
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
