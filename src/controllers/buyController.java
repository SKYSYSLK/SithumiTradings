package controllers;

import com.jfoenix.controls.JFXButton;
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
import models.Item;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author danushka
 */
public class buyController implements Initializable {
//    public TableColumn<buy, String> shop_no;
//    public TableColumn<buy, String> name;
//    public TableColumn<buy, Integer> type;
//    public TableColumn<buy, Float> contact;
//    public TableColumn<buy, Float> address;
//    public TableColumn<buy, String> date;
    public JFXButton back;
//    public TableView<buy> shopTable;
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

    public buyController() throws SQLException {
    }

    public void getSelected(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        shop_no.setCellValueFactory(new PropertyValueFactory<>("id"));
//        name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        type.setCellValueFactory(new PropertyValueFactory<>("type"));
//        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
//        address.setCellValueFactory(new PropertyValueFactory<>("address"));

//        itemTable1 = shopTable;
    }
//    private ObservableList<buy> itemData = FXCollections.observableArrayList(
////           buy.getAll()
//    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
//        Stage thisWindow = (Stage)shopTable.getScene().getWindow();
//        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/buyInvoice.fxml"));
//        Parent root = backLoader.load();
//        thisWindow.setTitle("Main Menu ");
//        thisWindow.setScene(new Scene(root));
    }

    public void addNew(MouseEvent mouseEvent) throws IOException {
            // Add new Item goes Here
    }

    public void delete(MouseEvent mouseEvent) throws SQLException {
//        buy item1 = shopTable.getSelectionModel().getSelectedItem();
//        Item itemCurrent = Item.getItem(item1.getItem_id());
//        assert itemCurrent != null;
//        int type = itemCurrent.getQuantity()-item1.getQuantity();
//        itemCurrent.setQuantity(type);
//        itemCurrent.update();
//        item1.delete();
//        buyController.removeItem(item1);
    }

//    static void addShop(buy item){
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
    }
}
