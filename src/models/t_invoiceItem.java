package models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class t_invoiceItem {
    private String itemNo, name;
    private int quantity;
    private double sellPrice, buyPrice;

    public t_invoiceItem(String itemNo, String name, int quantity, double sellPrice, double buyPrice) {
        this.itemNo = itemNo;
        this.name = name;
        this.quantity = quantity;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public static ArrayList<t_invoiceItem> getItems(String invoice_id) throws SQLException {
        ArrayList<t_invoiceItem> allRec = new ArrayList<>();
        ArrayList<InvoiceItem> itemList = InvoiceItem.getItems(invoice_id);
        for (InvoiceItem item:itemList){
            String item_id = item.getItemId();
            double sellPrice = item.getSellPrice();
            double buyPrice = item.getBuyPrice();
            int quantity = item.getQuantity();
            String itemName = Objects.requireNonNull(Item.getItem(item_id)).getName();
            allRec.add(new t_invoiceItem(item_id,itemName,quantity, sellPrice, buyPrice));
        }
        return allRec;
    }
}
