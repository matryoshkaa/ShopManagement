package com.example.shopmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//Activity where user can sell products+ send transaction details to transactions activity

public class Store extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(Store.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}