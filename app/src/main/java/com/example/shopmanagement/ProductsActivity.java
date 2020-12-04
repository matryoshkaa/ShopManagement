package com.example.shopmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Activity where user can search for product and purchase it for the store

public class ProductsActivity extends AppCompatActivity {


    private static final String TAG = "Firelog";
    private RecyclerView productsListView;
    String productDocId;
    ArrayList<Products> productsList;
    ProductsDisplayAdapter productsDisplayAdapter;

    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        productsList = new ArrayList<>();
        productsDisplayAdapter = new ProductsDisplayAdapter(getApplicationContext(), productsList);

        productsListView = (RecyclerView)findViewById(R.id.productList);
        productsListView.setHasFixedSize(true);
        productsListView.setLayoutManager(new LinearLayoutManager(this));
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

        productsListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, AddToStock.class);
                intent.putExtra("product_id", productDocId);
                setResult(RESULT_OK, intent);
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