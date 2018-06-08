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
import javafx.scene.effect.BoxBlur;
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

    private static final Image FULL_HEART = new Image("file:resources/images/" + "heart.png");
    private static final Image HALF_HEART = new Image("file:resources/images/" + "halfheart.png");
    private static final Image ARROW = new Image("file:resources/images/" + "arrow.png");
    private static final Image BOOMERANG = new Image("file:resources/images/" + "boomerang.png");
    private static final Image BOMB = new Image("file:resources/images/" + "bomb.png");

    private static final AudioClip PAUSE_OPEN_CLIP = new AudioClip(new File("resources/audio/PauseMenu_Open.wav").toURI().toString());
    private static final AudioClip PAUSE_CLOSE_CLIP = new AudioClip(new File("resources/audio/PauseMenu_Close.wav").toURI().toString());
    private static final AudioClip ARROW_CLIP = new AudioClip(new File("resources/audio/Arrow_Shoot.wav").toURI().toString());
    private static final AudioClip BOOMERANG_OUT = new AudioClip(new File("resources/audio/Boomerang_Start.wav").toURI().toString());
    private static final AudioClip HURT_CLIP = new AudioClip(new File("resources/audio/Hurt.wav").toURI().toString());
    private static final AudioClip LOW_HEALTH_CLIP = new AudioClip(new File("resources/audio/LowHealth.wav").toURI().toString());
    private static final AudioClip ITEM_CLIP = new AudioClip(new File("resources/audio/GainHp.wav").toURI().toString());
    private static final AudioClip ERROR_CLIP = new AudioClip(new File("resources/audio/Error.wav").toURI().toString());

    private final BoxBlur BLUR = new BoxBlur(450, 600, 1);

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
    private HBox bombsBox;
    @FXML
    private HBox arrowBox;
    @FXML
    private HBox boomerangBox;
    @FXML
    private HBox heartBox;
    @FXML
    private Label highscoreLabel;
    @FXML
    private Label scoreLabel;

    @Override
    public void init() {
        player = new Player();
        PowerUp.setPlayer(player);
        score = new Score(0);
        gameObjects = new ArrayList<>();
        levelReader = new LevelReader("enemy_groups.txt");

        // Add change listeners to score and health properties. When change is detected, update their respective labels
        score.scoreProperty().addListener(e -> scoreLabel.setText("Score: " + String.format("%06d", score.getScore())));
        player.getHealthProperty().addListener(e -> {
            heartBox.getChildren().clear();
            updateHearts();
            if (checkGameOver()) {
                gameOver();
            }
        });

        // Initialize HUD elements
        highscoreLabel.setText("High Score: " + String.format("%06d", application.getScores().get(0).getScore()));
        updateHearts();
        updateArrowBox();
        updateBoomerangBox();
        updateBombs();

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
        HURT_CLIP.setVolume(volume);
        ITEM_CLIP.setVolume(volume);
        ERROR_CLIP.setVolume(volume);

        // Start the game
        gameLoop.start();
    }

    private void updateArrowBox() {
        arrowBox.getChildren().clear();
        int arrows = player.arrowLevel;

        while (arrows > 0) {
            ImageView img = new ImageView(ARROW);
            img.setFitWidth(5);
            img.setFitHeight(24);
            arrows--;
            arrowBox.getChildren().add(img);
        }
    }

    private void updateBoomerangBox() {
        boomerangBox.getChildren().clear();
        int boomerangs = player.boomerangLevel;

        while (boomerangs > 0) {
            ImageView img = new ImageView(BOOMERANG);
            img.setFitWidth(18);
            img.setFitHeight(18);
            boomerangs--;
            boomerangBox.getChildren().add(img);
        }
    }

    private void updateBombs() {
        bombsBox.getChildren().clear();
        int bombs = player.bombInv;

        while (bombs > 0) {
            ImageView img = new ImageView(BOMB);
            img.setFitWidth(15);
            img.setFitHeight(15);
            bombs--;
            bombsBox.getChildren().add(img);
        }
    }

    private void updateHearts() {
        int health = player.getHealth();
        if (health <= 2 && health > 0) {
            LOW_HEALTH_CLIP.setCycleCount(AudioClip.INDEFINITE);
            LOW_HEALTH_CLIP.play();
        } else if (LOW_HEALTH_CLIP.isPlaying() && health > 2) {
            LOW_HEALTH_CLIP.stop();
        }
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
            heartBox.getChildren().add(img);
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

            if (code == KeyCode.P) {
                if (paused) {
                    PAUSE_CLOSE_CLIP.play();
                    System.out.println("Resumed");
                    gameLoop.start();
                    gameArea.setEffect(null);
                } else {
                    PAUSE_OPEN_CLIP.play();
                    System.out.println("Paused");
                    gameLoop.stop();
                    gameArea.setEffect(BLUR);
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
            ERROR_CLIP.play();
            return toAdd;
        }

        player.boomerangOut = true;
        BOOMERANG_OUT.play();

        switch (player.boomerangLevel) {
            case 1:
                toAdd.add(new Boomerang(player.getMidpointX(), player.getY(), 7, false, player, gameObjects, 1));
                break;
            case 2:
                toAdd.add(new Boomerang(player.getMidpointX(), player.getY(), 7, false, player, gameObjects, 2));
                break;
            case 3:
                toAdd.add(new Boomerang(player.getMidpointX(), player.getY(), 7, false, player, gameObjects, 10));
                break;
            case 4:
                toAdd.add(new Boomerang(player.getMidpointX(), player.getY(), 15, false, player, gameObjects, 10));
                break;
            default:
                toAdd.add(new Boomerang(player.getMidpointX() - 20, player.getY(), 15, false, player, gameObjects, 10));
                toAdd.add(new Boomerang(player.getMidpointX() + 20, player.getY(), 15, false, player, gameObjects, 10));
                break;
        }

        return toAdd;
    }

    private List<GameObject> launchBomb() {
        List<GameObject> toAdd = new ArrayList<>(1);

        if (player.bombInv <= 0) {
            return toAdd;
        }
        player.bombInv--;
        updateBombs();

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
                if (obj instanceof PowerUp) {
                    ITEM_CLIP.play();

                    if (obj instanceof ArrowPowerUp) {
                        player.arrowLevel++;
                        updateArrowBox();
                    } else if (obj instanceof BoomerangPowerUp) {
                        player.boomerangLevel++;
                        updateBoomerangBox();
                    } else if (obj instanceof BombAdd) {
                        player.bombInv++;
                        updateBombs();
                    } else {
                        player.gainHealth(2);
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
                        if (player.isInsideEnemy()) {
                            HURT_CLIP.play();
                        }
                    }

                    // Lose 2 health if hit by enemy projectile
                    if (obj instanceof Projectile) {
                        player.loseHealth(2);
                        HURT_CLIP.play();
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
                        switch (random.nextInt(4)) {
                            case 0: // Drop arrow upgrade
                                if (player.arrowLevel == 5) break Decide;
                                toAdd.add(new ArrowPowerUp(enemy.getX(), enemy.getY()));
                                break Decide;
                            case 1:
                                if (player.boomerangLevel == 5) break Decide;
                                toAdd.add(new BoomerangPowerUp(enemy.getX(), enemy.getY()));
                                break Decide;
                            case 2:
                                if (player.bombInv >= 3) break Decide;
                                toAdd.add(new BombAdd(enemy.getX(), enemy.getY()));
                                break Decide;
                            case 3:
                                if (player.getHeight() == 20) break Decide;
                                toAdd.add(new HeartAdd(enemy.getX(), enemy.getY()));
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

    private void gameOver() {
        gameLoop.stop();
        application.stopMusic();
        application.gotoScene("GameOver", false);
        MediaPlayer gameOverWAV = new MediaPlayer(new Media(new File("resources/audio/Game_Over.mp3").toURI().toString()));
        gameOverWAV.setVolume(volume);
        gameOverWAV.play();
        gameOverWAV.setOnEndOfMedia(() -> {
            List<Score> scores = application.getScores();
            int finalScore = this.score.getScore();
            for (int i = 0; i < scores.size(); i++) {
                if (finalScore >= scores.get(i).getScore()) {
                    // Popup to get name of player
                    TextInputDialog dialog = new TextInputDialog("Mr. Qayum");
                    dialog.setTitle("Only for the elites");
                    dialog.setHeaderText("Congratulations, you got a high-score of " + score.getScore() + "!");
                    dialog.setContentText("Please enter your name:");
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(name -> score.setName(name));
                    // Remove lowest score and add current score
                    scores.remove(9);
                    scores.add(i, this.score);
                    break;
                }
            }
        });
    }
}
