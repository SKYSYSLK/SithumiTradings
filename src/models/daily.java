package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author danushka
 */
public class daily {
    Connection con=connection.getConnection();
    private String item_no,day,name;
    private int quantity;
    private float sale_price,income,profit;

    public daily(String day, String item_no, int quantity, float sale_price, float income, float profit) throws SQLException {
        this.day = day;
        this.item_no = item_no;
        this.quantity = quantity;
        this.sale_price = sale_price;
        this.profit = profit;
        this.income = income;
        this.name = Objects.requireNonNull(item.getItem(this.item_no)).getName();
    }

    public static ArrayList<daily> getAll(String date) throws SQLException {
        Connection con = connection.getConnection();
        ArrayList<daily> allRecords = new ArrayList<>();
        String query = "SELECT * FROM daily WHERE day=?";
        PreparedStatement selectq = con.prepareStatement(query);
        selectq.setString(1,date);
        ResultSet result = selectq.executeQuery();
        while (result.next()){
            String day = result.getString("day");
            String item_no = result.getString("item_no");
            int quantity = result.getInt("quantity");
            float sale_price = result.getFloat("sale_price");
            float profit = result.getFloat("profit");
            float income = result.getFloat("income");
            allRecords.add(new daily(day,item_no,quantity,sale_price,income,profit));
        }
        con.close();
        return allRecords;
    }

    public String getDay() {
        return day;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getIncome() {
        return income;
    }

    public float getProfit() {
        return profit;
    }

    public float getSale_price() {
        return sale_price;
    }

    public String getItem_no() {
        return item_no;
    }

    public String getName() {
        return name;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public void setSale_price(float sale_price) {
        this.sale_price = sale_price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() throws SQLException {
        String insq = "INSERT INTO daily (item_no,quantity,day,sale_price,income,profit) VALUES (?,?,?,?,?,?)";
        PreparedStatement insert = con.prepareStatement(insq);
        insert.setString(1,this.item_no);
        insert.setFloat(4,this.sale_price);
        insert.setInt(2,this.quantity);
        insert.setString(3,this.day);
        insert.setFloat(5,this.income);
        insert.setFloat(6,this.profit);
        insert.execute();
        con.close();
    }

    public void delete() throws SQLException {
        String query = "DELETE FROM daily where item_no=? AND day=?";
        PreparedStatement delq = con.prepareStatement(query);
        delq.setString(1,this.item_no);
        delq.setString(2,this.day);
        delq.execute();
    }
}
