package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cheque {
    private String id, issuedDate, expireDate, bankName, branchName;
    private double amout;
    private Connection con = connection.getConnection();

    public Cheque(String id, String issuedDate, String expireDate, String bankName, String branchName, double amout) {
        this.id = id;
        this.issuedDate = issuedDate;
        this.expireDate = expireDate;
        this.bankName = bankName;
        this.branchName = branchName;
        this.amout = amout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public double getAmout() {
        return amout;
    }

    public void setAmout(double amout) {
        this.amout = amout;
    }

    public void save()
            throws SQLException {
        String query = "INSERT INTO cheques(id, amount, bank, issue_date, expire_date, branch, status) VALUES " +
                "(?,?,?,?,?,?,?)";
        PreparedStatement insq = con.prepareStatement(query);
        insq.setString(1,this.id);
        insq.setDouble(2,this.amout);
        insq.setString(3,this.bankName);
        insq.setString(4,this.branchName);
        insq.setString(5,this.expireDate);
        insq.setString(6,this.issuedDate);
        insq.setString(7,"1");
        insq.execute();
        System.out.println(insq.toString());
        // Need to add a success window
    }
}
