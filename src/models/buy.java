package models;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author danushka
 */
public class buy {
    Connection con = connection.getConnection();
    String item_id,day,paymentDate,check_no,name;
    int quantity;
    float buyPrice,sellPrice,total;
    public buy(String item_id, int quantity, float buyPrice, float sellPrice, String day, String paymentDate, String check_no) throws SQLException {
        this.item_id = item_id;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellPrice= sellPrice;
        this.day = day;
        this.paymentDate = paymentDate;
        this.check_no = check_no;
        this.total = buyPrice*quantity;
        this.name = item.getItem(this.item_id).getName();
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public float getSellPrice() {
        return sellPrice;
    }

    public float getBuyPrice() {
        return buyPrice;
    }

    public float getTotal() {
        return total;
    }

    public String getCheck_no() {
        return check_no;
    }

    public String getDay() {
        return day;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setSellPrice(float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBuyPrice(float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setCheck_no(String check_no) {
        this.check_no = check_no;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() throws SQLException {
        String insertquery = "INSERT INTO item_buy(item_id,quantity,buyPrice,sellPrice,day,total_bill,payment_date,check_no) VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement insq = con.prepareStatement(insertquery);
        insq.setString(1,this.item_id);
        insq.setInt(2,this.quantity);
        insq.setFloat(3,this.buyPrice);
        insq.setFloat(4,this.sellPrice);
        insq.setString(5,this.day);
        insq.setFloat(6,this.total);
        insq.setString(7,this.paymentDate);
        insq.setString(8,this.check_no);
        insq.execute();

        item newItem = item.getItem(this.item_id);
        if(newItem.getSellPrice()!=this.sellPrice){
            newItem.setSellPrice(this.sellPrice);
        }
        if(newItem.getBuyPrice()!=this.buyPrice){
            newItem.setBuyPrice(this.buyPrice);
        }
        int newQuantity = newItem.getQuantity()+this.quantity;
        newItem.setQuantity(newQuantity);
        newItem.update();
        con.close();
    }
    public static ArrayList<buy> getAll() throws SQLException {
        Connection con = connection.getConnection();
        ArrayList<buy> buyList = new ArrayList<>();
        String selectq = "SELECT * FROM item_buy";
        Statement select = con.createStatement();
        ResultSet result = select.executeQuery(selectq);
        while (result.next()){
            String item_id = result.getString("item_id");
            int quentity = result.getInt("quantity");
            float buyPrice = result.getFloat("buyPrice");
            float sellPrice = result.getFloat("sellPrice");
            String day = result.getString("day");
            String payment_date = result.getString("payment_date");
            String check_no = result.getString("check_no");
            buyList.add(new buy(item_id,quentity,buyPrice,sellPrice,day,payment_date,check_no));
        }
        con.close();
        return buyList;
    }

}
