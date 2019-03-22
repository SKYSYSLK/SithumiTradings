package controllers;

public class itemcalculated {
    private int quantity;
    private double itemProfit, Total, sellPrice;
    private String name;
    private String itemNo;
    public itemcalculated(String itemNo, String name, int quantity,double itemProfit, double Total, double sellPrice ){
        this.name = name;
        this.itemNo = itemNo;
        this.quantity = quantity;
        this.itemProfit = itemProfit;
        this.Total = Total;
        this.sellPrice = sellPrice;

    }

    public String getName(){
        System.out.println(name);
        return name;}
    public String getItemNo(){return itemNo;}
    public int getQuantity(){return quantity;}
    public double getItemProfit(){return itemProfit;}
    public double getTotal(){return Total;}
    public double getSellPrice(){return sellPrice;}
}
