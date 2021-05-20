package com.example.Application.Models;

public class HistoryDetails {
    private String pname, price, quantity, itemNumber;

    public HistoryDetails(){ }

    public HistoryDetails(String itemNumber){
        this.itemNumber = itemNumber;
    }
    public HistoryDetails(String name, String quantity, String price) {
        this.pname = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String name) {
        this.pname = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
