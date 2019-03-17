package controllers.alert;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class selectItemErrorController {

    public JFXButton close;

    public void close(ActionEvent actionEvent) {
        Stage current = (Stage) close.getScene().getWindow();
        current.close();
    }
}
