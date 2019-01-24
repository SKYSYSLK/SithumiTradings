package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author danushka
 */
public class daily {
    Connection con=connection.getConnection();
    private String item_no,day;
    private int quantity;
    private float sale_price,income,profit;

    daily(String day,String item_no,int quantity, float sale_price, float income, float profit){
        this.day = day;
        this.item_no = item_no;
        this.quantity = quantity;
        this.sale_price = sale_price;
        this.profit = profit;
        this.income = income;
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
    }
}
