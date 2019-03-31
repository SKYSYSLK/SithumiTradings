package controllers;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class editBuyController implements Initializable {
    public TableView<t_invoiceItem> invoiceItemTable;
    public TableColumn<t_invoiceItem, String> item_no;
    public TableColumn<t_invoiceItem, String> name;
    public TableColumn<t_invoiceItem, Integer> quantity;
    public TableColumn<t_invoiceItem, Float> buyPrice;
    public TableColumn<t_invoiceItem, Float> sellPrice;
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
    public JFXButton addItem;
    public JFXTextField itemName;
    public JFXTextField itemQuantity;
    public JFXTextField itemBuyPrice;
    public JFXTextField itemSellPrice;
    public Text error2;
    public Text error3;
    @FXML
    private Text total;

    private t_invoice invoice;
    private final buyInvoiceController InvoicesWindow;
    private Stage currentStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        item_no.setCellValueFactory(new PropertyValueFactory<>("itemNo"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        buyPrice.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        try {
            fillShopCombo();
            fillChequeCombo();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.setSelectionHandler(event -> itemId.setText(event.getObject()));
        try {
            autoCompletePopup.getSuggestions().addAll(Item.getItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Right Click Menu
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction((ActionEvent event)->{
            t_invoiceItem currentClicked = invoiceItemTable.getSelectionModel().getSelectedItem();
            try {
                currentClicked.delete();
                invoiceItemTable.getItems().remove(currentClicked);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        ContextMenu rightClick = new ContextMenu();
        rightClick.getItems().add(delete);
        invoiceItemTable.setContextMenu(rightClick);

        // Auto Complete Edit Text
        itemId.textProperty().addListener(observable -> {
            autoCompletePopup.filter(s -> s.contains(itemId.getText()));
            autoCompletePopup.hide();
            if(!autoCompletePopup.getFilteredSuggestions().isEmpty()){
                autoCompletePopup.show(itemId);
            }else{
                autoCompletePopup.hide();
            }
        });

    }

    public editBuyController(buyInvoiceController invoiceController) throws SQLException, ParseException {
        this.InvoicesWindow = invoiceController;
        currentStage = (Stage) invoiceController.invoiceTable.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/editInvoiceBuy.fxml"));
        loader.setController(this);
        try {
            currentStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.invoice = invoiceController.invoiceTable.getSelectionModel().getSelectedItem();
        // Set table Data
        invoiceItemTable.setItems(FXCollections.observableArrayList(t_invoiceItem.getItems(this.invoice.getId())));
        System.out.println(this.invoice==null);
        fillTextData(this.invoice);

        //Set total bill
        setTotalBill();

        back.setOnMouseClicked(event -> {
            try {
                this.backMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        invoiceItemTable.setOnMouseClicked(event -> {
            if(event.getClickCount()>1){
                fetchItemData();
            }
        });

        updateItem.setOnMouseClicked(event -> {
            String item_id = null;
            try {
                item_id = Item.getItemId(itemId.getText());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String item_name = itemId.getText();
            String invoiceId = invoice_id.getText();
            int item_quantity = Integer.parseInt(itemQuantity.getText());
            double sell_price = Double.parseDouble(itemSellPrice.getText());
            double buy_price = Double.parseDouble(itemBuyPrice.getText());
            t_invoiceItem currentItem = new t_invoiceItem(item_id,invoiceId,item_name,item_quantity,sell_price,buy_price);
            try {
                currentItem.update();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            invoiceItemTable.getItems().remove(invoiceItemTable.getSelectionModel().getSelectedItem());
            invoiceItemTable.getItems().add(currentItem);
            setTotalBill();
            itemId.clear();
            itemName.clear();
            itemQuantity.clear();
            itemBuyPrice.clear();
            itemSellPrice.clear();
        });

        addItem.setOnMouseClicked(event -> {
            String item_id = null;
            try {
                item_id = Item.getItemId(itemId.getText());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String item_name = itemId.getText();
            String invoiceId = invoice_id.getText();
            int item_quantity = Integer.parseInt(itemQuantity.getText());
            double sell_price = Double.parseDouble(itemSellPrice.getText());
            double buy_price = Double.parseDouble(itemBuyPrice.getText());
            t_invoiceItem currentItem = new t_invoiceItem(item_id,invoiceId,item_name,item_quantity,sell_price,buy_price);
            try {
                currentItem.save();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            itemId.clear();
            itemName.clear();
            itemQuantity.clear();
            itemBuyPrice.clear();
            itemSellPrice.clear();
            invoiceItemTable.getItems().add(currentItem);
            setTotalBill();
            double unitPrice = buy_price*item_quantity;
            try {
                Invoice.addAmount(invoiceId,unitPrice);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Item enteredItem = null;
            try {
                enteredItem = Item.getItem(item_id);
                int currentQuantity = enteredItem.getQuantity();
                int newQuantity = currentQuantity+item_quantity;
                enteredItem.setQuantity(newQuantity);
                enteredItem.update();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        itemName.setOnMouseClicked(event -> {
            try {
                Item item = Item.getItem(Item.getItemId(itemId.getText().toString()));
                itemName.setText(item.getId());
                itemSellPrice.setText(Float.toString(item.getSellPrice()));
                itemBuyPrice.setText(Float.toString(item.getBuyPrice()));
                //itemQuantity.setText(Integer.toString(item.getQuantity()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void setTotalBill() {
        double sumOfItem = 0;
        for(t_invoiceItem tableItem:invoiceItemTable.getItems()){
            sumOfItem+=tableItem.getBuyPrice()*tableItem.getQuantity();
        }
        total.setText("RS "+Double.toString(sumOfItem)+" /=");
    }

    private void fetchItemData() {
        t_invoiceItem currentSelected = invoiceItemTable.getSelectionModel().getSelectedItem();
        itemId.setText(currentSelected.getName());
        itemName.setText(currentSelected.getItemNo());
        itemQuantity.setText(Integer.toString(currentSelected.getQuantity()));
        itemBuyPrice.setText(Double.toString(currentSelected.getBuyPrice()));
        itemSellPrice.setText(Double.toString(currentSelected.getSellPrice()));
    }

    private void fillTextData(t_invoice invoice) throws ParseException {
        invoice_id.setText(invoice.getId());
        cheque_no.getSelectionModel().select(invoice.getCheque_number());
        shop_id.getSelectionModel().select(invoice.getShopName());
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date_issue.setValue(LocalDate.parse(invoice.getDateIsssued(),fomatter));
    }

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

    public void showStage() {
        //currentStage.showAndWait();
    }

    public void backMenu() throws IOException {
        Stage thisWindow = (Stage)invoiceItemTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("/resources/views/buyInvoice.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Invoices");
        thisWindow.setScene(new Scene(root));
    }

    public void getSelected(MouseEvent mouseEvent) {
    }



}
