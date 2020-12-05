package com.example.shopmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class ProductsDisplayAdapter extends ArrayAdapter<Products>{

    public List <Products> productsList = new ArrayList<>();
    String productDocId;
    public Context context;
    public ProductsDisplayAdapter(Context context,  ArrayList<Products> list){
        super(context, 0 , list);
        this.productsList = list;
        this.context = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.activity_products_list_item, parent, false);

        Products currentProduct = productsList.get(position);

        TextView productText = (TextView)listItem.findViewById(R.id.productName);
        TextView supplierText = (TextView)listItem.findViewById(R.id.supplierName);
        TextView priceText = (TextView)listItem.findViewById(R.id.productUnitPrice);
        TextView taxText = (TextView)listItem.findViewById(R.id.productTaxAmount);
        TextView shippingText = (TextView)listItem.findViewById(R.id.productShippingPrice);

        productText.setText(currentProduct.getProductName());
        supplierText.setText(currentProduct.getSupplierName());
        priceText.setText(Double.toString( currentProduct.getUnitPrice()));
        shippingText.setText(Double.toString(currentProduct.getShippingPrice()));
        taxText.setText(Double.toString(currentProduct.getProductTax()));

        return listItem;
    }


}
