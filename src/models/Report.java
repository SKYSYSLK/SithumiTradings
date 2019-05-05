package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Report {

    private String bank, branch, issueDate, expireDate, type;
//    private Connection con;

    public Report() {
    }

    public static ArrayList<Invoice> getInvoicesByShopAndIssuedDate(int shopId, String from_date, String to_date) throws SQLException {
        Connection con = connection.getConnection();
        ArrayList<Invoice> allRecords = new ArrayList<>();
        String query = "SELECT * FROM invoices WHERE shop_id = ? AND date_issued >= ? AND date_issued <= ?";
        PreparedStatement selectq = con.prepareStatement(query);
        selectq.setInt(1, shopId);
        selectq.setString(2, from_date);
        selectq.setString(3, to_date);
        ResultSet result = selectq.executeQuery();
        while (result.next()) {
            String id = result.getString("id");
            Double amount = result.getDouble("amount");
            String date_issue = result.getString("date_issued");
            String cheque_id = result.getString("cheque_id");
            int type = result.getInt("type");
            allRecords.add(new Invoice(id, shopId, date_issue, amount, cheque_id, type));
        }
        con.close();
        return allRecords;
    }

    public static ArrayList<Invoice> getInvoicesByShop(int shopId) throws SQLException {
        Connection con = connection.getConnection();
        ArrayList<Invoice> allRecords = new ArrayList<>();
        String query = "SELECT * FROM invoices WHERE shop_id = ?";
        PreparedStatement selectq = con.prepareStatement(query);
        selectq.setInt(1, shopId);
        ResultSet result = selectq.executeQuery();
        while (result.next()) {
            String id = result.getString("id");
            Double amount = result.getDouble("amount");
            String date_issue = result.getString("date_issued");
            String cheque_id = result.getString("cheque_id");
            int type = result.getInt("type");
            allRecords.add(new Invoice(id, shopId, date_issue, amount, cheque_id, type));
        }
        con.close();
        return allRecords;
    }

    public static ArrayList<Invoice> getInvoicesByDate(String from_date, String to_date) throws SQLException {
        Connection con = connection.getConnection();
        ArrayList<Invoice> allRecords = new ArrayList<>();
        String query = "SELECT * FROM invoices WHERE date_issued >= ? AND date_issued <= ?";
        PreparedStatement selectq = con.prepareStatement(query);
        selectq.setString(1, from_date);
        selectq.setString(2, to_date);
        ResultSet result = selectq.executeQuery();
        while (result.next()) {
            String id = result.getString("id");
            int shopId = result.getInt("shop_id");
            Double amount = result.getDouble("amount");
            String date_issue = result.getString("date_issued");
            String cheque_id = result.getString("cheque_id");
            int type = result.getInt("type");
            allRecords.add(new Invoice(id, shopId, date_issue, amount, cheque_id, type));
        }
        con.close();
        return allRecords;
    }
}
