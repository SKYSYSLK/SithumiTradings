package controllers;

import java.io.*;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    //    public JFXTextField shopname;
    public JFXButton addinvoice;
    public AnchorPane itempane;
    public AnchorPane invoicepane;
    public JFXRadioButton cash;
    public JFXRadioButton cheque;
    public JFXRadioButton due;
    public JFXComboBox cheque_no;
    public JFXTextField shopid;
    public Text totalShow;
    public JFXButton btnAddItem;
    private String currID;
    private ToggleGroup paymode = new ToggleGroup();
    private LocalDate today = LocalDate.now();
    private int chequefield;
    private boolean isEdit=false;
    public sellController() throws SQLException {
    }

//    public void getSelected(MouseEvent mouseEvent) {
//    }

    public void setInvoice(String id) {
        this.currID = id;
        System.out.println(currID + "on Edit Mode");
    }

    public void setUpdate(boolean b) throws SQLException {
        this.isEdit =b;
        if(b){
            isEdit();
        }
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
        due.setToggleGroup(paymode);

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction((ActionEvent event)->{
            itemcalculated currentClicked = itemTable.getSelectionModel().getSelectedItem();
            try {
                currentClicked.delete(this.currinvoice.getText().toString());
                itemTable.getItems().remove(currentClicked);
                getTotal();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        ContextMenu rightClick = new ContextMenu();
        rightClick.getItems().add(delete);
        itemTable.setContextMenu(rightClick);

        // Set Autofill Text for Items

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.setSelectionHandler(event -> itemId.setText(event.getObject()));
        try {
            autoCompletePopup.getSuggestions().addAll(Item.getItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        itemId.textProperty().addListener(observable -> {
            autoCompletePopup.filter(s -> s.contains(itemId.getText()));
            autoCompletePopup.hide();
            if(!autoCompletePopup.getFilteredSuggestions().isEmpty()){
                autoCompletePopup.show(itemId);
            }else{
                autoCompletePopup.hide();
            }
        });

        // End autofill Item Textbox

        //Start Autofill Shops
        JFXAutoCompletePopup<String> autoCompleteShop = new JFXAutoCompletePopup<>();
        autoCompleteShop.setSelectionHandler(event -> shopid.setText(event.getObject()));
        try {
            autoCompleteShop.getSuggestions().addAll(Shop.getShopNames());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        shopid.textProperty().addListener(observable -> {
            autoCompleteShop.filter(s -> s.contains(shopid.getText()));
            autoCompleteShop.hide();
            if(!autoCompleteShop.getFilteredSuggestions().isEmpty()){
                autoCompleteShop.show(shopid);
            }else{
                autoCompleteShop.hide();
            }
        });
        // End auto fill shops
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
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("/resources/views/sellInvoice.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Invoices");
        thisWindow.setScene(new Scene(root));
    }

    public void getName(MouseEvent mouseEvent) throws SQLException, IOException {
        Item current = Item.getItem(Item.getItemId(itemId.getText()));
        if (current != null) {
            itemName.setText(current.getId());
            itemSellPrice.setText(Float.toString(current.getSellPrice()));
            this.add.setDisable(false);
        } else {
            itemName.setText("Item could not be found");
            FXMLLoader load = new FXMLLoader(getClass().getResource("/resources/views/alert/idErr.fxml"));
            Stage model = new Stage();
            Parent root = load.load();
            model.setTitle("Error");
            model.initModality(Modality.APPLICATION_MODAL);
            model.setScene(new Scene(root));
            model.show();
            return;
        }
    }

    public void addItem(MouseEvent mouseEvent) throws SQLException, IOException, ParseException {
        try{
            if (isEdit){
//            this.editRecord(mouseEvent);
                itemcalculated currentSelected = itemTable.getSelectionModel().getSelectedItem();
                int prevAmount = currentSelected.getQuantity();
                double sellUpdate=Double.parseDouble(this.itemSellPrice.getText());
                int sellQuantity = Integer.parseInt(this.itemquantity.getText());

                changeStock(currentSelected.getItemNo(),prevAmount-sellQuantity);

                InvoiceItem updateitem = new InvoiceItem(currentSelected.getItemNo(),currID,currentSelected.getBuyPrice(),sellUpdate,sellQuantity);
                updateitem.update();

                calculatedItems = FXCollections.observableArrayList(itemcalculated.getItems(currID));
                itemTable.setItems(calculatedItems);
                getTotal();
            }
            else{
                calculatedItems.removeAll(calculatedItems);
                String itemNo = Item.getItemId(itemId.getText());
                String invoiceId = this.currinvoice.getText();
                //String name = itemName.getText();

                int qty = Integer.parseInt(itemquantity.getText());
                double sellPrice = Double.parseDouble(itemSellPrice.getText());
                double buyPrice = this.getBuy();

                changeStock(itemNo,-qty);

                //System.out.println(invoiceId);
                InvoiceItem newItem = new InvoiceItem(itemNo, invoiceId, buyPrice, sellPrice, qty);
                newItem.save();
                calculatedItems = FXCollections.observableArrayList(itemcalculated.getItems(invoiceId));
                itemTable.setItems(calculatedItems);
                clearinput();
                this.add.setDisable(true);
                Invoice.addAmount(invoiceId,sellPrice*qty);
                getTotal();
            }
        }catch(Exception e){
            System.out.println(e);
        }
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
        Item current = Item.getItem(Item.getItemId(itemId.getText()));
        return current.getBuyPrice();
    }

    public void addInvoice(MouseEvent mouseEvent) throws SQLException, ParseException, IOException {
        if(!isEdit){
            if (this.currinvoice.getText() == null || this.date.getValue() == null || this.shopid.getText().isEmpty()) {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/resources/views/alert/saveFail.fxml"));
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
                String chequeNo;
                if (cheque.isSelected()) {
                    chequeNo = cheque_no.getValue().toString();
                } else if(cash.isSelected()){
                    chequeNo = "CASH";
                }else{
                    chequeNo = "DUE";
                }

                int type = 2;
                int shopId = Shop.getShopId(shopid.getText().toString());

                Invoice newInvoice = new Invoice(invoiceId, shopId, date, Total, chequeNo, type);
                newInvoice.save();
                System.out.println(newInvoice.getId() + " Written");
                this.itempane.setDisable(false);
                this.invoicepane.setDisable(true);
            }


        }
        else{
            if (this.currinvoice.getText() == null || this.date.getValue() == null || this.shopid.getText().isEmpty()) {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/resources/views/alert/saveFail.fxml"));
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
                String chequeNo;
                if (cheque.isSelected()) {
                    chequeNo = cheque_no.getValue().toString();
                } else if(cash.isSelected()) {
                    chequeNo = "CASH";
                } else{
                    chequeNo = "DUE";
                }

                int type = 2;
                int shopId = Shop.getShopId(shopid.getText().toString());

                Invoice newInvoice = new Invoice(invoiceId, shopId, date, Total, chequeNo, type);
                newInvoice.update();
                backMenu(mouseEvent);
                calculatedItems = FXCollections.observableArrayList(itemcalculated.getItems(currID));
                itemTable.setItems(calculatedItems);
                getTotal();


            }


        }}

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
        FXMLLoader load = new FXMLLoader(getClass().getResource("/resources/views/addCheque.fxml"));
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

//    public void fillShopCombo() throws SQLException {
//        ArrayList<Shop> allShops = Shop.getAll();
//        for (Shop shop : allShops) {
//            if(shop.getType()==1) shopid.getItems().add(shop.getName());
//        }
//
//    }



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


    private void changeStock(String id,int amount) throws SQLException{
        int currStock = Item.getItem(id).getQuantity();
        System.out.println("Changing Stocks by"+amount);
        Item currItem = new Item(id,"dummy",currStock,0,0);
        currItem.addAmount(amount);
    }

//Edit methods here

    public void isEdit() throws SQLException{
//        System.out.println(this.currID);
        Invoice currentInvoice = Invoice.getInvoice(this.currID);
        this.currinvoice.setText(this.currID);
        this.itempane.setDisable(false);
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date.setValue(LocalDate.parse(currentInvoice.getDate_issue(),fomatter));
//        System.out.println("Shop Name: "+Shop.getShopName(currentInvoice.getShop_id()));
        //shopid.getSelectionModel().select("ShopName");
        shopid.setText(Shop.getShopName(currentInvoice.getShop_id()));
        if(currentInvoice.getCheque_id().equals("DUE")) due.setSelected(true);
        else if(currentInvoice.getCheque_id().equals("CASH")) cash.setSelected(true);
        else cheque.setSelected(true);


        btnAddItem.setVisible(true);
        this.add.setText("Update");
        this.addinvoice.setText("Update");
        calculatedItems = FXCollections.observableArrayList(itemcalculated.getItems(currID));
//        itemTable.setItems(calculatedItems);
        itemTable.setItems(calculatedItems);
        getTotal();
    }



    public void editRecord(MouseEvent mouseEvent) throws SQLException, IOException{
        itemcalculated currentSelected = itemTable.getSelectionModel().getSelectedItem();
        double sellUpdate=Double.parseDouble(this.itemSellPrice.getText());
        int sellQuantity = Integer.parseInt(this.itemquantity.getText());
        InvoiceItem updateitem = new InvoiceItem(currentSelected.getItemNo(),currID,currentSelected.getBuyPrice(),sellUpdate,sellQuantity);
        updateitem.update();
    }

    public void getSelected(MouseEvent mouseEvent) throws SQLException, ParseException, IOException {
        if(itemTable.getSelectionModel().getSelectedItem()==null){
            System.out.println("None Selected");
        }
        else {
            if(mouseEvent.getClickCount()>1) {
                itemcalculated currentSelected = itemTable.getSelectionModel().getSelectedItem();
                System.out.println("Editing" + currentSelected.getName());
//            getName(mouseEvent);
                this.itemId.setText(currentSelected.getName());
                this.itemquantity.setText(Integer.toString(currentSelected.getQuantity()));
                this.itemSellPrice.setText(Double.toString(currentSelected.getSellPrice()));
                this.currinvoice.setText(currID);
            }

        }
    }

    public void addNewItem(MouseEvent mouseEvent) throws SQLException {
        if(!currinvoice.getText().isEmpty()){
            //calculatedItems.removeAll(calculatedItems);
            String invoice_id = currinvoice.getText().toString();
            String item_id = Item.getItemId(itemId.getText().toString());
            //String name = itemName.getText().toString();
            int quantity = Integer.parseInt(itemquantity.getText().toString());
            double sellPrice = Double.parseDouble(itemSellPrice.getText());
            double buyPrice = this.getBuy();

            InvoiceItem newItem = new InvoiceItem(item_id, invoice_id, buyPrice, sellPrice, quantity);
            changeStock(item_id,-quantity);
            newItem.save();
            calculatedItems = FXCollections.observableArrayList(itemcalculated.getItems(invoice_id));
            itemTable.setItems(calculatedItems);
            clearinput();
            Invoice.addAmount(invoice_id,sellPrice*quantity);
            getTotal();
        }
    }
}