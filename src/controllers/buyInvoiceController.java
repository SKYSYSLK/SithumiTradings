package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Item;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class buyInvoiceController implements Initializable {

    public TableColumn<Item, String> invoice_id;
    public TableColumn<Item, String> shopName;
    public TableColumn<Item, Float> amount;
    public TableColumn<Item, String> dateIssue;
    public TableColumn<Item, String> checkNo;
    public JFXButton back;
    public TableView<Item> invoiceTable;
    private static TableView<Item> itemTable1;

    public buyInvoiceController() throws SQLException {
    }

    public void getSelected(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        item_no.setCellValueFactory(new PropertyValueFactory<>("item_id"));
//        name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//        buyPrice.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
//        sellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
//        Total.setCellValueFactory(new PropertyValueFactory<>("total"));
//        paymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
//        checkno.setCellValueFactory(new PropertyValueFactory<>("check_no"));
//        date.setCellValueFactory(new PropertyValueFactory<>("day"));
        //itemTable.setItems(itemData);
        itemTable1 = invoiceTable;
    }
    private ObservableList<Item> itemData = FXCollections.observableArrayList(
            //buy.getAll()
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)invoiceTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu ");
        thisWindow.setScene(new Scene(root));
    }

    public void addNew(MouseEvent mouseEvent) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("../resources/views/Buy.fxml"));
        Stage model = (Stage)back.getScene().getWindow();
        Parent root = load.load();
        model.setTitle("Add New Record");
        //model.initModality(Modality.APPLICATION_MODAL);
        model.setScene(new Scene(root));
        model.show();
    }

    public void delete(MouseEvent mouseEvent) throws SQLException {
//        buy item1 = invoiceTable.getSelectionModel().getSelectedItem();
//        Item itemCurrent = Item.getItem(item1.getItem_id());
//        assert itemCurrent != null;
//        int quantity = itemCurrent.getQuantity()-item1.getQuantity();
//        itemCurrent.setQuantity(quantity);
//        itemCurrent.update();
//        item1.delete();
//        buyInvoiceController.removeItem(item1);
    }

//    static void addItem(buy item){
//        itemTable1.getItems().add(item);
//    }
//
//    private static void removeItem(buy item){
//        itemTable1.getItems().remove(item);
//    }
}
