package controllers.alert;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.awt.*;

/**
 * @author danushka
 */
public class saveSuccess {
    public JFXButton close;

    public void close(ActionEvent actionEvent) {
        Stage current = (Stage) close.getScene().getWindow();
        current.close();
    }

    public void addnew(ActionEvent actionEvent) {
    }
}
