package com.example.shopmanagement;

public class Products {

    private String productId;
    private String productName;
    private String productImage;
    private String supplierName;
    private double unitPrice;
    private double productTax;
    private double shippingPrice;
    private int productAmount;

    public Products() {
    }

    public Products(String productId, String productName, String productImage, String supplierName, double unitPrice, double productTax, double shippingPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.supplierName = supplierName;
        this.unitPrice = unitPrice;
        this.productTax = productTax;
        this.shippingPrice = shippingPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
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

    @Override
    public String toString() {
        return "Products{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productImage='" + productImage + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", unitPrice=" + unitPrice +
                ", productTax=" + productTax +
                ", shippingPrice=" + shippingPrice +
                '}';
    }
}
