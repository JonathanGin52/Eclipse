package eclipse.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    public static final File SCORE_FILE = new File("resources/scores.txt");
    private static final Dimension2D dimensions = new Dimension2D(450, 600);
    private static List<Score> scores = null;
    private final double MINIMUM_WINDOW_WIDTH = 450;
    private final double MINIMUM_WINDOW_HEIGHT = 600;
    private Stage stage;
    private Scene scene;

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
            gotoScene("HomeScreen");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        stage.setOnCloseRequest(e -> {
            try (PrintWriter reiter = new PrintWriter(new FileWriter(SCORE_FILE, false))) {
                for (int i = 0; i < scores.size(); i++) {
                    reiter.println(scores.get(i).getName() + "," + scores.get(i).getScore());
                }
            } catch (IOException exception) {
                System.out.println("IO Exception");
                exception.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    void gotoScene(String scene) {
        try {
            ParentController sceneContent = (ParentController) replaceSceneContent(scene + ".fxml");
            sceneContent.setApp(this);
            sceneContent.init();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Scene getScene() {
        return scene;
    }

    public List<Score> getScores() {
        return scores == null ? loadScores() : scores;
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
        System.out.println(scores);
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
}
