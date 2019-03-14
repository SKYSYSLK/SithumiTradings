package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Invoice {
    String id,date_issue,shop_name,cheque_id;
    int shop_id;
    double amount;
    Connection con = connection.getConnection();

    public String getCheque_id() {
        return cheque_id;
    }

    public void setCheque_id(String cheque_id) {
        this.cheque_id = cheque_id;
    }

    public Invoice(String id, int shop_id, String date_issue, double amount, String cheque_id) {
        this.id = id;
        this.shop_id = shop_id;
        this.date_issue = date_issue;
        this.amount = amount;
        this.shop_name = "";
        this.cheque_id = cheque_id;
    }
    // This constructor is for add a shop_name
    public Invoice(String id, int shop_id,String shop_name, String date_issue, double amount, String cheque_id) {
        this.id = id;
        this.shop_id = shop_id;
        this.date_issue = date_issue;
        this.amount = amount;
        this.shop_name = shop_name;
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

    public void save(String id, String shop_id, Double amount, String date_issue, String cheque_id) throws SQLException {
        String query = "INSERT INTO invoices(id,shop_id,amount,date_issued,cheque_id)" +
                "VALUES(?,?,?,?,?)";
        PreparedStatement insq = con.prepareStatement(query);
        insq.setString(1,id);
        insq.setString(2,shop_id);
        insq.setDouble(3,amount);
        insq.setString(4,date_issue);
        insq.setString(5,cheque_id);
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
            String shopName = Shop.getShopName(shop_id);
            allRecords.add(new Invoice(id,shop_id,shopName,date_issue,amount,cheque_id));
        }
        con.close();
        return allRecords;
    }

}
