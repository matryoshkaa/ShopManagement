package com.example.shopmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//Activity where monthly/daily sales and profits statistics are shown

public class SalesReports extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_reports);
    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(SalesReports.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}