package com.example.shopmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//Activity where user can look into customer and supplier transaction(all expenditure/sales/profits)

public class Transactions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(Transactions.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}