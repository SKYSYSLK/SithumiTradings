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

    public void generateShopBasedReport(int shopId) {

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
//
//    public void update() throws SQLException {
//        String upQuery = "UPDATE shops SET name=?,contact=?,address=?,type=? WHERE id=?";
//        PreparedStatement upq = con.prepareStatement(upQuery);
//        upq.setInt(5,this.id);
//        upq.setString(1,this.name);
//        upq.setString(2,this.getContact());
//        upq.setString(3,this.getAddress());
//        upq.setInt(4,this.getType());
//        upq.execute();
//        con.close();
//    }
//
//    public static String getShopName(int id) throws SQLException {
//        Connection con = connection.getConnection();
//        String quey = "SELECT name FROM shops WHERE id = ? LIMIT 1";
//        PreparedStatement selectq = con.prepareStatement(quey);
//        selectq.setInt(1,id);
//        ResultSet resultSet = selectq.executeQuery();
//        String name = resultSet.getString("name");
//        con.close();
//        return name;
//    }
//
//    public static int getShopId(String name) throws SQLException {
//        Connection con = connection.getConnection();
//        String query = "SELECT id FROM shops WHERE name=?";
//        PreparedStatement selectq = con.prepareStatement(query);
//        selectq.setString(1,name);
//        ResultSet resultSet = selectq.executeQuery();
//        int id = resultSet.getInt("id");
//        con.close();
//        return id;
//    }
}
