package controllers;

import java.io.*;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
    public JFXTextField invoiceid;
    public JFXTextField shopid;
    public JFXTextField shopname;
    public JFXButton addinvoice;
    public AnchorPane itempane;
    public String thisInvoice;
    private LocalDate today = LocalDate.now();

    public sellController() throws SQLException {
    }

    public void getSelected(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        item_no.setCellValueFactory(new PropertyValueFactory<>("itemNo"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemProfit.setCellValueFactory(new PropertyValueFactory<>("itemProfit"));
        Total.setCellValueFactory(new PropertyValueFactory<>("Total"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        thisInvoice= thisInvoice();
        itemTable.setItems(itemcalculated);




    }

    public String thisInvoice(){
        return this.invoiceid.getText();
    }


    private static ArrayList<itemcalculated> calculate() throws SQLException{
//        System.out.println(thisInvoice);

        ObservableList<t_invoiceItem> itemData = FXCollections.observableArrayList(
                t_invoiceItem.getItems("2323")
        );
        ArrayList allRec = new ArrayList();
        itemData.forEach((item) -> {
            double Total =  item.getQuantity()*item.getSellPrice();
            double itemProfit = Total - item.getBuyPrice()*item.getQuantity();
            String itemName = item.getName();
            System.out.println(item.getName());
            int quantity = item.getQuantity();
            double sellprice = item.getSellPrice();
            String itemno = item.getItemNo();

            allRec.add(new itemcalculated(itemno,itemName,quantity,itemProfit,Total,sellprice));

        });

        return allRec;

    }


    private  ObservableList<itemcalculated> itemcalculated = FXCollections.observableArrayList(
            this.calculate()
    );



//    private void populate() throws  SQLException{
//        itemTable.setItems(FXCollections.observableArrayList(t_invoiceItem.getItems("0000")));
//    }



    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)itemTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/sellInvoice.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Invoices");
        thisWindow.setScene(new Scene(root));
    }

    public void getName(MouseEvent mouseEvent) throws SQLException {
        Item current = Item.getItem(itemId.getText());
        if(current!=null) {
            itemName.setText(current.getName());
            itemSellPrice.setText(Float.toString(current.getSellPrice()));
        }
        else itemName.setText("Item could not be found");
    }

    public void addItem(MouseEvent mouseEvent) throws SQLException{
        String itemNo = itemId.getText();
        String invoiceId = this.invoiceid.getText();
        String name = itemName.getText();
// Implement Try Catch Error
        int qty = Integer.parseInt(itemquantity.getText());
        double sellPrice = Double.parseDouble(itemSellPrice.getText());
        double buyPrice = this.getBuy();

        System.out.println(invoiceId);
        InvoiceItem newItem = new InvoiceItem(itemNo,invoiceId,buyPrice,sellPrice,qty);
        newItem.save();
        itemcalculated.removeAll(itemcalculated);
        itemcalculated = FXCollections.observableArrayList(calculate());
        itemTable.setItems(itemcalculated);

    }

    public void getToday(){

    }


    private float getBuy() throws SQLException{
        Item current = Item.getItem(itemId.getText());
        return current.getBuyPrice();

    }

    public void addInvoice(MouseEvent mouseEvent) throws SQLException, ParseException, IOException {

        if(this.invoiceid.getText()==null || this.date.getValue() == null  ||this.shopid.getText() == null){
            FXMLLoader load = new FXMLLoader(getClass().getResource("../resources/views/alert/saveFail.fxml"));
            Stage model = new Stage();
            Parent root = load.load();
            model.setTitle("Error");
            model.initModality(Modality.APPLICATION_MODAL);
            model.setScene(new Scene(root));
            model.show();
        }
        else{
            String invoiceId = this.invoiceid.getText();
            String date = this.date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            double Total = 0;
            String cheque = "Cash";
            int type = 2;
            int shopId = Integer.parseInt(this.shopid.getText());

            Invoice newInvoice = new Invoice(invoiceId,shopId,date,Total,cheque,type);
            newInvoice.save();
            System.out.println(newInvoice.getId()+" Written");
            this.itempane.setDisable(false);
        }

    }



}
