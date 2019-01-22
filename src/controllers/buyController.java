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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.buy;
import models.item;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author danushka
 */
public class buyController implements Initializable {
    public TableColumn<buy, String> item_no;
    public TableColumn<buy, String> name;
    public TableColumn<buy, Integer> quantity;
    public TableColumn<buy, Float> buyPrice;
    public TableColumn<buy, Float> sellPrice;
    public TableColumn<buy, Float> Total;
    public TableColumn<buy, String> paymentDate;
    public TableColumn<buy, String> checkno;
    public TableColumn<buy, String> date;
    public JFXButton back;
    public TableView<buy> itemTable;
    private static TableView<buy> itemTable1;

    public buyController() throws SQLException {
    }

    public void getSelected(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        item_no.setCellValueFactory(new PropertyValueFactory<>("item_id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        buyPrice.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        Total.setCellValueFactory(new PropertyValueFactory<>("total"));
        paymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        checkno.setCellValueFactory(new PropertyValueFactory<>("check_no"));
        date.setCellValueFactory(new PropertyValueFactory<>("day"));
        itemTable.setItems(itemData);
        itemTable1 = itemTable;
    }
    private ObservableList<buy> itemData = FXCollections.observableArrayList(
           buy.getAll()
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)itemTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu ");
        thisWindow.setScene(new Scene(root));
    }

    public void addNew(MouseEvent mouseEvent) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("../resources/views/addBuyItem.fxml"));
        Stage model = new Stage();
        Parent root = load.load();
        model.setTitle("Add New Record");
        model.initModality(Modality.APPLICATION_MODAL);
        model.setScene(new Scene(root));
        model.show();
    }

    public void delete(MouseEvent mouseEvent) throws SQLException {
        buy item1 = itemTable.getSelectionModel().getSelectedItem();
        item itemCurrent = item.getItem(item1.getItem_id());
        assert itemCurrent != null;
        int quantity = itemCurrent.getQuantity()-item1.getQuantity();
        itemCurrent.setQuantity(quantity);
        itemCurrent.update();
        item1.delete();
        buyController.removeItem(item1);
    }

    static void addItem(buy item){
        itemTable1.getItems().add(item);
    }

    private static void removeItem(buy item){
        itemTable1.getItems().remove(item);
    }
}
