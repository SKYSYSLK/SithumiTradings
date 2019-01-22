package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * @author danushka
 */
public class dailyController {
    public JFXTextField itemId;
    public JFXTextField itemName;
    public JFXTextField itemquantity;
    public JFXTextField buyPrice;
    public JFXTextField itemSellPrice;
    public JFXButton update;
    public Text income;
    public Text spent;
    public Text profit;
    public TableView itemTable;
    public TableColumn item_no;
    public TableColumn name;
    public TableColumn quantity;
    public TableColumn sellPrice;
    public TableColumn Total;
    public TableColumn itemProfit;
    public JFXButton back;
    public JFXDatePicker date;

    public void getSelected(MouseEvent mouseEvent) {
    }

    public void backMenu(MouseEvent mouseEvent) {
    }

    public void getName(MouseEvent mouseEvent) {
    }

    public void updateItem(MouseEvent mouseEvent) {
    }
}
