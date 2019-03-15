package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author danushka
 */
public class buyController implements Initializable {
    public TableColumn<t_invoiceItem, String> item_no;
    public TableColumn<t_invoiceItem, String> name;
    public TableColumn<t_invoiceItem, Integer> quantity;
    public TableColumn<t_invoiceItem, Float> buyPrice;
    public TableColumn<t_invoiceItem, Float> sellPrice;

    public JFXButton back;
    public TableView<t_invoiceItem> invoiceItemTable;
    public JFXTextField invoice_id;
    public JFXButton addInvoice;
    public JFXComboBox cheque_no;
    public JFXButton addCheque;
    public JFXComboBox shop_id;
    public JFXButton addShop;
    public JFXDatePicker date_issue;
    public JFXTextField itemId;

    @FXML
    private JFXButton addItem;

    @FXML
    private JFXTextField itemName;

    @FXML
    private JFXTextField itemQuantity;

    @FXML
    private JFXTextField itemBuyPrice;

    @FXML
    private JFXTextField itemSellPrice;
//    private static TableView<buy> itemTable1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        item_no.setCellValueFactory(new PropertyValueFactory<>("item_no"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        buyPrice.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        invoiceItemTable.setItems(itemData);
//        itemTable1 = itemTable;
        try {
            fillShopCombo();
            fillChequeCombo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private ObservableList<t_invoiceItem> itemData = FXCollections.observableArrayList(
            t_invoiceItem.getItems("5547")
    );

    private void fillShopCombo() throws SQLException {
        ArrayList<Shop> allShops = Shop.getAll();
        for(Shop shop:allShops){
            shop_id.getItems().add(shop.getName());
        }
    }

    private void fillChequeCombo() throws SQLException {
        ArrayList<Cheque> allCheques = Cheque.getAll();
        for(Cheque cheque:allCheques){
            cheque_no.getItems().add(cheque.getId());
        }
    }

    public buyController() throws SQLException {
    }

    public void getSelected(MouseEvent mouseEvent) {
    }

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)invoiceItemTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/buyInvoice.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Invoices");
        thisWindow.setScene(new Scene(root));
    }

    public void addNew(MouseEvent mouseEvent) throws IOException {
            // Add new Item goes Here
    }

    public void delete(MouseEvent mouseEvent) throws SQLException {
//        buy item1 = itemTable.getSelectionModel().getSelectedItem();
//        Item itemCurrent = Item.getItem(item1.getItem_id());
//        assert itemCurrent != null;
//        int quantity = itemCurrent.getQuantity()-item1.getQuantity();
//        itemCurrent.setQuantity(quantity);
//        itemCurrent.update();
//        item1.delete();
//        buyController.removeItem(item1);
    }

//    static void addItem(buy item){
////        itemTable1.getItems().add(item);
//    }

//    private static void removeItem(buy item){
//        itemTable1.getItems().remove(item);
//    }

    public void addChequeDialog(MouseEvent mouseEvent) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("../resources/views/addCheque.fxml"));
        Stage model = new Stage();
        Parent root = load.load();
        model.setTitle("Add New Cheque");
        model.initModality(Modality.APPLICATION_MODAL);
        model.setScene(new Scene(root));
        model.show();
    }

    public void addShopWindow(MouseEvent mouseEvent) {
        // Open window of adding a new shop goes here
    }

    public void saveInvoice(MouseEvent mouseEvent) throws SQLException {
        String invoice = invoice_id.getText();
        String cheque = cheque_no.getSelectionModel().getSelectedItem().toString();
        int shop = Shop.getShopId(shop_id.getSelectionModel().getSelectedItem().toString());
        String date = java.sql.Date.valueOf(date_issue.getValue()).toString();
        Invoice currentInv = new Invoice(invoice,shop,date,0,cheque,1);
        currentInv.save();
        // Success Message Box shows here
    }
}
