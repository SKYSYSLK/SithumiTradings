package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

public class mainMenu {
    public BarChart week;
    public PieChart day;
    public CategoryAxis xAxis;
    public NumberAxis yAxis;
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

    public void initialize(){
        // Weekly Chart Data
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getWeekdays();
        Days.addAll(Arrays.asList(Arrays.copyOfRange(months,1,8)));
        xAxis.setCategories(Days);
        setData();

        // Daily Chart Data
//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//                new PieChart.Data("Income",5000),
//                new PieChart.Data("spent",2000)
//        );
//        day.setData(pieChartData);
    }

    public void openItems(MouseEvent mouseEvent) throws IOException {
        Stage thiswind = (Stage) reports.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("../resources/views/items.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Items in your stock");
        thiswind.setScene(new Scene(root));
        thiswind.show();
    }

    public void openShops(MouseEvent mouseEvent) throws IOException{
        Stage thiswind = (Stage) shops.getScene().getWindow();
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("../resources/views/shop.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Your Custom");
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
        FXMLLoader itemsView = new FXMLLoader(getClass().getResource("../resources/views/buyInvoice.fxml"));
        Parent root = (Parent) itemsView.load();
        thiswind.setTitle("Manage Buying Invoices");
        thiswind.setScene(new Scene(root));
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
}
