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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsDisplayAdapter extends RecyclerView.Adapter<ProductsDisplayAdapter.ViewHolder> {

    public List <Products> productsList;
    public Context context;
    public ProductsDisplayAdapter(Context context, List<Products> productList){
        this.productsList = productList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_products_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productText.setText(productsList.get(position).getProductName());
        holder.supplierText.setText(productsList.get(position).getSupplierName());
        holder.priceText.setText(Double.toString( productsList.get(position).getUnitPrice()));
        holder.shippingText.setText(Double.toString(productsList.get(position).getShippingPrice()));
        holder.taxText.setText(Double.toString(productsList.get(position).getProductTax()));
        GlideApp.with(holder.image.getContext()).load(productsList.get(position).getProductImage()).into(holder.image);

        final String productDocId = productsList.get(position).productId;

//        holder.myView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AddToStock.class);
//                intent.putExtra("product_id", productDocId);
//
////                Toast.makeText(context, "Product id" + productDocId, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View myView;
        public ImageView image;
        public TextView productText;
        public TextView supplierText;
        public TextView priceText;
        public TextView taxText;
        public TextView shippingText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;

            image = (ImageView)myView.findViewById(R.id.productImage);
            productText = (TextView)myView.findViewById(R.id.productName);
            supplierText = (TextView)myView.findViewById(R.id.supplierName);
            priceText = (TextView)myView.findViewById(R.id.productUnitPrice);
            taxText = (TextView)myView.findViewById(R.id.productTaxAmount);
            shippingText = (TextView)myView.findViewById(R.id.productShippingPrice);
        }
    }

}
