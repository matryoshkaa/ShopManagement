package com.example.shopmanagement;

public class StockDisplay {

    private String productName;
    private String supplierName;
    private double unitPrice;
    private double productTax;
    private double shippingPrice;


    public StockDisplay() {
        //empty constructor
    }

    public StockDisplay(String productName,String supplierName,double unitPrice,double productTax,double shippingPrice){
        this.productName=productName;
        this.supplierName=supplierName;
        this.unitPrice=unitPrice;
        this.productTax=productTax;
        this.shippingPrice=shippingPrice;
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getProductTax() {
        return productTax;
    }

    public void setProductTax(double productTax) {
        this.productTax = productTax;
    }

    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }
}