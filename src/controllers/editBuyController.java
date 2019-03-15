package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class editBuyController {
    public TableView invoiceItemTable;
    public TableColumn item_no;
    public TableColumn name;
    public TableColumn quantity;
    public TableColumn buyPrice;
    public TableColumn sellPrice;
    public JFXButton back;
    public JFXTextField invoice_id;
    public JFXButton updateInvoice;
    public JFXComboBox cheque_no;
    public JFXButton addCheque;
    public JFXComboBox shop_id;
    public JFXButton addShop;
    public JFXDatePicker date_issue;
    public Text error1;
    public Text error4;
    public JFXTextField itemId;
    public JFXButton updateItem;
    public JFXTextField itemName;
    public JFXTextField itemQuantity;
    public JFXTextField itemBuyPrice;
    public JFXTextField itemSellPrice;
    public Text error2;
    public Text error3;

    public void fetchData(InputMethodEvent mouseEvent) {
    }

    public void saveItem(MouseEvent mouseEvent) {
    }

    public void addShopWindow(MouseEvent mouseEvent) {
    }

    public void addChequeDialog(MouseEvent mouseEvent) {
    }

    public void saveInvoice(MouseEvent mouseEvent) {
    }

    public void hideError(KeyEvent keyEvent) {
    }

    public void backMenu(MouseEvent mouseEvent) {
    }

    public void getSelected(MouseEvent mouseEvent) {
    }
}
