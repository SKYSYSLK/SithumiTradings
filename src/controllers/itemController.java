package controllers;

import com.jfoenix.controls.JFXButton;
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
import javafx.stage.Stage;
import models.item;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author danushka
 */
public class itemController implements Initializable {

    public TableView<item> itemTable;

    public TableColumn<item,String> item_no;
    public TableColumn<item,Integer> quantity;
    public TableColumn<item,Float> buyPrice;
    public TableColumn<item,Float> sellPrice;
    public TableColumn<item, String> name;
    public JFXTextField u_itemId;
    public JFXTextField u_name;
    public JFXTextField u_quantity;
    public JFXTextField u_buyPrice;
    public JFXTextField u_sellPrice;
    public JFXButton update;
    public JFXTextField a_itemId;
    public JFXTextField a_name;
    public JFXTextField a_quantity;
    public JFXTextField a_buyPrice;
    public JFXTextField a_sellPrice;
    public JFXButton add;
    public JFXButton back;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        item_no.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        buyPrice.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        itemTable.setItems(itemData);
    }
    private ObservableList<item> itemData = FXCollections.observableArrayList(
            new item("Ab15","myItem1",150,150,250)
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)itemTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu");
        thisWindow.setScene(new Scene(root));
    }
}
