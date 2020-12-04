package com.example.shopmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsDisplayAdapter extends RecyclerView.Adapter<ProductsDisplayAdapter.ViewHolder> {

    public List <Products> productsList;
    public ProductsDisplayAdapter(List<Products> productList){
        this.productsList = productList;
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
        GlideApp.with(holder.image.getContext()).load(productsList.get(position).getProductImage()).into(holder.image);
//        Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);
//        GlideApp.with(t)
//                .load("http://via.placeholder.com/300.png")
//                .into(ivImg);
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;

            image = (ImageView)myView.findViewById(R.id.productImage);
            productText = (TextView)myView.findViewById(R.id.productName);
            supplierText = (TextView)myView.findViewById(R.id.supplierName);
        }
    }

}
