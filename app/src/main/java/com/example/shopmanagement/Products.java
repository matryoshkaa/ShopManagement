package com.example.shopmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//Activity where user can search for product and purchase it for the store

public class Products extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(Products.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}