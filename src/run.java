import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * @author danushka
 */
public class run extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/views/splashScreen.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.show();
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e->primaryStage.getScene().getWindow().hide());
        pause.play();
    }
    public static void main(String args[]){
        launch(args);
    }
}
