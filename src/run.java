import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author danushka
 */
public class run extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Splash Screen
        Parent root = FXMLLoader.load(getClass().getResource("resources/views/splashScreen.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.show();
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e->{
                primaryStage.getScene().getWindow().hide();
                    try {
                        startMenu();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
        );
        pause.play();
    }
    public static void main(String args[]){
        launch(args);
    }
    public void startMenu() throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("resources/views/mainMenu.fxml"));
        Stage new1 = new Stage();
        new1.setTitle("Main Menu");
        new1.setScene(new Scene(root2));
        new1.show();
    }

}
