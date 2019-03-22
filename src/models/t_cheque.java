package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class t_cheque {
    private String id,bank,branch,issueDate,expireDate,type;
    private double amount;
    private Connection con = connection.getConnection();

    public t_cheque(String id, String bank, String branch, String issueDate, String expireDate, String type, double amount) {
        this.id = id;
        this.bank = bank;
        this.branch = branch;
        this.issueDate = issueDate;
        this.expireDate = expireDate;
        this.type = type;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static ArrayList<t_cheque> getAll() throws SQLException {
        ArrayList<Cheque> allRec = Cheque.getAll();
        ArrayList<t_cheque> currentAll = new ArrayList<>();
        for(Cheque item:allRec){
            String cType = "";
            switch (item.getType()){
                case 1: cType = "Due"; break;
                case 2: cType = "Issued";break;
                case 3: cType = "Settled";break;
            }
            currentAll.add(new t_cheque(item.getId(),item.getBankName(),item.getBranchName(),item.getIssuedDate(),item.getExpireDate(),cType,item.getAmout()));
        }
        return currentAll;
    }

    public void delete() throws SQLException {
        String query = "DELETE FROM cheques WHERE id = ?";
        PreparedStatement delq = con.prepareStatement(query);
        delq.setString(1,this.id);
        delq.execute();
        con.close();
    }

    public void update() throws SQLException {
        int chequeType=-1;
        switch (this.type) {
            case "Due":
                chequeType = 1;
                break;
            case "Issued":
                chequeType = 2;
                break;
            case "Settled":
                chequeType = 3;
                break;
        }
        Cheque currentCheque = Cheque.getCheque(this.id);
        currentCheque.setAmout(this.amount);
        currentCheque.setBankName(this.bank);
        currentCheque.setBranchName(this.branch);
        currentCheque.setExpireDate(this.expireDate);
        currentCheque.setIssuedDate(this.issueDate);
        currentCheque.setType(chequeType);
        currentCheque.update();
    }
    public static ArrayList<t_cheque> getRecent() throws SQLException {
        ArrayList<Cheque> allRec = Cheque.getAll();
        ArrayList<t_cheque> currentAll = new ArrayList<>();
        for(Cheque item:allRec){
            String cType = "";
            switch (item.getType()){
                case 1: cType = "Due"; break;
                case 2: cType = "Issued";break;
                case 3: cType = "Settled";break;
            }
            if(item.getType()==2)
            currentAll.add(new t_cheque(item.getId(),item.getBankName(),item.getBranchName(),item.getIssuedDate(),item.getExpireDate(),cType,item.getAmout()));
        }
        return currentAll;
    }
}
