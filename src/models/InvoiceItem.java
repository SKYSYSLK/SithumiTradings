package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InvoiceItem {
    private String itemId, invoiceId;
    private double buyPrice,sellPrice;
    private int quantity;

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    private Connection con=connection.getConnection();

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public InvoiceItem(String itemId, String invoiceId, double buyPrice, double sellPrice, int quantity) {
        this.itemId = itemId;
        this.invoiceId = invoiceId;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public static ArrayList<InvoiceItem> getItems(String invoiceId) throws SQLException {
        Connection con = connection.getConnection();
        ArrayList<InvoiceItem> allRec = new ArrayList<>();
        String query = "SELECT * FROM invoiceItems WHERE invoice_id = ?";
        PreparedStatement selectq = con.prepareStatement(query);
        selectq.setString(1,invoiceId);
        ResultSet resultSet = selectq.executeQuery();
        while (resultSet.next()){
            String itemId = resultSet.getString("item_id");
            double buyPrice = resultSet.getDouble("buyPrice");
            double sellPrice = resultSet.getDouble("sellPrice");
            int quantity = resultSet.getInt("quantity");
            InvoiceItem current = new InvoiceItem(itemId,invoiceId,buyPrice,sellPrice,quantity);
            allRec.add(current);
        }
        return allRec;
    }

}
