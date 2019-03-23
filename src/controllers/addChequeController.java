package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Cheque;

import java.io.IOException;
import java.sql.SQLException;

public class addChequeController {
    public JFXTextField cheque_id;
    public JFXTextField amount1;
    public JFXDatePicker issued_date;
    public JFXDatePicker expire_date;
    public JFXTextField bank_name;
    public JFXButton save;
    public JFXButton close;
    public JFXTextField branch_name;

    private JFXButton reports;

    public void save(MouseEvent mouseEvent) throws SQLException, IOException {
        try{
            String chequeNumber = cheque_id.getText().toString();
            double amount = Double.parseDouble(amount1.getText());
            String issued = java.sql.Date.valueOf(issued_date.getValue()).toString();
            String expire = java.sql.Date.valueOf(expire_date.getValue()).toString();
            String bank = bank_name.getText();
            String branch = branch_name.getText();
            Cheque current = new Cheque(chequeNumber,issued,expire,bank,branch,amount,2);
            current.save();
            Stage currentWin = (Stage) save.getScene().getWindow();
            currentWin.close();
        }catch (Exception e){
            warning.incomplete();
        }
    }

    public void close(MouseEvent mouseEvent) {
        Stage currentWin = (Stage) save.getScene().getWindow();
        currentWin.close();
    }

}
