package controllers;

import models.t_invoiceItem;
import java.sql.SQLException;
import java.util.ArrayList;

public class itemcalculated {
    private int quantity;
    private double itemProfit, Total, sellPrice;
    private String name;
    private String itemNo;

    public itemcalculated(String itemNo, String name, int quantity, double itemProfit, double Total, double sellPrice) {
        this.name = name;
        this.itemNo = itemNo;
        this.quantity = quantity;
        this.itemProfit = itemProfit;
        this.Total = Total;
        this.sellPrice = sellPrice;

    }

    public String getName() {
//        System.out.println(name);
        return name;
    }

    public String getItemNo() {
        return itemNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getItemProfit() {
        return itemProfit;
    }

    public double getTotal() {
        return Total;
    }

    public double getSellPrice() {
        return sellPrice;
    }


    public static ArrayList<itemcalculated> getItems(String id) throws SQLException {
        ArrayList<t_invoiceItem> allItems = t_invoiceItem.getItems(id);
        ArrayList<itemcalculated> allRec = new ArrayList<>();

        for (t_invoiceItem item : allItems) {
            String itemid = item.getItemNo();
            String itemname = item.getName();
            int qty = item.getQuantity();
            double sprice = item.getSellPrice();
            double bprice = item.getBuyPrice();
            double profit = (sprice - bprice) * qty;
            double Total = sprice * qty;

            allRec.add(new itemcalculated (itemid, itemname, qty, profit, Total, sprice));

        }
        return allRec;

    }
}
