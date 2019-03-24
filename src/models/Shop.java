package models;

import jdk.nashorn.internal.ir.Expression;

import java.sql.*;
import java.util.ArrayList;

public class Shop {
    private int id,type;
    private String name,contact,address;
    private Connection con = connection.getConnection();

    public Shop(int id, int type, String name, String contact, String address) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    public Shop(int type, String name, String contact, String address) {
        this.type = type;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.con = con;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void save() throws SQLException {
        String query = "INSERT INTO shops(id, name, contact, address, type ) VALUES (?,?,?,?,?)";
        PreparedStatement insq = con.prepareStatement(query);
        insq.setInt(1,this.id);
        insq.setString(2,this.name);
        insq.setString(3,this.contact);
        insq.setString(4,this.address);
        insq.setInt(5,this.type);

        insq.execute();
        con.close();
    }
    public void delete() throws SQLException {
        String query = "DELETE FROM shops WHERE id=?";
        PreparedStatement insq = con.prepareStatement(query);
        insq.setInt(1,this.id);
        insq.execute();
        con.close();
    }

    public static ArrayList<Shop> getAll() throws SQLException {
        ArrayList<Shop> shops = new ArrayList<>();
        Connection con = connection.getConnection();
        String selectQuery = "SELECT * FROM shops";
        Statement select = con.createStatement();
        ResultSet result = select.executeQuery(selectQuery);
        while (result.next()){
            shops.add(new Shop(
                    result.getInt("id"),
                    result.getInt("type"),
                    result.getString("name"),
                    result.getString("contact"),
                    result.getString("address")
            ));
        }
        con.close();
        return shops;
    }


    public void update() throws SQLException {
        String upQuery = "UPDATE shops SET id=?,name=?,contact=?,address=?,type=? WHERE id=?";
        PreparedStatement upq = con.prepareStatement(upQuery);
        upq.setInt(1,this.id);
        upq.setString(2,this.name);
        upq.setString(3,this.contact);
        upq.setString(4,this.address);
        upq.setInt(5,this.type);
        upq.setInt(6,this.id);
        upq.execute();
        con.close();
    }


    public static String getShopName(int id) throws SQLException {
        Connection con = connection.getConnection();
        String quey = "SELECT name FROM shops WHERE id = ? LIMIT 1";
        PreparedStatement selectq = con.prepareStatement(quey);
        selectq.setInt(1,id);
        ResultSet resultSet = selectq.executeQuery();
        try {
            String name = resultSet.getString("name");
            con.close();
            return name;
        }
        catch(Exception e){
            String name="ERR: Shop Name not found!";
            con.close();
            return name;
        }

    }

    public static int getShopId(String name) throws SQLException {
        Connection con = connection.getConnection();
        String query = "SELECT id FROM shops WHERE name=?";
        PreparedStatement selectq = con.prepareStatement(query);
        selectq.setString(1,name);
        ResultSet resultSet = selectq.executeQuery();
        int id = resultSet.getInt("id");
        con.close();
        return id;
    }
}
