//package models;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//
//
//public class sell {
//    private String id, shopName, dateIsssued, cheque_number;
//    private double amount;
//    private int type;
//    private Connection con= connection.getConnection();
//
//
//    public sell(String id, String shopName, String dateIsssued, String cheque_number, double amount) {
//        this.id = id;
//        this.shopName = shopName;
//        this.dateIsssued = dateIsssued;
//        this.cheque_number = cheque_number;
//        this.amount = amount;
//    }
//
//    public static ArrayList<sell> getAll() throws SQLException {
//        ArrayList<Invoice> allInv = Invoice.getAll(2);
//        ArrayList<sell> allRec = new ArrayList<>();
//        for(Invoice invoice: allInv){
//            String id = invoice.getId();
//            String shopName = Shop.getShopName(invoice.getShop_id());
//            double amount = invoice.getAmount();
//            String date_issue = invoice.getDate_issue();
//            String chequeNo = invoice.getCheque_id();
//            allRec.add(new sell(id,shopName,date_issue,chequeNo,amount));
//        }
//        return allRec;
//    }
//
//
//
//
//}
