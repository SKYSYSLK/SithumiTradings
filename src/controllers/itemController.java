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
import java.sql.SQLException;
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

    public itemController() throws SQLException {
    }


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
            item.getAll()
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)itemTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu");
        thisWindow.setScene(new Scene(root));
    }

    public void updateItem(MouseEvent mouseEvent) throws IOException, SQLException {
        String itemId = u_itemId.getText();
        String itemName = u_name.getText();
        if(itemId.equals("")||itemName.equals("")||u_quantity.getText().equals("")||u_buyPrice.getText().equals("")||u_sellPrice.getText().equals("")) {
            warning.incomplete();
            return;
        }
        int quantity = Integer.parseInt(u_quantity.getText());
        float buyPrice = Float.parseFloat(u_buyPrice.getText());
        float sellPrice = Float.parseFloat(u_sellPrice.getText());

        item newItem = new item(itemId,itemName,quantity,buyPrice,sellPrice);
        newItem.update();

        //Refresh Table
        Stage thiswind = (Stage) itemTable.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("../resources/views/items.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Items in your stock");
        thiswind.setScene(new Scene(root));
        thiswind.show();
    }

    public void addItem(MouseEvent mouseEvent) throws SQLException, IOException {
        String itemId = a_itemId.getText();
        String itemName = a_name.getText();
        if(itemId.equals("")||itemName.equals("")||a_quantity.getText().equals("")||buyPrice.getText().equals("")||sellPrice.getText().equals("")) {
            warning.incomplete();
            return;
        }
        int quantity = Integer.parseInt(a_quantity.getText());
        float buyPrice = Float.parseFloat(a_buyPrice.getText());
        float sellPrice = Float.parseFloat(a_sellPrice.getText());

        item newItem = new item(itemId,itemName,quantity,buyPrice,sellPrice);
        newItem.save();

        //Refresh Table
        itemTable.getItems().add(newItem);
        a_itemId.clear();
        a_name.clear();
        a_quantity.clear();
        a_sellPrice.clear();
        a_buyPrice.clear();
    }

    public void getSelected(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()>1){
            onEdit();
        }
    }
    private void onEdit(){
        if(itemTable.getSelectionModel().getSelectedItem()!=null){
            item current = itemTable.getSelectionModel().getSelectedItem();
            u_itemId.setText(current.getId());
            u_name.setText(current.getName());
            u_quantity.setText(Integer.toString(current.getQuantity()));
            u_buyPrice.setText(Float.toString(current.getBuyPrice()));
            u_sellPrice.setText(Float.toString(current.getSellPrice()));
        }
    }
}
