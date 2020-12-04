package com.example.shopmanagement;

public class StockDisplay {

    private String productName;
    private String supplierName;
    private String productId;
    private String productStock;
    private double unitPrice;
    private double productTax;
    private double shippingPrice;


    public StockDisplay() {
        //empty constructor
    }

    public StockDisplay(String productName,String supplierName,String productId,double unitPrice,double productTax,double shippingPrice,String productStock){
        this.productName=productName;
        this.supplierName=supplierName;
        this.productId=productId;
        this.unitPrice=unitPrice;
        this.productTax=productTax;
        this.shippingPrice=shippingPrice;
        this.productStock=productStock;
    }

    public String getProductStock() {
        return productStock;
    }

    public void setProductStock(String productStock) {
        this.productStock = productStock;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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