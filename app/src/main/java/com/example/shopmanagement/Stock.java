package com.example.shopmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//Activity where products bought from supplier is displayed, user can set price/discount details and upload it to the shop

public class Stock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(Stock.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}