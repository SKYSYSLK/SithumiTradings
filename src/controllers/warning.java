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
public class warning {
    public static void saveSuccess() throws IOException {
        FXMLLoader errorWindow = new FXMLLoader(warning.class.getResource("/resources/views/alert/saveSuccess.fxml"));
        Stage current = new Stage();
        Parent model = (Parent) errorWindow.load();
        current.setTitle("Save Successfully");
        current.setScene(new Scene(model));
        current.show();
    }

    public static void deleteSuccess() throws IOException {
        FXMLLoader errorWindow = new FXMLLoader(warning.class.getResource("/resources/views/alert/deleteSuccess.fxml"));
        Stage current = new Stage();
        Parent model = (Parent) errorWindow.load();
        current.setTitle("Deleted Successfully");
        current.setScene(new Scene(model));
        current.show();
    }

    public static void updateSuccess() throws IOException {
//        FXMLLoader errorWindow = new FXMLLoader(warning.class.getResource("/resources/views/alert/updateSuccess.fxml"));
//        Stage current = new Stage();
//        Parent model = (Parent) errorWindow.load();
//        current.setTitle("Updated Successfully");
//        current.setScene(new Scene(model));
//        current.show();
    }

    static void incomplete() throws IOException {
        FXMLLoader errorWindow = new FXMLLoader(warning.class.getResource("/resources/views/alert/saveFail.fxml"));
        Stage current = new Stage();
        Parent model = (Parent) errorWindow.load();
        current.setTitle("Save Unsuccessful");
        current.setScene(new Scene(model));
        current.initModality(Modality.APPLICATION_MODAL);
        current.show();
    }

    static void notSelected() throws IOException {
        FXMLLoader errorWindow = new FXMLLoader(warning.class.getResource("/resources/views/alert/notselected.fxml"));
        Stage current = new Stage();
        Parent model = (Parent) errorWindow.load();
        current.setTitle("Item not selected");
        current.setScene(new Scene(model));
        current.initModality(Modality.APPLICATION_MODAL);
        current.show();
    }
}
