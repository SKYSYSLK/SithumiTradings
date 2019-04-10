package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Cheque;
import models.t_cheque;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;

public class mainMenu {
    public BarChart week;
    public CategoryAxis xAxis;
    public NumberAxis yAxis;
    public TableColumn<t_cheque, String> id;
    public TableColumn<t_cheque, String> amount;
    public TableColumn<t_cheque, String> issueDate;
    public TableColumn<t_cheque, String> expireDate;
    public TableView<t_cheque> recentCheques;
    @FXML
    private JFXButton reports;
    @FXML
    private JFXButton sell;
    @FXML
    private JFXButton buy;
    @FXML
    private JFXButton items;
    @FXML
    private JFXButton shops;

    private ObservableList<String> Days = FXCollections.observableArrayList();

    public mainMenu() throws SQLException {
    }

    public void initialize() throws SQLException, ParseException {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expireDate.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
        issueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        recentCheques.setItems(chequesData);

        // Weekly Chart Data
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getWeekdays();
        Days.addAll(Arrays.asList(Arrays.copyOfRange(months,1,8)));
        xAxis.setCategories(Days);
        setData();
        Cheque.checkExpire();

    }
    private ObservableList<t_cheque> chequesData = FXCollections.observableArrayList(
            t_cheque.getRecent()
    );

    public void openItems(MouseEvent mouseEvent) throws IOException {
        Stage thiswind = (Stage) reports.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("/resources/views/items.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Items in your stock");
        thiswind.setScene(new Scene(root));
        thiswind.setMaximized(false);
        thiswind.show();
    }

    public void openReports(MouseEvent mouseEvent) throws IOException {
        Stage thiswind = (Stage) reports.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("/resources/views/reports.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Items in your stock");
        thiswind.setScene(new Scene(root));
        thiswind.setMaximized(false);
        thiswind.show();
    }

    public void openSell(MouseEvent mouseEvent) throws IOException {
        Stage thiswind = (Stage) reports.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("/resources/views/sellInvoice.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Daily Business");
        thiswind.setScene(new Scene(root));
        thiswind.setMaximized(false);
        thiswind.show();
    }

    public void openBuy(MouseEvent mouseEvent) throws IOException {
        Stage thiswind = (Stage) reports.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("/resources/views/buyInvoice.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Buying Invoices");
        thiswind.setScene(new Scene(root));
        thiswind.setMaximized(false);
        thiswind.show();
    }

    public void openShops(MouseEvent mouseEvent) throws IOException {
        Stage thiswind = (Stage) reports.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("/resources/views/shops.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Shops related to your business");
        thiswind.setScene(new Scene(root));
        thiswind.setMaximized(false);
        thiswind.show();
    }

    public void setData(){
        int[] dayCounter = new int[7];
        dayCounter[0]=50;
        dayCounter[1]=75;
        dayCounter[2]=85;
        dayCounter[3]=15;
        dayCounter[4]=85;
        dayCounter[5]=80;
        dayCounter[6]=25;

        XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
        for(int i=0;i<dayCounter.length;i++){
            series1.getData().add(new XYChart.Data<>(Days.get(i),dayCounter[i]));
        }
        week.getData().add(series1);
    }

    public void openCheques(MouseEvent mouseEvent) throws IOException {
        FXMLLoader load = new FXMLLoader(getClass().getResource("/resources/views/cheques.fxml"));
        Stage thiswind = (Stage) reports.getScene().getWindow();
        Parent root = load.load();
        thiswind.setTitle("Manage Cheque");
        thiswind.setScene(new Scene(root));
        thiswind.show();
    }
}
