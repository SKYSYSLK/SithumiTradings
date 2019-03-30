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
    private Connection con=connection.getConnection();

    public InvoiceItem(String itemId, String invoiceId, double buyPrice, double sellPrice, int quantity) {
        this.itemId = itemId;
        this.invoiceId = invoiceId;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
    }

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


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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
//        System.out.println(query);
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
        con.close();
//        System.out.println(allRec);
        return allRec;
    }

    public void save() throws SQLException {
        String query = "INSERT INTO invoiceItems(item_id, invoice_id, quantity, buyPrice, sellPrice) VALUES(?,?,?,?,?)";
        PreparedStatement insq = con.prepareStatement(query);
        insq.setString(1,this.itemId);
        insq.setString(2,this.invoiceId);
        insq.setInt(3,this.quantity);
        insq.setFloat(4, (float) this.buyPrice);
        insq.setFloat(5, (float) this.sellPrice);
        insq.execute();
        con.close();
//        System.out.println("query");
    }

    public void update() throws SQLException {
        String query = "UPDATE invoiceItems set quantity = ?, buyPrice = ?, sellPrice = ? WHERE item_id = ? AND invoice_id = ?";

        InvoiceItem current = InvoiceItem.getItem(this.invoiceId,this.itemId);
        int quantityChange = this.quantity-current.getQuantity();

        PreparedStatement upq = con.prepareStatement(query);
        upq.setInt(1,this.quantity);
        upq.setDouble(2,this.buyPrice);
        upq.setDouble(3,this.sellPrice);
        upq.setString(4,this.itemId);
        upq.setString(5,this.invoiceId);
        upq.execute();
        con.close();

        // Update new Quantity
        Item currentItem = Item.getItem(itemId);
        assert currentItem != null;
        int newQuantity = currentItem.getQuantity()+quantityChange;
        currentItem.setQuantity(newQuantity);
        currentItem.setSellPrice((float) this.sellPrice);
        currentItem.setBuyPrice((float)this.buyPrice);
        currentItem.update();

//        System.out.println("itemId: "+itemId+" invoice_id: "+invoiceId+" sell: "+this.sellPrice+" buy "+this.buyPrice
//        +" quantity "+this.quantity);
    }

    public static InvoiceItem getItem(String invoiceId, String itemId) throws SQLException {
        Connection con = connection.getConnection();
        String query = "SELECT * FROM invoiceItems where invoice_id=? AND item_id=? LIMIT 1";
        PreparedStatement selectq = con.prepareStatement(query);
        selectq.setString(1,invoiceId);
        selectq.setString(2,itemId);
        ResultSet result = selectq.executeQuery();
        int quantity = result.getInt("quantity");
        double sellPrice = result.getDouble("sellPrice");
        double buyPrice = result.getDouble("buyPrice");
        con.close();
        return new InvoiceItem(itemId,invoiceId,buyPrice,sellPrice,quantity);
    }

    public void delete() throws SQLException {
        String query = "DELETE FROM invoiceItems WHERE invoice_id=? AND item_id = ?";
        PreparedStatement delq = con.prepareStatement(query);
        delq.setString(1,this.invoiceId);
        delq.setString(2,this.itemId);
        delq.execute();
        con.close();
    }
}
