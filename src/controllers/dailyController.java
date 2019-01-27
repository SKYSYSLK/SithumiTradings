package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.daily;
import models.item;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author danushka
 */
public class dailyController implements Initializable {
    public JFXTextField itemId;
    public JFXTextField itemName;
    public JFXTextField itemquantity;
    public JFXTextField itemSellPrice;
    public Text income;
    public Text profit;
    public TableView<daily> itemTable;
    public TableColumn<daily,String> item_no;
    public TableColumn<daily,String> name;
    public TableColumn<daily,Integer> quantity;
    public TableColumn<daily,Float> sellPrice;
    public TableColumn<daily,Float> Total;
    public TableColumn<daily,Float> itemProfit;
    public JFXButton back;
    public JFXDatePicker date;
    public JFXButton add;
    private LocalDate today = LocalDate.now();
    public dailyController() throws SQLException {
    }

    public void getSelected(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        item_no.setCellValueFactory(new PropertyValueFactory<>("item_no"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<>("sale_price"));
        Total.setCellValueFactory(new PropertyValueFactory<>("income"));
        itemProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));
        itemTable.setItems(itemData);
        try {
            setCalculations();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        itemTable.setRowFactory(
                (TableView<daily> tableview) -> {
                    final TableRow<daily> row = new TableRow<>();
                    final ContextMenu menu = new ContextMenu();
                    MenuItem removeButton = new MenuItem("remove");
                    removeButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            daily item1 = itemTable.getSelectionModel().getSelectedItem();
                            itemTable.getItems().remove(item1);
                            try {
                                item1.delete();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    menu.getItems().add(removeButton);
                    row.contextMenuProperty().bind(
                            javafx.beans.binding.Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                    .then(menu)
                                    .otherwise((ContextMenu) null));
                    return row;
                });
    }
    private ObservableList<daily> itemData = FXCollections.observableArrayList(
            daily.getAll(java.sql.Date.valueOf(today).toString())
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage)itemTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu  ");
        thisWindow.setScene(new Scene(root));
    }

    public void getName(MouseEvent mouseEvent) throws SQLException {
        item current = item.getItem(itemId.getText());
        if(current!=null) {
            itemName.setText(current.getName());
            itemSellPrice.setText(Float.toString(current.getSellPrice()));
        }
        else itemName.setText("Item could not be found");
    }

    public void addItem(MouseEvent mouseEvent) throws SQLException {
        String day = java.sql.Date.valueOf(date.getValue()).toString();
        String itemID = itemId.getText();
        int itemQuantity = Integer.parseInt(itemquantity.getText());
        float sellPrice = Float.parseFloat(itemSellPrice.getText());

        item current = item.getItem(itemID);
        assert current != null;
        float profit = (sellPrice-current.getBuyPrice())*itemQuantity;
        if(profit<0) profit=0;
        float income = sellPrice*itemQuantity;
        if(current.getSellPrice()!=sellPrice){
            current.setSellPrice(sellPrice);
        }

        int currentQuantity = current.getQuantity()-itemQuantity;
        current.setQuantity(currentQuantity);
        current.update();

        daily dailyItem = new daily(day,itemID,itemQuantity,sellPrice,income,profit);
        dailyItem.save();
        itemTable.getItems().add(dailyItem);
        clearInputs();
        setCalculations();
    }

    public void getToday(MouseEvent mouseEvent) throws SQLException {
        if (date.getValue()==null) return;
        String day = java.sql.Date.valueOf(date.getValue()).toString();
        itemTable.getItems().clear();
        itemTable.setItems(FXCollections.observableArrayList(daily.getAll(day)));
        setCalculations();
    }

    public void clearInputs(){
        itemId.setText("");
        itemName.setText("");
        itemquantity.setText("");
        itemSellPrice.setText("");
    }
    private void setCalculations() throws SQLException {
        String day;
        float income1=0, profit1=0;
        if(date.getValue()!=null){
            day = java.sql.Date.valueOf(date.getValue().toString()).toString();
        }else{
            day = java.sql.Date.valueOf(today.toString()).toString();
        }
        ArrayList<daily> allData= daily.getAll(day);
        for (daily data:allData){
            income1 += data.getIncome();
            profit1 += data.getProfit();
        }
        income.setText(Float.toString(income1));
        profit.setText(Float.toString(profit1));
    }

}
