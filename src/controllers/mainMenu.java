package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class mainMenu {
    @FXML
    private JFXButton reports;
    @FXML
    private JFXButton sell;
    @FXML
    private JFXButton buy;
    @FXML
    private JFXButton items;

    public void openItems(MouseEvent mouseEvent) throws IOException {
        Stage thiswind = (Stage) reports.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("../resources/views/items.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Items in your stock");
        thiswind.setScene(new Scene(root));
        thiswind.show();
    }

    public void openReports(MouseEvent mouseEvent) {
    }

    public void openSell(MouseEvent mouseEvent) throws IOException {
        Stage thiswind = (Stage) reports.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("../resources/views/daily.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Daily Business");
        thiswind.setScene(new Scene(root));
        thiswind.show();
    }

    public void openBuy(MouseEvent mouseEvent) throws IOException {
        Stage thiswind = (Stage) reports.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("../resources/views/buy.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Buying Items");
        thiswind.setScene(new Scene(root));
        thiswind.show();
    }
}
