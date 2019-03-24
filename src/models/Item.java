package models;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author danushka
 */
public class Item {
    private Connection con = connection.getConnection();
    private String id,name;
    private int quantity;
    private float buyPrice,sellPrice;
    public Item(String id, String name, int quantity, float buyPrice, float sellPrice){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getBuyPrice() {
        return buyPrice;
    }

    public float getSellPrice() {
        return sellPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setBuyPrice(float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSellPrice(float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void save() throws SQLException {
        String inserQuery = "INSERT INTO items(id,name,quantity, buyPrice, sellPrice, available)"+
                "VALUES(?,?,?,?,?,?)";
        PreparedStatement insq = con.prepareStatement(inserQuery);
        insq.setString(1,this.getId());
        insq.setString(2,this.getName());
        insq.setInt(3,this.getQuantity());
        insq.setFloat(4,this.getBuyPrice());
        insq.setFloat(5,this.getSellPrice());
        insq.setInt(6,1);
        insq.execute();
        con.close();
    }
    public static ArrayList<Item> getAll() throws SQLException {
        ArrayList<Item> items = new ArrayList<>();
        Connection con = connection.getConnection();
        String selectQuery = "SELECT * FROM items";
        Statement select = con.createStatement();
        ResultSet result = select.executeQuery(selectQuery);
        while (result.next()){
            items.add(new Item(
                    result.getString("id"),
                    result.getString("name"),
                    result.getInt("quantity"),
                    result.getFloat("buyPrice"),
                    result.getFloat("sellPrice")
            ));
        }
        con.close();
        return items;
    }
    public void update() throws SQLException {
        String upQuery = "UPDATE items SET name=?,quantity=?,buyPrice=?,sellPrice=?,available=? WHERE id=?";
        System.out.println("FROM update(): " + this.getQuantity());
        PreparedStatement upq = con.prepareStatement(upQuery);
        upq.setString(6,this.id);
        upq.setString(1,this.name);
        upq.setInt(2,this.getQuantity());
        upq.setFloat(3,this.getBuyPrice());
        upq.setFloat(4,this.getSellPrice());
        upq.setInt(5,1);
        upq.execute();
        con.close();
    }
    public static Item getItem(String itemId) throws SQLException {
        Connection con = connection.getConnection();
        String findQuery = "SELECT * FROM items WHERE id=?";
        PreparedStatement select = con.prepareStatement(findQuery);
        select.setString(1,itemId);
        ResultSet result = select.executeQuery();
        if (result.next()) {
            String name = result.getString("name");
            int quantity = result.getInt("quantity");
            float buyPrice = result.getFloat("buyPrice");
            float sellPrice = result.getFloat("sellPrice");
            con.close();
            return new Item(itemId, name, quantity, buyPrice, sellPrice);
        }else{
            con.close();
            return null;
        }
    }
    public void addAmount(int amount) throws SQLException {
        int allQuantity = this.quantity + amount;
        System.out.println(this.id + " Current stock: " +this.quantity);
        this.quantity = allQuantity;
        String query = "UPDATE items SET Quantity = ? WHERE id = ?";
        PreparedStatement upq = con.prepareStatement(query);
        upq.setInt(1,allQuantity);
        upq.setString(2,this.id);
        upq.execute();
        con.close();
        System.out.println("Queried Stock on " + this.id +" by " + amount + "New Stock: "+ this.quantity);
    }
}
