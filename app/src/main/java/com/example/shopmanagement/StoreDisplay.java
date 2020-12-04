package com.example.shopmanagement;

public class StoreDisplay {

    private String itemId;
    private String productName;
    private String supplierName;

    private int prodAmount;
    private double sellingPrice;


    public StoreDisplay() {
        //empty constructor
    }

    public StoreDisplay(String itemId, String prodName,String supplierName,int prodAmount,double sellingPrice){
        this.itemId=itemId;
        this.productName=prodName;
        this.supplierName=supplierName;
        this.prodAmount=prodAmount;
        this.sellingPrice=sellingPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getProdAmount() { return prodAmount; }

    public void setProdAmount(int prodAmount) { this.prodAmount = prodAmount; }

    public double getSellingPrice() { return sellingPrice; }

    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }
}
