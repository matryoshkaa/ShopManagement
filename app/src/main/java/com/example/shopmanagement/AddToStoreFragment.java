package com.example.shopmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

public class AddToStoreFragment extends Fragment {

    double profit=0.0;
    double discount=0.0;
    double sellingPrice=0.0;
    double totalCostPrice=0.0;

    TextView sellingPriceTV;
    EditText productBenefit;
    EditText productDiscount;
    Button calculateButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add_to_store, container, false);

        productBenefit=view.findViewById(R.id.productBenefit);
        productDiscount=view.findViewById(R.id.productDiscount);
        sellingPriceTV=view.findViewById(R.id.sellingPrice);
        calculateButton=view.findViewById(R.id.calculateButton);

        Bundle bundle = getArguments();
        totalCostPrice=bundle.getDouble("totalCP");

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double finalSellingPrice = setSellingPrice();

                DecimalFormat newFormat = new DecimalFormat("#.##");
                double roundedSP =  Double.valueOf(newFormat.format(finalSellingPrice));
                sellingPriceTV.setText(Double.toString(roundedSP));

            }
        });


        return view;

    }


    public double setSellingPrice(){

        if(!productBenefit.getText().toString().isEmpty())
            profit=Double.parseDouble(String.valueOf(productBenefit.getText()));

        sellingPrice=(totalCostPrice)+((profit/100)*totalCostPrice);

        if(!productDiscount.getText().toString().isEmpty()){
            discount=Double.parseDouble(String.valueOf(productDiscount.getText()));
            sellingPrice=sellingPrice-(sellingPrice*(discount/100));
        }
        return sellingPrice;

    }
}
