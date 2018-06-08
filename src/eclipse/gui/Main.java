package eclipse.gui;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private static final File SCORE_FILE = new File("resources/scores.txt");
    private static final Media TITLE_THEME = new Media(new File("resources/audio/Title.mp3").toURI().toString());
    private static final Media GERUDO_THEME = new Media(new File("resources/audio/Gerudo.mp3").toURI().toString());
    private static final Dimension2D dimensions = new Dimension2D(450, 600);
    private static MediaPlayer mediaPlayer = new MediaPlayer(TITLE_THEME);
    private static List<Score> scores = null;
    private final double MINIMUM_WINDOW_WIDTH = 450;
    private final double MINIMUM_WINDOW_HEIGHT = 600;
    private Stage stage;
    private Scene scene;
    private DoubleProperty volume = new SimpleDoubleProperty(1.0);

    public static void main(String[] args) {
        launch(args);
    }

    public static Dimension2D getDimensions() {
        return dimensions;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            stage.setTitle("ICS Summative");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            stage.setResizable(false);
            gotoScene("HomeScreen", false);

            // Add listener to volume property to auto set volume onchange
            volume.addListener(c -> mediaPlayer.setVolume(volume.getValue()));

            // Load scores
            loadScores();

            mediaPlayer.play();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Update score file with scores from current session
        stage.setOnCloseRequest(e -> {
            if (scores != null) {
                try (PrintWriter reiter = new PrintWriter(new BufferedWriter(new FileWriter(SCORE_FILE, false)))) {
                    for (int i = 0; i < scores.size(); i++) {
                        reiter.println(scores.get(i).getName() + "," + scores.get(i).getScore());
                    }
                } catch (IOException exception) {
                    System.out.println("IO Exception");
                    exception.printStackTrace();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    // Switches active scene
    void gotoScene(String scene, boolean switchMusic) {
        try {
            if (switchMusic) {
                mediaPlayer.stop();
                playMusic(scene);
            }
            ParentController sceneContent = (ParentController) replaceSceneContent(scene + ".fxml");
            sceneContent.setApp(this);
            sceneContent.init();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void playMusic(String scene) {
        switch (scene) {
            case "HomeScreen":
                mediaPlayer = new MediaPlayer(TITLE_THEME);
                break;
            case "Game":
                mediaPlayer = new MediaPlayer(GERUDO_THEME);
                break;
        }
        mediaPlayer.setVolume(getVolume());
        mediaPlayer.play();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loops music
    }

    public void stopMusic() {
        mediaPlayer.stop();
    }

    public Scene getScene() {
        return scene;
    }

    public List<Score> getScores() {
        return scores;
    }

    public List<Score> loadScores() {
        // Read scores and place into LinkedList (preserves insertion order)
        scores = new ArrayList<>(10);
        try (BufferedReader br = new BufferedReader(new FileReader(SCORE_FILE))) {
            for (int i = 1; i <= 10; i++) {
                String currentLine = br.readLine();
                if (currentLine == null || currentLine.isEmpty()) {
                    scores.add(new Score());
                } else {
                    int delim = currentLine.indexOf(",");
                    scores.add(new Score(currentLine.substring(0, delim), Integer.parseInt(currentLine.substring(delim + 1))));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        AnchorPane page;
        try (InputStream in = Main.class.getResourceAsStream(fxml)) {
            page = loader.load(in);
        }
        scene = new Scene(page, dimensions.getWidth(), dimensions.getHeight());
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    public double getVolume() {
        return volume.doubleValue();
    }

    public void setVolume(double volume) {
        this.volume.setValue(volume);
    }
}
