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
import models.Shop;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;




public class shopController implements Initializable {
    public TableView<Shop> shopsTable;

    public TableColumn<Shop,Integer> id;
    public TableColumn<Shop,String> name;
    public TableColumn<Shop,String> contact;
    public TableColumn<Shop,String> address;
    public TableColumn<Shop,Integer> type;
    public JFXTextField a_type;
    public JFXTextField a_address;
    public JFXTextField a_contact;
    public JFXTextField a_name;
    public JFXTextField a_id;
    public JFXTextField u_id;
    public JFXTextField u_name;
    public JFXTextField u_contact;
    public JFXTextField u_address;
    public JFXTextField u_type;


    public shopController() throws SQLException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources){
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        shopsTable.setItems(shopData);
    }

    private ObservableList<Shop> shopData = FXCollections.observableArrayList(
            Shop.getAll()
    );


    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)shopsTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu");
        thisWindow.setScene(new Scene(root));

    }

    public void addShop(MouseEvent mouseEvent) throws SQLException, IOException {
        String shopId = a_id.getText();
        String shopName = a_name.getText();
        String shopContact = a_contact.getText();
        String shopAddress = a_address.getText();
        int shopType = Integer.parseInt(a_type.getText());

        if(shopId.equals("")||shopName.equals("")||a_contact.getText().equals("")||a_address.getText().equals("")||a_type.getText().equals("")) {
            warning.incomplete();
            return;
        }

        Shop newShop = new Shop(Integer.parseInt(shopId),shopType,shopName,shopContact,shopAddress);
        newShop.save();

        shopsTable.getItems().add(newShop);
        a_type.clear();
        a_id.clear();
        a_name.clear();
        a_address.clear();
        a_contact.clear();

    }

    public void updateShop(MouseEvent mouseEvent) throws SQLException, IOException{

    }





}

