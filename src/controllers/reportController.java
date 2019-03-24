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
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class reportController implements Initializable {

    public TableView<Invoice> shopReportTable;
    public TableColumn invoice_id;
    public TableColumn issued_date;
    public TableColumn amount;
    public TableColumn cheque_id;
    public TableColumn type;

    public JFXButton generate_report;
    public ComboBox shop_list;
    public DatePicker from_date;
    public DatePicker to_date;
    public JFXButton back;
    public CheckBox time_reports_check;

    private static int selectedShopId = 0;

    // Create a new font object selecting one of the PDF base fonts
    private PDFont fontPlain = PDType1Font.HELVETICA;
    private PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    private PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    private PDFont fontMono = PDType1Font.COURIER;


    public reportController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        generate_report.setDisable(true);

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
                generate_report.setDisable(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });


        try {
            fillShopCombo();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        enableSelections();
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

    public void generateReport() throws IOException, SQLException {
        Stage thisWindow = (Stage) shopReportTable.getScene().getWindow();

        // Saving location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        File file = fileChooser.showSaveDialog(thisWindow);

        if (file != null) {
            createAndSaveDocument(file);
        }
    }

    private void createAndSaveDocument(File file) throws IOException, SQLException {
        generateShopRelatedReport(2, file);
    }

    public void generateShopRelatedReport(int shopId, File file) throws IOException, SQLException {

        Shop shop = Shop.getShopById(shopId);

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle rect = page.getMediaBox();
        document.addPage(page);

        //Creating the PDDocumentInformation object
        PDDocumentInformation pdd = document.getDocumentInformation();

        //Setting the author of the document
        pdd.setAuthor("SkySys Software Solutions");

        // Setting the title of the document
        pdd.setTitle("Sithumi Tradings - Business Report");

        //Setting the created date of the document
        Calendar date = Calendar.getInstance();
        pdd.setCreationDate(date);

        // Text streams
        PDPageContentStream stream = new PDPageContentStream(document, page);

        // Page attributes
        float pageMargin = 40;

        String pageTitle = "GENERATED REPORT";
        int pageTitleFontSize = 18;
        String pageCreationTime = Calendar.getInstance().getTime().toString();

        float titleWidth = fontBold.getStringWidth(pageTitle) / 1000 * pageTitleFontSize;
        float titleHeight = fontBold.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * pageTitleFontSize;

        // header
        stream.beginText();
        stream.setLeading(16f);
        stream.setFont(fontBold, pageTitleFontSize);
        stream.newLineAtOffset((rect.getWidth() - titleWidth) / 2, rect.getHeight() - pageMargin - titleHeight);
        stream.showText(pageTitle);
        stream.newLine();
        stream.setFont(fontPlain, 10);
        stream.newLineAtOffset(-5, 0);
        stream.showText("Generated on " + pageCreationTime);
        stream.newLine();
        stream.endText();

        // shop details
        stream.beginText();
        stream.setLeading(14.5f);

        stream.setFont(fontBold, 14);
        stream.newLineAtOffset(pageMargin, rect.getHeight() - pageMargin - 60);
        stream.showText("Shop Details");
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop ID : ");
        stream.setFont(fontItalic, 10);
        stream.showText(String.valueOf(shopId));
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Name : ");
        stream.setFont(fontItalic, 10);
        stream.showText(shop.getName());
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Type : ");
        stream.setFont(fontItalic, 10);
        stream.showText(String.valueOf(shop.getType()));
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Contact : ");
        stream.setFont(fontItalic, 10);
        stream.showText(shop.getContact());
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Address : ");
        stream.setFont(fontItalic, 10);
        stream.showText(shop.getAddress());
        stream.newLine();
        stream.newLine();

        stream.setFont(fontBold, 14);
        stream.showText("Business Details");
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Total x : ");
        stream.setFont(fontItalic, 10);
        stream.showText(String.valueOf(shopId));
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Name : ");
        stream.setFont(fontItalic, 10);
        stream.showText("sds");
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Type : ");
        stream.setFont(fontItalic, 10);
        stream.showText("fsfs");
        stream.newLine();
        stream.newLine();
        stream.newLine();

        stream.endText();

        //Dummy Table
        // starting y position is whole page height subtracted by top and bottom pageMargin
        float yStartNewPage = rect.getHeight() - pageMargin - 250;
        float yStart = yStartNewPage;
        float bottomMargin = 20;
        // we want table across whole page width (subtracted by left and right pageMargin of course)
        float tableWidth = page.getMediaBox().getWidth() - (2 * pageMargin);

        BaseTable baseTable = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth,
                pageMargin, document, page, true, true);

        // Create Header row
        Row<PDPage> headerRow = baseTable.createRow(15f);
        Cell<PDPage> cell = headerRow.createCell(100, "Related Invoice Details");
        cell.setFont(fontBold);
        cell.setFillColor(Color.GRAY);
        cell.setTextColor(Color.WHITE);

        baseTable.addHeaderRow(headerRow);

        // Create 2 column row
        Row<PDPage> row = baseTable.createRow(15f);
        cell = row.createCell(20, "Invoice ID : ");
        cell.setFont(fontPlain);

        cell = row.createCell(20, "Issued Date : ");
        cell.setFont(fontPlain);

        cell = row.createCell(20, "Amount : ");
        cell.setFont(fontPlain);

        cell = row.createCell(20, "Cheque No : ");
        cell.setFont(fontPlain);

        cell = row.createCell(20, "Type : ");
        cell.setFont(fontPlain);

        for(Invoice invoice: invoiceData) {
            Row<PDPage> rowData = baseTable.createRow(15f);
            cell = rowData.createCell(20, invoice.getId());
            cell.setFont(fontPlain);

            cell = rowData.createCell(20, invoice.getDate_issue());
            cell.setFont(fontPlain);

            cell = rowData.createCell(20, String.valueOf(invoice.getAmount()));
            cell.setFont(fontPlain);

            cell = rowData.createCell(20, invoice.getCheque_id());
            cell.setFont(fontPlain);

            cell = rowData.createCell(20, String.valueOf(invoice.getType()));
            cell.setFont(fontPlain);
        }

        baseTable.draw();

        stream.close();

        document.save(file);
        document.close();
    }

    private void fillShopCombo() throws SQLException {
        ArrayList<Shop> allShops = Shop.getAll();
        for (Shop shop : allShops) {
            shop_list.getItems().add(shop.getName());
        }
    }

    public void enableSelections() {
        if (time_reports_check.isSelected()) {
            from_date.setDisable(false);
            to_date.setDisable(false);
        } else {
            from_date.setDisable(true);
            to_date.setDisable(true);
        }
    }
}
