package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Invoice {
    private String id,date_issue,shop_name,cheque_id;
    private int shop_id;

    private int type;
    private double amount;
    private Connection con = connection.getConnection();

    public Invoice(String id, int shop_id, String date_issue, double amount, String cheque_id, int type) {
        this.id = id;
        this.shop_id = shop_id;
        this.date_issue = date_issue;
        this.amount = amount;
        this.shop_name = "";
        this.cheque_id = cheque_id;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCheque_id() {
        return cheque_id;
    }

    public void setCheque_id(String cheque_id) {
        this.cheque_id = cheque_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getDate_issue() {
        return date_issue;
    }

    public void setDate_issue(String date_issue) {
        this.date_issue = date_issue;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void save() throws SQLException {
        String query = "INSERT INTO invoices(id,shop_id,amount,date_issued,cheque_id)" +
                "VALUES(?,?,?,?,?)";
        PreparedStatement insq = con.prepareStatement(query);
        insq.setString(1,this.id);
        insq.setInt(2,this.shop_id);
        insq.setDouble(3,this.amount);
        insq.setString(4,this.date_issue);
        insq.setString(5,this.cheque_id);
        insq.execute();
        con.close();
    }

    public static ArrayList<Invoice> getAll() throws SQLException {
        Connection con = connection.getConnection();
        ArrayList<Invoice> allRecords = new ArrayList<>();
        String query = "SELECT * FROM invoices LIMIT 10";
        PreparedStatement selectq = con.prepareStatement(query);
        ResultSet result = selectq.executeQuery();
        while (result.next()){
            String id = result.getString("id");
            int shop_id = result.getInt("shop_id");
            Double amount = result.getDouble("amount");
            String date_issue = result.getString("date_issued");
            String cheque_id = result.getString("cheque_id");
            int type = result.getInt("type");
            allRecords.add(new Invoice(id,shop_id,date_issue,amount,cheque_id,type));
        }
        con.close();
        return allRecords;
    }

    public void update() throws SQLException {
        String query = "UPDATE invoices SET shop_id = ?, amount = ?, date_issued = ?, cheque_id=?, type = ? " +
                "WHERE id = ?";
        PreparedStatement upq = con.prepareStatement(query);
        upq.setInt(1,this.shop_id);
        upq.setDouble(2,this.amount);
        upq.setString(3,date_issue);
        upq.setString(4,cheque_id);
        upq.setInt(5,this.type);
        upq.execute();
        con.close();
    }

    public static Invoice getInvoice(String invoice_id) throws SQLException {
        Connection con = connection.getConnection();
        String query = "SELECT * FROM invoices WHERE id = ? LIMIT 1";
        PreparedStatement selecq = con.prepareStatement(query);
        selecq.setString(1,invoice_id);
        ResultSet resultSet = selecq.executeQuery();

        String invId = resultSet.getString("id");
        int invShopId = resultSet.getInt("shop_id");
        double invAmount = resultSet.getDouble("amount");
        String invDateIssue = resultSet.getString("date_issued");
        String invCheque_id = resultSet.getString("cheque_id");
        int invType = resultSet.getInt("type");
        con.close();
        System.out.println(invId);
        return new Invoice(invId,invShopId,invDateIssue,invAmount,invCheque_id,invType);

    }

}
