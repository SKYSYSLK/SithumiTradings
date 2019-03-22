package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.t_cheque;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class chequesController implements Initializable {

    public TableView<t_cheque> chequesTable;
    public JFXButton back;
    public JFXButton add;
    public JFXTextField cheque_id;
    public JFXTextField amount1;
    public JFXDatePicker issued_date;
    public JFXDatePicker expire_date;
    public JFXTextField bank_name;
    public JFXTextField branch_name;
    public JFXComboBox type;
    public Text income;
    public Text profit;
    public TableColumn<t_cheque,String> cId;
    public TableColumn<t_cheque,String> cAmount;
    public TableColumn<t_cheque,String> cBank;
    public TableColumn<t_cheque,String> cBranch;
    public TableColumn<t_cheque,String> cIssueDate;
    public TableColumn<t_cheque,String> cExpireDate;
    public TableColumn<t_cheque,String> cType;

    public chequesController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cBank.setCellValueFactory(new PropertyValueFactory<>("bank"));
        cBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        cIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        cExpireDate.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
        cType.setCellValueFactory(new PropertyValueFactory<>("type"));
        cAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        chequesTable.setItems(chequesData);
    }

    private ObservableList<t_cheque> chequesData = FXCollections.observableArrayList(
            t_cheque.getAll()
    );

    public void updateItem(MouseEvent mouseEvent) {
    }

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)chequesTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Invoices");
        thisWindow.setScene(new Scene(root));
    }

    public void addNewCheque(MouseEvent mouseEvent) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("../resources/views/addCheque.fxml"));
        Stage model = new Stage();
        Parent root = load.load();
        model.setTitle("Add New Cheque");
        model.initModality(Modality.APPLICATION_MODAL);
        model.setScene(new Scene(root));
        model.show();
    }

    public void showDueCheques(MouseEvent mouseEvent) {
    }

    public void showIssuedCheques(MouseEvent mouseEvent) {
    }

    public void showSettledCheques(MouseEvent mouseEvent) {
    }
}
