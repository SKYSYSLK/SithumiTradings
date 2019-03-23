package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Report;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class reportController implements Initializable {

    public TableView<Report> reportTable;
    
    public JFXButton generate_report;
    public RadioButton shop_reports_radio;
    public RadioButton time_reports_radio;
    public ComboBox shop_list;
    public DatePicker from_date;
    public DatePicker to_date;
    public JFXButton back;

    public reportController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage) reportTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("/resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu");
        thisWindow.setScene(new Scene(root));
    }

    public void generateReport() {
        Stage thisWindow = (Stage) reportTable.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        File file = fileChooser.showSaveDialog(thisWindow);
        if( file != null) {
            try {
                PDDocument testDoc = new PDDocument();
                PDPage blankPage = new PDPage();
                testDoc.addPage(blankPage);
                testDoc.save(file);
                testDoc.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
