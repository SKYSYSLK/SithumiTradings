package controllers;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.page.DefaultPageProvider;
import be.quodlibet.boxable.page.PageProvider;
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
        createReportTableSection(document, page, rect);
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

    private void createReportTableSection(PDDocument document, PDPage page, PDRectangle rect) throws IOException {
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