package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Shop;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author danushka
 */
public class shopController implements Initializable {

    public TableView<Shop> shopTable;

    public TableColumn<Shop,String> shop_no;
    public TableColumn<Shop,Integer> type;
    public TableColumn<Shop,Float> contact;
    public TableColumn<Shop,Float> address;
    public TableColumn<Shop, String> name;
    public JFXTextField u_shopId;
    public JFXTextField u_name;
    public JFXComboBox u_type;
    public JFXTextField u_contact;
    public JFXTextField u_address;
    public JFXButton update;
    public JFXButton deleteShop;
    public JFXButton add;
    public JFXButton back;

    public shopController() throws SQLException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shop_no.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        shopTable.setItems(shopData);
        fillShopTypes();
    }

    private void fillShopTypes() {
        u_type.getItems().add("sell");
        u_type.getItems().add("buy");
    }

    private ObservableList<Shop> shopData = FXCollections.observableArrayList(
            Shop.getAll()
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage) shopTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("/resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu");
        thisWindow.setScene(new Scene(root));
    }

    public void updateShop(MouseEvent mouseEvent) throws IOException, SQLException {
        String shopName = u_name.getText();
        if(u_shopId.getText().equals("")||shopName.equals("")|| u_type.getSelectionModel().getSelectedItem().toString().equals("")|| u_contact.getText().equals("")|| u_address.getText().equals("")) {
            warning.incomplete();
            return;
        }

        int shopId = Integer.parseInt(u_shopId.getText());
        int type = getShopType(u_type.getSelectionModel().getSelectedItem().toString());
        String contact = u_contact.getText();
        String address = u_address.getText();

        Shop newShop = new Shop(shopId,type,shopName,contact,address);
        newShop.update();

        //Refresh Table
        Stage thiswind = (Stage) shopTable.getScene().getWindow();
        FXMLLoader shopsView = new FXMLLoader(getClass().getResource("/resources/views/shops.fxml"));
        Parent root = (Parent) shopsView.load();
        thiswind.setTitle("Manage Shops related to your business");
        thiswind.setScene(new Scene(root));
        thiswind.show();
        warning.updateSuccess();
    }

    public void deleteShop(MouseEvent mouseEvent) throws IOException, SQLException {
        String shopName = u_name.getText();
        if(u_shopId.getText().equals("")||shopName.equals("")|| u_type.getSelectionModel().getSelectedItem().toString().equals("")|| u_contact.getText().equals("")|| u_address.getText().equals("")) {
            warning.notSelected();
            return;
        }

        int shopId = Integer.parseInt(u_shopId.getText());
        int type = getShopType(u_type.getSelectionModel().getSelectedItem().toString());
        String contact = u_contact.getText();
        String address = u_address.getText();

        Shop newShop = new Shop(shopId,type,shopName,contact,address);
        newShop.delete();

        //Refresh Table
        Stage thiswind = (Stage) shopTable.getScene().getWindow();
        FXMLLoader shopsView = new FXMLLoader(getClass().getResource("/resources/views/shops.fxml"));
        Parent root = (Parent) shopsView.load();
        thiswind.setTitle("Manage Shops related to your business");
        thiswind.setScene(new Scene(root));
        thiswind.show();
        warning.deleteSuccess();
    }

    private int getShopType(String selectedItem) {
        if(selectedItem.equals("sell")){
            return 1;
        }else{
            return 0;
        }
    }

    private String getShopType(int selectedItem){
        if(selectedItem==1){
            return "sell";
        }else {
            return "buy";
        }
    }

    public void addShop(MouseEvent mouseEvent) throws SQLException, IOException {

        String shopName = u_name.getText();
        if(shopName.equals("")|| u_type.getSelectionModel().getSelectedItem().toString().equals("")|| u_contact.getText().equals("")|| u_address.getText().equals("")) {
            warning.incomplete();
            return;
        }

        int shopId = Integer.parseInt(u_shopId.getText());

        Shop checkShop = Shop.getShopById(shopId);
        if(checkShop!=null){
            warning.idExists();
            return;
        }


        int type = getShopType(u_type.getSelectionModel().getSelectedItem().toString());
        String contact = u_contact.getText();
        String address = u_address.getText();

        Shop newShop = new Shop(shopId,type,shopName,contact,address);
        newShop.save();

        //Refresh Table
        shopTable.getItems().add(newShop);
        u_shopId.clear();
        u_name.clear();
        u_type.getSelectionModel().clearSelection();
        u_address.clear();
        u_contact.clear();
        warning.saveSuccess();
    }

    public void getSelected(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()>1){
            onEdit();
        }
    }
    private void onEdit(){
        if(shopTable.getSelectionModel().getSelectedItem()!=null){
            Shop current = shopTable.getSelectionModel().getSelectedItem();
            u_shopId.setText(Integer.toString(current.getId()));
            u_name.setText(current.getName());
            u_type.getSelectionModel().select(getShopType(current.getType()));
            u_contact.setText(current.getContact());
            u_address.setText(current.getAddress());
        }
    }
}
