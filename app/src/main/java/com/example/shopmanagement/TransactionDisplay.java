package com.example.shopmanagement;

public class TransactionDisplay {

    private String id;
    private String itemName;
    private String supplier;
    private String date;
    private Integer amountPurchasedI;
    private double amountPaidD;

    public TransactionDisplay() {
        //empty constructor
    }

    public TransactionDisplay( String id,String itemName, String supplier, String date, Integer amountPurchasedI, double amountPaidD) {
        this.id = id;
        this.itemName = itemName;
        this.supplier = supplier;
        this.date = date;
        this.amountPurchasedI = amountPurchasedI;
        this.amountPaidD = amountPaidD;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAmountPurchasedI() {
        return amountPurchasedI;
    }

    public void setAmountPurchasedI(Integer amountPurchasedI) {
        this.amountPurchasedI = amountPurchasedI;
    }

    public double getAmountPaidD() {
        return amountPaidD;
    }

    public void setAmountPaidD(double amountPaidD) {
        this.amountPaidD = amountPaidD;
    }
}
