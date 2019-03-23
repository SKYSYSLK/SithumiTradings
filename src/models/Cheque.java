package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Cheque {
    private String id, issuedDate, expireDate, bankName, branchName;
    private double amout;
    private Connection con = connection.getConnection();
    private int type;

    public Cheque(String id, String issuedDate, String expireDate, String bankName, String branchName, double amout, int type) {
        this.id = id;
        this.issuedDate = issuedDate;
        this.expireDate = expireDate;
        this.bankName = bankName;
        this.branchName = branchName;
        this.amout = amout;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        insq.setString(6,this.branchName);
        insq.setString(5,this.expireDate);
        insq.setString(4,this.issuedDate);
        insq.setString(7,"1");
        insq.execute();
        con.close();
        System.out.println(insq.toString());
        // Need to add a success window
    }
    public static ArrayList<Cheque> getAll() throws SQLException {
        Connection con = connection.getConnection();
        ArrayList<Cheque> allRec = new ArrayList<>();
        String query = "SELECT * FROM cheques";
        PreparedStatement selectq = con.prepareStatement(query);
        ResultSet resultSet = selectq.executeQuery();
        while (resultSet.next()){
            String id = resultSet.getString("id");
            double amount = resultSet.getDouble("amount");
            String bank = resultSet.getString("bank");
            String branch = resultSet.getString("branch");
            String expireDate = resultSet.getString("expire_date");
            String issuedDate = resultSet.getString("issue_date");
            int status = resultSet.getInt("status");
            Cheque current = new Cheque(id,issuedDate,expireDate,bank,branch,amount,status);
            allRec.add(current);
        }
        con.close();
        return allRec;
    }

    public static ArrayList<Cheque> getAll(int type) throws SQLException {
        Connection con = connection.getConnection();
        ArrayList<Cheque> allRec = new ArrayList<>();
        String query = "SELECT * FROM cheques WHERE status=?";
        PreparedStatement selectq = con.prepareStatement(query);
        selectq.setInt(1,type);
        ResultSet resultSet = selectq.executeQuery();
        while (resultSet.next()){
            String id = resultSet.getString("id");
            double amount = resultSet.getDouble("amount");
            String bank = resultSet.getString("bank");
            String branch = resultSet.getString("branch");
            String expireDate = resultSet.getString("expire_date");
            String issuedDate = resultSet.getString("issue_date");
            int status = resultSet.getInt("status");
            Cheque current = new Cheque(id,issuedDate,expireDate,bank,branch,amount,status);
            allRec.add(current);
        }
        con.close();
        return allRec;
    }

    public void update() throws SQLException {
        String query = "UPDATE cheques SET amount=?, bank=?, issue_date=?, expire_date=?, branch=?, status=?" +
                "WHERE id=?";
        PreparedStatement upq = con.prepareStatement(query);
        upq.setDouble(1,this.amout);
        upq.setString(2,this.bankName);
        upq.setString(3,this.issuedDate);
        upq.setString(4,this.expireDate);
        upq.setString(5,this.branchName);
        upq.setInt(6,this.type);
        upq.setString(7,this.id);
        upq.execute();
        con.close();
    }

    public static Cheque getCheque(String id) throws SQLException {
        Connection con = connection.getConnection();
        String query = "SELECT * FROM cheques WHERE id=?";
        PreparedStatement selectq = con.prepareStatement(query);
        selectq.setString(1,id);
        ResultSet resultSet = selectq.executeQuery();
        double amount = resultSet.getDouble("amount");
        String bank = resultSet.getString("bank");
        String branch = resultSet.getString("branch");
        String issueDate = resultSet.getString("issue_date");
        String expireDate = resultSet.getString("expire_date");
        int type = resultSet.getInt("status");
        con.close();
        return new Cheque(id,issueDate,expireDate,bank,branch,amount,type);
    }
}
