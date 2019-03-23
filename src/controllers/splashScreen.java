package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author danushka
 */
public class splashScreen {
    public void show() throws IOException {
        FXMLLoader screen = new FXMLLoader(getClass().getResource("/resources/views/splashScreen.fxml"));
        Parent currentScreen  = (Parent) screen.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(currentScreen));
        stage.showAndWait();

    }
}
