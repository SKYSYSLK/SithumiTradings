package controllers;

import java.io.*;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.ResourceBundle;
import controllers.itemcalculated;

import java.net.URL;

import static models.t_invoiceItem.*;




public class sellController implements Initializable {
    public TableView<itemcalculated> itemTable;
    public TableColumn<itemcalculated, String> item_no;
    public TableColumn<itemcalculated, String> name;
    public TableColumn<itemcalculated, Integer> quantity;
    public TableColumn<itemcalculated, Float> Total;
    public TableColumn<itemcalculated, Float> itemProfit;
    public TableColumn<itemcalculated, Float> sellPrice;


    public JFXTextField itemId;
    public JFXTextField itemName;
    public JFXTextField itemquantity;
    public JFXTextField itemSellPrice;
    public Text income;
    public Text profit;
    public JFXButton back;
    public JFXDatePicker date;
    public JFXButton add;
    public JFXTextField currinvoice;

    public JFXTextField shopname;
    public JFXButton addinvoice;
    public AnchorPane itempane;
    public AnchorPane invoicepane;
    public JFXRadioButton cash;
    public JFXRadioButton cheque;
    public JFXComboBox cheque_no;
    public JFXComboBox shopid;
    public Text totalShow;
    private String currID;
    private ToggleGroup paymode = new ToggleGroup();
    private LocalDate today = LocalDate.now();
    private int chequefield;

    public sellController() throws SQLException {
    }

    public void getSelected(MouseEvent mouseEvent) {
    }

    public void setInvoice(String id) {
        this.currID = id;
        System.out.println(currID + "on Edit Mode");
    }

