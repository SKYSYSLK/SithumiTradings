package controllers;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    public JFXDatePicker from_date;
    public JFXDatePicker to_date;
    public JFXButton back;
    public CheckBox time_reports_check;

    private static int selectedShopId = 0;
    private static int activateGenerateButton = 0;

    // Create a new font object selecting one of the PDF base fonts
    private PDFont fontPlain = PDType1Font.HELVETICA;
    private PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    private PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    private PDFont fontMono = PDType1Font.COURIER;


    public reportController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // disable the buttons and check box at start
        generate_report.setDisable(true);
        time_reports_check.setDisable(true);

        invoice_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        issued_date.setCellValueFactory(new PropertyValueFactory<>("date_issue"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        cheque_id.setCellValueFactory(new PropertyValueFactory<>("cheque_id"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        shopReportTable.setItems(invoiceData);

        // shop list combo box listener
        shop_list.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                reportController.selectedShopId = Shop.getShopId((String) newValue);
                invoiceData.removeAll(invoiceData);
                for (Invoice invoice : Report.getInvoicesByShop(reportController.selectedShopId)) {
                    invoiceData.add(invoice);
                }
                shopReportTable.setItems(invoiceData);
                generate_report.setDisable(false);
                reportController.activateGenerateButton = 2;
                time_reports_check.setDisable(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // from date event listener
        from_date.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ArrayList<Invoice> temp = Report.getInvoicesByShop(reportController.selectedShopId);
                invoiceData.removeAll(invoiceData);
                for (Invoice invoice : temp) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = format.parse(invoice.getDate_issue());
                    Date date2 = format.parse(from_date.getValue().toString());
                    if (date1.compareTo(date2) >= 0)
                        invoiceData.add(invoice);
                }
                shopReportTable.setItems(invoiceData);
                if (reportController.activateGenerateButton == 0) {
                    reportController.activateGenerateButton = 1;
                } else {
                    generate_report.setDisable(false);
                    reportController.activateGenerateButton = 2;
                }
            } catch (Exception ex) {
                System.err.println(ex);
            }
        });

        // to date event listener
        to_date.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ObservableList<Invoice> temp = FXCollections.observableArrayList(invoiceData);
                invoiceData.removeAll(invoiceData);
                for (Invoice invoice : temp) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = format.parse(invoice.getDate_issue());
                    Date date2 = format.parse(to_date.getValue().toString());
                    if (date1.compareTo(date2) <= 0)
                        invoiceData.add(invoice);
                }
                shopReportTable.setItems(invoiceData);
                if (reportController.activateGenerateButton == 0) {
                    reportController.activateGenerateButton = 1;
                } else {
                    generate_report.setDisable(false);
                    reportController.activateGenerateButton = 2;
                }
            } catch (Exception ex) {
                System.err.println(ex);
            }
        });


        try {
            fillShopCombo();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // time based check box selection
        enableSelections();
    }

    private ObservableList<Invoice> invoiceData = FXCollections.observableArrayList(
            Report.getInvoicesByShop(reportController.selectedShopId)
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage) shopReportTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("/resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu");
        thisWindow.setScene(new Scene(root));
    }

    // generate button click action
    public void generateReport() throws IOException, SQLException {
        Stage thisWindow = (Stage) shopReportTable.getScene().getWindow();
        // Saving location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        File file = fileChooser.showSaveDialog(thisWindow);

        if (file != null) {
            generateShopRelatedReport(reportController.selectedShopId, file);
            warning.saveSuccess();
        }
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
        stream.setFont(fontPlain, 8);
        stream.newLineAtOffset(rect.getWidth() - 10, rect.getHeight() - 10);
        stream.showText("01");
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
        if (shop.getType() == 0) {
            stream.showText("Buy");
        } else {
            stream.showText("Sell");
        }
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
        if (time_reports_check.isSelected()) {
            stream.setFont(fontBold, 10);
            stream.showText("From : ");
            stream.setFont(fontItalic, 10);
            stream.showText(from_date.getValue().toString());
            stream.newLine();
            stream.setFont(fontBold, 10);
            stream.showText("To : ");
            stream.setFont(fontItalic, 10);
            stream.showText(to_date.getValue().toString());
            stream.newLine();
        }
        stream.setFont(fontBold, 10);
        stream.showText("Total Profit : ");
        stream.setFont(fontItalic, 10);
        stream.showText(String.valueOf(totalProfit()));

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
        cell = row.createCell(20, "Invoice ID");
        cell.setFont(fontBold);

        cell = row.createCell(20, "Issued Date");
        cell.setFont(fontBold);

        cell = row.createCell(20, "Amount");
        cell.setFont(fontBold);

        cell = row.createCell(20, "Cheque No");
        cell.setFont(fontBold);

        cell = row.createCell(20, "Type");
        cell.setFont(fontBold);

        for (Invoice invoice : invoiceData) {
            Row<PDPage> rowData = baseTable.createRow(15f);
            cell = rowData.createCell(20, invoice.getId());
            cell.setFont(fontPlain);

            cell = rowData.createCell(20, invoice.getDate_issue());
            cell.setFont(fontPlain);

            cell = rowData.createCell(20, String.valueOf(invoice.getAmount()));
            cell.setFont(fontPlain);

            cell = rowData.createCell(20, invoice.getCheque_id());
            cell.setFont(fontPlain);

            if (invoice.getType() == 1) {
                cell = rowData.createCell(20, "Buy Invoice");
            } else {
                cell = rowData.createCell(20, "Sell Invoice");
            }

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

    // time based check box selection action
    public void enableSelections() {
        if (time_reports_check.isSelected()) {
            from_date.setDisable(false);
            from_date.setValue(null);
            to_date.setDisable(false);
            to_date.setValue(null);
            generate_report.setDisable(true);
            reportController.activateGenerateButton = 0;
        } else {
            from_date.setDisable(true);
            to_date.setDisable(true);
        }
    }

    public double totalProfit() {
        double buy = 0;
        double sell = 0;
        for (Invoice invoice : invoiceData) {
            if (invoice.getType() == 1) {
                buy += invoice.getAmount();
            } else if (invoice.getType() == 2) {
                sell += invoice.getAmount();
            }
        }
        return sell - buy;
    }
}
