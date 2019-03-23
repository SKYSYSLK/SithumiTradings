package controllers;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Invoice;
import models.Report;
import models.Shop;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class reportController implements Initializable {

    public TableView<Invoice> shopReportTable;
    public TableColumn invoice_id;
    public TableColumn issued_date;
    public TableColumn amount;
    public TableColumn cheque_id;
    public TableColumn type;

    public TableView<Report> timeReportTable;

    public JFXButton generate_report;
    public RadioButton shop_reports_radio;
    public RadioButton time_reports_radio;
    public ComboBox shop_list;
    public DatePicker from_date;
    public DatePicker to_date;
    public JFXButton back;

    private static int selectedShopId = 0;

    public reportController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        invoice_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        issued_date.setCellValueFactory(new PropertyValueFactory<>("date_issued"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        cheque_id.setCellValueFactory(new PropertyValueFactory<>("cheque_id"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        shopReportTable.setItems(invoiceData);

        shop_list.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                reportController.selectedShopId = Shop.getShopId((String) newValue);
                invoiceData.removeAll(invoiceData);
                for (Invoice invoice : Report.getInvoicesByShop(reportController.selectedShopId)) {
                    invoiceData.add(invoice);
                }
                shopReportTable.setItems(invoiceData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });


        try {
            fillShopCombo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Invoice> invoiceData = FXCollections.observableArrayList(
            Report.getInvoicesByShop(reportController.selectedShopId)
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage) shopReportTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("../resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu");
        thisWindow.setScene(new Scene(root));
    }

    public void generateReport() throws IOException {
        Stage thisWindow = (Stage) shopReportTable.getScene().getWindow();

        // Saving location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        File file = fileChooser.showSaveDialog(thisWindow);

        if (file != null) {
            createAndSaveDocument(file);
        }
    }

    private void createAndSaveDocument(File file) throws IOException {

        // Create a new font object selecting one of the PDF base fonts
        PDFont fontPlain = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
        PDFont fontMono = PDType1Font.COURIER;

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle rect = page.getMediaBox();
        document.addPage(page);

        //Dummy Table
        float margin = 50;
        // starting y position is whole page height subtracted by top and bottom margin
        float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
        float yStart = yStartNewPage;
        float bottomMargin = 20;
        // we want table across whole page width (subtracted by left and right margin ofcourse)
        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

        BaseTable baseTable = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth,
                margin, document, page, true, true);

        // Create Header row
        Row<PDPage> headerRow = baseTable.createRow(15f);
        Cell<PDPage> cell = headerRow.createCell(100, "Awesome Facts About Belgium");
        cell.setFont(fontBold);
        cell.setFillColor(Color.BLACK);
        cell.setTextColor(Color.WHITE);

        baseTable.addHeaderRow(headerRow);

        // Create 2 column row
        Row<PDPage> row = baseTable.createRow(15f);
        cell = row.createCell(30, "Source:");
        cell.setFont(fontPlain);

        cell = row.createCell(70, "http://www.factsofbelgium.com/");
        cell.setFont(fontPlain);

        baseTable.draw();

        document.save(file);
        document.close();
    }

    public void generateShopRelatedReport(int shopId) {

    }

    private void fillShopCombo() throws SQLException {
        ArrayList<Shop> allShops = Shop.getAll();
        for (Shop shop : allShops) {
            shop_list.getItems().add(shop.getName());
        }
    }
}
