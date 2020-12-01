package com.example.shopmanagement;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);







    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(Dashboard.this, Settings.class));
    }

    public void goToProducts (View view){
        Intent i=new Intent();
        startActivity(new Intent(Dashboard.this, Products.class));
    }

    public void goToTransactions (View view){
        Intent i=new Intent();
        startActivity(new Intent(Dashboard.this, Transactions.class));
    }

    public void goToStock (View view){
        Intent i=new Intent();
        startActivity(new Intent(Dashboard.this, Stock.class));
    }

    public void goToReports (View view){
        Intent i=new Intent();
        startActivity(new Intent(Dashboard.this, SalesReports.class));
    }

    public void goToStore (View view){
        Intent i=new Intent();
        startActivity(new Intent(Dashboard.this, Store.class));
    }
}