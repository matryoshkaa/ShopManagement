package com.example.shopmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//Activity where user can search for product and purchase it for the store

public class ProductsActivity extends AppCompatActivity{


    FirebaseFirestore mFireStore;

    private static final String TAG = "Firelog";
    ListView productsListView;
    String productDocId;
    ArrayList<Products> productsList;
    ProductsDisplayAdapter productsDisplayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        productsList = new ArrayList<>();
        productsDisplayAdapter = new ProductsDisplayAdapter(this, productsList);

        productsListView = (ListView)findViewById(R.id.productList);
        productsListView.setAdapter(productsDisplayAdapter);

        mFireStore = FirebaseFirestore.getInstance();


        mFireStore.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.d(TAG, "error" + error.getMessage());
                }

                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                         productDocId = doc.getDocument().getId();
                          Products products = doc.getDocument().toObject(Products.class).withId(productDocId);
                          productsList.add(products);
                          productsDisplayAdapter.notifyDataSetChanged();
                    }

                }
            }
        });



        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductsActivity.this, AddToStock.class);
//                TextView chosen = (TextView) view.findViewById(R.id.productId)''
                intent.putExtra("product_id", productDocId);
                startActivity(intent);
            }
        });

    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(ProductsActivity.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }


}