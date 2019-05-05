package controllers;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.utils.PDStreamUtils;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class reportController implements Initializable {

    public TableView<Invoice> shopReportTable;
    public TableColumn invoice_id;
    public TableColumn shop_id;
    public TableColumn issued_date;
    public TableColumn amount;
    public TableColumn cheque_id;
    public TableColumn type;

    public JFXButton generate_report;
    public JFXButton execute_query;
    public ComboBox shop_list;
    public JFXDatePicker from_date;
    public JFXDatePicker to_date;
    public JFXButton back;
    public CheckBox time_reports_check;
    public CheckBox shop_reports_check;

    private static int selectedShopId = -1;
    private static String selectedFromDate = "";
    private static String selectedToDate = "";
    private static int activateExecuteButton = 0;

    // Create a new font object selecting one of the PDF base fonts
    private PDFont fontPlain = PDType1Font.HELVETICA;
    private PDFont fontBold = PDType1Font.HELVETICA_BOLD;

    public reportController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // disable the buttons at start
        generate_report.setDisable(true);
        execute_query.setDisable(true);

        invoice_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        shop_id.setCellValueFactory(new PropertyValueFactory<>("shop_id"));
        issued_date.setCellValueFactory(new PropertyValueFactory<>("date_issue"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        cheque_id.setCellValueFactory(new PropertyValueFactory<>("cheque_id"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        try {
            fillShopCombo();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // time based check box selection
        enableTimeSelections();

        // shop based check box selection
        enableShopSelections();
    }

    private ObservableList<Invoice> invoiceDataByShopAndDate = FXCollections.observableArrayList(
            Report.getInvoicesByShopAndIssuedDate(
                    reportController.selectedShopId,
                    reportController.selectedFromDate,
                    reportController.selectedToDate)
    );

    private ObservableList<Invoice> invoiceDataByShop = FXCollections.observableArrayList(
            Report.getInvoicesByShop(
                    reportController.selectedShopId)
    );

    private ObservableList<Invoice> invoiceDataByDate = FXCollections.observableArrayList(
            Report.getInvoicesByDate(
                    reportController.selectedFromDate,
                    reportController.selectedToDate)
    );

    public void backMenu(MouseEvent mouseEvent) throws IOException {
        Stage thisWindow = (Stage) shopReportTable.getScene().getWindow();
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource("/resources/views/mainMenu.fxml"));
        Parent root = backLoader.load();
        thisWindow.setTitle("Main Menu");
        thisWindow.setScene(new Scene(root));
    }

    // execute query button click action
    public void executeQuery() throws IOException, SQLException, ParseException {
        if (shop_list.getValue() != null) {
            reportController.selectedShopId = Shop.getShopId(shop_list.getValue().toString());
        }
        if (from_date.getValue() != null) {
            reportController.selectedFromDate = from_date.getValue().toString();
        }
        if (to_date.getValue() != null) {
            reportController.selectedToDate = to_date.getValue().toString();
        }

        try {
            invoiceDataByShopAndDate.removeAll(invoiceDataByShopAndDate);
            invoiceDataByShop.removeAll(invoiceDataByShop);
            invoiceDataByDate.removeAll(invoiceDataByDate);

            if (shop_reports_check.isSelected() && time_reports_check.isSelected()) {
                for (Invoice invoice : Report.getInvoicesByShopAndIssuedDate(
                        reportController.selectedShopId,
                        reportController.selectedFromDate,
                        reportController.selectedToDate)) {
                    invoiceDataByShopAndDate.add(invoice);
                }
                shopReportTable.setItems(invoiceDataByShopAndDate);
            } else if (shop_reports_check.isSelected()) {
                for (Invoice invoice : Report.getInvoicesByShop(
                        reportController.selectedShopId)) {
                    invoiceDataByShop.add(invoice);
                }
                shopReportTable.setItems(invoiceDataByShop);
            } else if (time_reports_check.isSelected()) {
                for (Invoice invoice : Report.getInvoicesByDate(
                        reportController.selectedFromDate,
                        reportController.selectedToDate)) {
                    invoiceDataByDate.add(invoice);
                }
                shopReportTable.setItems(invoiceDataByDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (shopReportTable.getItems().size() != 0) {
            generate_report.setDisable(false);
        } else {
            generate_report.setDisable(true);
        }
    }

    // generate button click action
    public void generateReport() throws IOException, SQLException {
        Stage thisWindow = (Stage) shopReportTable.getScene().getWindow();
        // Saving location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        File file = fileChooser.showSaveDialog(thisWindow);

        if (file != null) {
            if (reportController.selectedShopId != -1) {
                generateShopRelatedReport(reportController.selectedShopId, file);
                warning.saveSuccess();
            } else {
                generateTimeRelatedReport(file);
                warning.saveSuccess();
            }
        }
        generate_report.setDisable(true);
    }

    private void generateShopRelatedReport(int shopId, File file) throws IOException, SQLException {

        Shop shop = Shop.getShopById(shopId);

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle rect = page.getMediaBox();
        document.addPage(page);

        // Text streams
        PDPageContentStream stream = new PDPageContentStream(document, page);

        createReportMetaData(document.getDocumentInformation());
        createReportHeaderSection(stream, rect);
        createReportShopSection(stream, rect, shop);
        createReportTableShopBasedSection(document, page, rect);
        addFooterAndBorder(document, rect);

        stream.close();
        document.save(file);
        document.close();
    }

    private void generateTimeRelatedReport(File file) throws IOException, SQLException {

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle rect = page.getMediaBox();
        document.addPage(page);

        // Text streams
        PDPageContentStream stream = new PDPageContentStream(document, page);

        createReportMetaData(document.getDocumentInformation());
        createReportHeaderSection(stream, rect);
        createReportTimeSection(stream, rect);
        createReportTableTimeBasedSection(document, page, rect);
        addFooterAndBorder(document, rect);

        stream.close();
        document.save(file);
        document.close();
    }

    private void createReportHeaderSection(PDPageContentStream stream, PDRectangle rect) throws IOException {
        String pageTitle = "REPORT GENERATION";
        int pageTitleFontSize = 18;
        String pageCreationTime = Calendar.getInstance().getTime().toString();

        float titleWidth = fontBold.getStringWidth(pageTitle) / 1000 * pageTitleFontSize;
        float titleHeight = fontBold.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * pageTitleFontSize;

        // header
        stream.beginText();
        stream.setLeading(16f);

        stream.setFont(fontBold, pageTitleFontSize);
        stream.newLineAtOffset((rect.getWidth() - titleWidth) / 2, rect.getHeight() - 40 - titleHeight);
        stream.showText(pageTitle);
        stream.newLine();

        stream.setFont(fontPlain, 10);
        stream.newLineAtOffset(0, 0);
        stream.showText("Generated on " + pageCreationTime);
        stream.newLine();

        stream.endText();
    }

    private void createReportMetaData(PDDocumentInformation pdd) {
        //Setting the author of the document
        pdd.setAuthor("SkySys Software Solutions");

        // Setting the title of the document
        pdd.setTitle("Sithumi Tradings - Business Report");

        //Setting the created date of the document
        Calendar date = Calendar.getInstance();
        pdd.setCreationDate(date);
    }

    private void createReportShopSection(PDPageContentStream stream, PDRectangle rect, Shop shop) throws IOException {

        stream.beginText();
        stream.setLeading(14.5f);
        stream.setFont(fontBold, 14);
        stream.newLineAtOffset(40, rect.getHeight() - 100);
        stream.showText("Shop Details");
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop ID : ");
        stream.setFont(fontPlain, 10);
        stream.showText(String.valueOf(shop.getId()));
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Name : ");
        stream.setFont(fontPlain, 10);
        stream.showText(shop.getName());
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Type : ");
        stream.setFont(fontPlain, 10);
        if (shop.getType() == 0) {
            stream.showText("Buy");
        } else {
            stream.showText("Sell");
        }
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Contact : ");
        stream.setFont(fontPlain, 10);
        stream.showText(shop.getContact());
        stream.newLine();
        stream.setFont(fontBold, 10);
        stream.showText("Shop Address : ");
        stream.setFont(fontPlain, 10);
        stream.showText(shop.getAddress());
        stream.newLine();
        stream.newLine();

        stream.setFont(fontBold, 14);
        stream.showText("Business Details");
        stream.newLine();
        if (time_reports_check.isSelected()) {
            stream.setFont(fontBold, 10);
            stream.showText("From : ");
            stream.setFont(fontPlain, 10);
            stream.showText(from_date.getValue().toString());
            stream.newLine();
            stream.setFont(fontBold, 10);
            stream.showText("To : ");
            stream.setFont(fontPlain, 10);
            stream.showText(to_date.getValue().toString());
            stream.newLine();
        }
        stream.setFont(fontBold, 10);
        stream.showText("Total Profit : ");
        stream.setFont(fontPlain, 10);
        stream.showText(String.valueOf(totalProfit()));
        stream.newLine();
        stream.newLine();

        stream.endText();
    }

    private void createReportTimeSection(PDPageContentStream stream, PDRectangle rect) throws IOException {
        stream.beginText();
        stream.setLeading(14.5f);
        stream.setFont(fontBold, 14);
        stream.newLineAtOffset(40, rect.getHeight() - 100);
        stream.showText("Business Details");
        stream.newLine();
        if (time_reports_check.isSelected()) {
            stream.setFont(fontBold, 10);
            stream.showText("From : ");
            stream.setFont(fontPlain, 10);
            stream.showText(selectedFromDate);
            stream.newLine();
            stream.setFont(fontBold, 10);
            stream.showText("To : ");
            stream.setFont(fontPlain, 10);
            stream.showText(selectedToDate);
            stream.newLine();
        }
        stream.setFont(fontBold, 10);
        stream.showText("Total Profit : ");
        stream.setFont(fontPlain, 10);
        stream.showText(String.valueOf(totalProfit()));
        stream.newLine();
        stream.newLine();

        stream.endText();
    }

    private void createReportTableShopBasedSection(PDDocument document, PDPage page, PDRectangle rect) throws IOException {
        // starting y position is whole page height subtracted by top and bottom pageMargin
        float pageMargin = 40;
        float yStartNewPage = rect.getHeight() - pageMargin;
        float yStart = yStartNewPage - 250;
        float bottomMargin = 30;
        // we want table across whole page width (subtracted by left and right pageMargin of course)
        float tableWidth = rect.getWidth() - (2 * pageMargin);

        BaseTable baseTable = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, pageMargin, document, page, true, true);

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

        if (shop_reports_check.isSelected() && time_reports_check.isSelected()) {
            for (Invoice invoice : invoiceDataByShopAndDate) {

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
        } else if (shop_reports_check.isSelected()) {
            for (Invoice invoice : invoiceDataByShop) {

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
        }

        baseTable.draw();
    }

    private void createReportTableTimeBasedSection(PDDocument document, PDPage page, PDRectangle rect) throws IOException, SQLException {
        // starting y position is whole page height subtracted by top and bottom pageMargin
        float pageMargin = 40;
        float yStartNewPage = rect.getHeight() - pageMargin;
        float yStart = yStartNewPage - 250;
        float bottomMargin = 30;
        // we want table across whole page width (subtracted by left and right pageMargin of course)
        float tableWidth = rect.getWidth() - (2 * pageMargin);

        BaseTable baseTable = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, pageMargin, document, page, true, true);

        // Create Header row
        Row<PDPage> headerRow = baseTable.createRow(15f);
        Cell<PDPage> cell = headerRow.createCell(100, "Related Invoice Details");
        cell.setFont(fontBold);
        cell.setFillColor(Color.GRAY);
        cell.setTextColor(Color.WHITE);

        baseTable.addHeaderRow(headerRow);

        // Create 2 column row
        Row<PDPage> row = baseTable.createRow(15f);
        cell = row.createCell(16, "Invoice ID");
        cell.setFont(fontBold);

        cell = row.createCell(16, "Shop Name");
        cell.setFont(fontBold);

        cell = row.createCell(16, "Issued Date");
        cell.setFont(fontBold);

        cell = row.createCell(16, "Amount");
        cell.setFont(fontBold);

        cell = row.createCell(16, "Cheque No");
        cell.setFont(fontBold);

        cell = row.createCell(20, "Type");
        cell.setFont(fontBold);

        if (time_reports_check.isSelected()) {
            for (Invoice invoice : invoiceDataByDate) {

                Row<PDPage> rowData = baseTable.createRow(15f);
                cell = rowData.createCell(16, invoice.getId());
                cell.setFont(fontPlain);

                cell = rowData.createCell(16, Shop.getShopName(invoice.getShop_id()));
                cell.setFont(fontPlain);

                cell = rowData.createCell(16, invoice.getDate_issue());
                cell.setFont(fontPlain);

                cell = rowData.createCell(16, String.valueOf(invoice.getAmount()));
                cell.setFont(fontPlain);

                cell = rowData.createCell(16, invoice.getCheque_id());
                cell.setFont(fontPlain);

                if (invoice.getType() == 1) {
                    cell = rowData.createCell(20, "Buy Invoice");
                } else {
                    cell = rowData.createCell(20, "Sell Invoice");
                }

                cell.setFont(fontPlain);
            }
        }

        baseTable.draw();
    }

    private void addFooterAndBorder(PDDocument document, PDRectangle rect) throws IOException {
        // get all number of pages.

        int numberOfPages = document.getNumberOfPages();
        String text = "Product of SKYSYS software solutions";

        for (int i = 0; i < numberOfPages; i++) {
            PDPage fpage = document.getPage(i);

            // content stream to write content in pdf page.
            PDPageContentStream contentStream = new PDPageContentStream(document, fpage, PDPageContentStream.AppendMode.APPEND, true);
            PDStreamUtils.write(contentStream, text,
                    PDType1Font.HELVETICA, 10, (rect.getWidth() - fontPlain.getStringWidth(text) / 100) / 2, 30, Color.GRAY);//set stayle and size
            PDStreamUtils.write(contentStream, String.valueOf(i + 1),
                    PDType1Font.HELVETICA, 10, rect.getWidth() - 40, 30, Color.GRAY);

            // Add a page border
            contentStream.addRect(15, 15, rect.getWidth() - 30, rect.getHeight() - 30);
            contentStream.setLineWidth(2);
            contentStream.setStrokingColor(Color.BLACK);
            contentStream.stroke();

            contentStream.close();

        }
    }

    private void fillShopCombo() throws SQLException {
        ArrayList<Shop> allShops = Shop.getAll();
        for (Shop shop : allShops) {
            shop_list.getItems().add(shop.getName());
        }
    }

    // time based check box selection action
    public void enableTimeSelections() {
        from_date.setValue(null);
        to_date.setValue(null);
        if (time_reports_check.isSelected()) {
            from_date.setDisable(false);
            to_date.setDisable(false);
            reportController.activateExecuteButton++;
        } else {
            from_date.setDisable(true);
            to_date.setDisable(true);
            reportController.selectedFromDate = "";
            reportController.selectedToDate = "";
            reportController.activateExecuteButton--;
        }

        if (reportController.activateExecuteButton == -2) {
            execute_query.setDisable(true);
        } else {
            execute_query.setDisable(false);
        }
    }

    // shop based check box selection action
    public void enableShopSelections() {
        shop_list.getSelectionModel().clearSelection();
        if (shop_reports_check.isSelected()) {
            shop_list.setDisable(false);
            reportController.activateExecuteButton++;
        } else {
            shop_list.setDisable(true);
            reportController.selectedShopId = -1;
            reportController.activateExecuteButton--;
        }

        if (reportController.activateExecuteButton == -2) {
            execute_query.setDisable(true);
        } else {
            execute_query.setDisable(false);
        }
    }

    // total profit count
    public double totalProfit() {
        double buy = 0;
        double sell = 0;

        if (shop_reports_check.isSelected() && time_reports_check.isSelected()) {
            for (Invoice invoice : invoiceDataByShopAndDate) {
                if (invoice.getType() == 1) {
                    buy += invoice.getAmount();
                } else if (invoice.getType() == 2) {
                    sell += invoice.getAmount();
                }
            }
        } else if (shop_reports_check.isSelected()) {
            for (Invoice invoice : invoiceDataByShop) {
                if (invoice.getType() == 1) {
                    buy += invoice.getAmount();
                } else if (invoice.getType() == 2) {
                    sell += invoice.getAmount();
                }
            }
        } else if (time_reports_check.isSelected()) {
            for (Invoice invoice : invoiceDataByDate) {
                if (invoice.getType() == 1) {
                    buy += invoice.getAmount();
                } else if (invoice.getType() == 2) {
                    sell += invoice.getAmount();
                }
            }
        }
        return sell - buy;
    }
}