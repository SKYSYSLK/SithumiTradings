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
import models.Item;
import models.t_invoice;
import models.Invoice;
import models.t_invoiceItem;


import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SellInvoiceController implements Initializable {

    public TableColumn<Item, String> invoice_id;
    public TableColumn<Item, String> shopName;
    public TableColumn<Item, Float> amount;
    public TableColumn<Item, String> dateIssue;
    public TableColumn<Item, String> checkNo;
    public JFXButton back;
    public TableView<t_invoice> invoiceTable;
//    private static TableView<Item> itemTable1;

    public SellInvoiceController() throws SQLException {
    }

    public void getSelected(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        invoice_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        shopName.setCellValueFactory(new PropertyValueFactory<>("shopName"));
        dateIssue.setCellValueFactory(new PropertyValueFactory<>("dateIsssued"));
        checkNo.setCellValueFactory(new PropertyValueFactory<>("cheque_number"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        invoiceTable.setItems(itemData);}

    private ObservableList<t_invoice> itemData = FXCollections.observableArrayList(
            t_invoice.getAll(2)
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)invoiceTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("/resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu ");
        thisWindow.setScene(new Scene(root));
    }

    public void addNew(MouseEvent mouseEvent) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("/resources/views/Sell.fxml"));
        Stage model = (Stage)back.getScene().getWindow();
        Parent root = load.load();
        model.setTitle("Add New Record");
        model.setScene(new Scene(root));
        model.show();
    }

    public void delete(MouseEvent mouseEvent) throws SQLException {
        t_invoice item = invoiceTable.getSelectionModel().getSelectedItem();
        if(item==null) return;
        item.delete();
        invoiceTable.getItems().remove(item);
    }

    public void editRecord(MouseEvent mouseEvent) throws SQLException, ParseException, IOException {
        if(invoiceTable.getSelectionModel().getSelectedItem()==null){
            FXMLLoader load = new FXMLLoader(getClass().getResource("/resources/views/alert/selectItemError.fxml"));
            Stage model = new Stage();
            Parent root = load.load();
            model.setTitle("Error");
            model.initModality(Modality.APPLICATION_MODAL);
            model.setScene(new Scene(root));
            model.show();
            return;
        }else{
            t_invoice currentSelected = invoiceTable.getSelectionModel().getSelectedItem();
            System.out.println(currentSelected.getShopName());
            FXMLLoader load = new FXMLLoader(getClass().getResource("/resources/views/Sell.fxml"));
            Stage model = (Stage)back.getScene().getWindow();
            Parent root = load.load();
            sellController controller = load.getController();
            controller.setInvoice(currentSelected.getId());
            controller.setUpdate(true);
            model.setTitle("Update Record");
            model.setScene(new Scene(root));
            model.show();
        }

    }


}