    public void setUpdate(boolean b) {
        if(b){isEdit();}
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        item_no.setCellValueFactory(new PropertyValueFactory<>("itemNo"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemProfit.setCellValueFactory(new PropertyValueFactory<>("itemProfit"));
        Total.setCellValueFactory(new PropertyValueFactory<>("Total"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        add.setDisable(true);
//        itemTable.setItems(calculatedItems);

        cash.setToggleGroup(paymode);
        cheque.setToggleGroup(paymode);


    }


//    public  ArrayList<itemcalculated> calculate() throws SQLException{
//
//        ObservableList<t_invoiceItem> itemData = FXCollections.observableArrayList(
//                t_invoiceItem.getItems(getCurrID())
//        );
//
//        ArrayList allRec = new ArrayList();
//        itemData.forEach((item) -> {
//            double Total =  item.getQuantity()*item.getSellPrice();
//            double itemProfit = Total - item.getBuyPrice()*item.getQuantity();
//            String itemName = item.getName();
//            System.out.println(item.getName());
//            int quantity = item.getQuantity();
//            double sellprice = item.getSellPrice();
//            String itemno = item.getItemNo();
//
//            allRec.add(new itemcalculated(itemno,itemName,quantity,itemProfit,Total,sellprice));
//
//        });
//
//        return allRec;
//
//    }
//
//
//    private  ObservableList<itemcalculated> itemcalculated = FXCollections.observableArrayList(
//            this.calculate()
//    );


    private ObservableList<itemcalculated> calculatedItems = FXCollections.observableArrayList(
            itemcalculated.getItems("0000")
    );


    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage) itemTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/sellInvoice.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Invoices");
        thisWindow.setScene(new Scene(root));
    }

    public void getName(MouseEvent mouseEvent) throws SQLException, IOException {
        Item current = Item.getItem(itemId.getText());
        if (current != null) {
            itemName.setText(current.getName());
            itemSellPrice.setText(Float.toString(current.getSellPrice()));
            this.add.setDisable(false);
        } else {
            itemName.setText("Item could not be found");
            FXMLLoader load = new FXMLLoader(getClass().getResource("../resources/views/alert/idErr.fxml"));
            Stage model = new Stage();
            Parent root = load.load();
            model.setTitle("Error");
            model.initModality(Modality.APPLICATION_MODAL);
            model.setScene(new Scene(root));
            model.show();
            return;
        }
    }

    public void addItem(MouseEvent mouseEvent) throws SQLException {
        calculatedItems.removeAll(calculatedItems);
        String itemNo = itemId.getText();
        String invoiceId = this.currinvoice.getText();
        String name = itemName.getText();
// Implement Try Catch Error
        int qty = Integer.parseInt(itemquantity.getText());
        double sellPrice = Double.parseDouble(itemSellPrice.getText());
        double buyPrice = this.getBuy();

        System.out.println(invoiceId);
        InvoiceItem newItem = new InvoiceItem(itemNo, invoiceId, buyPrice, sellPrice, qty);
        newItem.save();
        calculatedItems = FXCollections.observableArrayList(itemcalculated.getItems(invoiceId));
        itemTable.setItems(calculatedItems);
        clearinput();
        this.add.setDisable(true);
        Invoice.addAmount(invoiceId,sellPrice*qty);
        getTotal();



    }

    private void clearinput() {
        itemId.clear();
        itemName.clear();
        itemquantity.clear();
        itemSellPrice.clear();
    }

    public void getToday() {

    }


    private float getBuy() throws SQLException {
        Item current = Item.getItem(itemId.getText());
        return current.getBuyPrice();

    }

    public void addInvoice(MouseEvent mouseEvent) throws SQLException, ParseException, IOException {

        if (this.currinvoice.getText() == null || this.date.getValue() == null || this.shopid.getValue() == null) {
            FXMLLoader load = new FXMLLoader(getClass().getResource("../resources/views/alert/saveFail.fxml"));
            Stage model = new Stage();
            Parent root = load.load();
            model.setTitle("Error");
            model.initModality(Modality.APPLICATION_MODAL);
            model.setScene(new Scene(root));
            model.show();
        } else {
            String invoiceId = this.currinvoice.getText();
            String date = this.date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            double Total = 0;
            String cheque;
            if (!cash.isSelected()) {
                cheque = cheque_no.getValue().toString();
            } else {
                cheque = "CASH";
            }

            int type = 2;
            int shopId = Shop.getShopId(shopid.getValue().toString());

            Invoice newInvoice = new Invoice(invoiceId, shopId, date, Total, cheque, type);
            newInvoice.save();
            System.out.println(newInvoice.getId() + " Written");
            this.itempane.setDisable(false);
            this.invoicepane.setDisable(true);
        }

    }

    public void paymode() throws SQLException {
//        System.out.println(this.paymode.getSelectedToggle().getUserData());
        if (cash.isSelected()) {
            System.out.println("paymode is cash");
            emptyCombo();

        } else {
            System.out.println("Paymode is Cheque");
            fillChequeCombo();
        }
    }


    public void addChequeDialog(MouseEvent mouseEvent) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("../resources/views/addCheque.fxml"));
        Stage model = new Stage();
        Parent root = load.load();
        model.setTitle("Add New Cheque");
        model.initModality(Modality.APPLICATION_MODAL);
        model.setScene(new Scene(root));
        model.show();
    }

    private void fillChequeCombo() throws SQLException {
        ArrayList<Cheque> allCheques = Cheque.getAll();
        for (Cheque cheque : allCheques) {
            cheque_no.getItems().add(cheque.getId());
        }
    }

    private void emptyCombo() throws SQLException {
        ArrayList<Cheque> allCheques = Cheque.getAll();
        for (Cheque cheque : allCheques) {
            cheque_no.getItems().removeAll(cheque.getId());
        }
    }

    public void fillShopCombo() throws SQLException {
        ArrayList<Shop> allShops = Shop.getAll();
        for (Shop shop : allShops) {
            shopid.getItems().add(shop.getName());
        }

    }



    private double getTotal() {
        double sumOfItem = 0;
        double profitofItem =0;
        for(itemcalculated tableItem:itemTable.getItems()){
            sumOfItem+=tableItem.getTotal();
            profitofItem+= tableItem.getItemProfit();
        }
        income.setText("RS "+Double.toString(sumOfItem)+" /=");
        profit.setText("RS "+Double.toString(profitofItem)+" /=");
        return sumOfItem;
    }


    private void reduceStock(){

    }

//Edit methods here

    public void isEdit(){
        System.out.println(this.currID);
        this.currinvoice.setText(this.currID);
        this.itempane.setDisable(false);
        this.add.setText("Update");
        this.addinvoice.setText("Update");

        itemTable.setItems(calculatedItems);
    }


}

