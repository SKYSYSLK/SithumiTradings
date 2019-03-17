package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class t_invoice {
    private String id, shopName, dateIsssued, cheque_number;
    private double amount;
    private Connection con= connection.getConnection();

    public t_invoice(String id, String shopName, String dateIsssued, String cheque_number, double amount) {
        this.id = id;
        this.shopName = shopName;
        this.dateIsssued = dateIsssued;
        this.cheque_number = cheque_number;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDateIsssued() {
        return dateIsssued;
    }

    public void setDateIsssued(String dateIsssued) {
        this.dateIsssued = dateIsssued;
    }

    public String getCheque_number() {
        return cheque_number;
    }

    public void setCheque_number(String cheque_number) {
        this.cheque_number = cheque_number;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static ArrayList<t_invoice> getAll() throws SQLException {
        ArrayList<Invoice> allInv = Invoice.getAll();
        ArrayList<t_invoice> allRec = new ArrayList<>();
        for(Invoice invoice: allInv){
            String id = invoice.getId();
            String shopName = Shop.getShopName(invoice.getShop_id());
            double amount = invoice.getAmount();
            String date_issue = invoice.getDate_issue();
            String chequeNo = invoice.getCheque_id();
            allRec.add(new t_invoice(id,shopName,date_issue,chequeNo,amount));
        }
        return allRec;
    }

    public void delete() throws SQLException {
        Invoice current = Invoice.getInvoice(this.id);
        current.delete();
    }

}
