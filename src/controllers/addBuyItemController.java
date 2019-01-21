package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.buy;
import models.item;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author danushka
 */
public class addBuyItemController {
    public JFXTextField itemId;
    public JFXTextField itemName;
    public JFXTextField quantity;
    public JFXTextField buyPrice;
    public JFXTextField sellPrice;
    public JFXDatePicker day;
    public JFXDatePicker paymentDate;
    public JFXTextField checkNo;
    public JFXButton save;
    public JFXButton close;

    public void findItem(MouseEvent mouseEvent) throws SQLException {
        String itemID = itemId.getText();
        item current = item.getItem(itemID);
        if(current!=null){
            itemName.setText(current.getName());
        }else{
            itemName.setText("");
        }
    }

    public void save(MouseEvent mouseEvent) throws SQLException, IOException {
        if(!itemName.getText().equals("")) {
            String itemID = itemId.getText();
            int quanty = Integer.parseInt(quantity.getText());
            float bPrice = Float.parseFloat(buyPrice.getText());
            float sPrice = Float.parseFloat(sellPrice.getText());
            String today = java.sql.Date.valueOf(day.getValue()).toString();
            String paymentD = java.sql.Date.valueOf(paymentDate.getValue()).toString();
            String chque = checkNo.getText();
            buy newBuy = new buy(itemID,quanty,bPrice,sPrice,today,paymentD,chque);
            newBuy.save();
            Stage current = (Stage) save.getScene().getWindow();
            current.close();
        }else{
            warning.incomplete();
        }
    }

    public void close(MouseEvent mouseEvent) {
        Stage current = (Stage) save.getScene().getWindow();
        current.close();
    }
}
