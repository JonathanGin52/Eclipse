package eclipse.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jonathan Gin, Justin Reiter, Alex Yang
 */
public class Main extends Application {

    private static final Dimension2D dimensions = new Dimension2D(450, 600);
    private final double MINIMUM_WINDOW_WIDTH = 390.0;
    private final double MINIMUM_WINDOW_HEIGHT = 500.0;
    private final int FRAME_RATE = -1; // -1 to automatically set frame rate
    private Stage stage;
    private Scene scene;

    /**
     * @param args the command line arguments
     */
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

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        AnchorPane page;
        try {
            page = loader.load(in);
        } finally {
            in.close();
        }
        scene = new Scene(page, dimensions.getWidth(), dimensions.getHeight());
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
}
