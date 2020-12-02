package com.example.shopmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

public class ProdInfoFragment extends Fragment {


    TextView unitCost;
    TextView prodtax;
    TextView shipping;
    TextView marketingCost;
    TextView storagePrice;
    TextView costPrice;
    TextView unitsAvailable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_prod_details, container, false);

        unitCost=view.findViewById(R.id.unitPrice);
        prodtax=view.findViewById(R.id.tax);
        shipping=view.findViewById(R.id.shippingPrice);
        marketingCost=view.findViewById(R.id.marketingCost);
        storagePrice=view.findViewById(R.id.storagePrice);
        costPrice=view.findViewById(R.id.costPrice);
        unitsAvailable=view.findViewById(R.id.unitsAvailable);

        Bundle bundle = getArguments();
        assert bundle != null;
        String prodUnitPrice = bundle.getString("unitPrice");
        String prodTax = bundle.getString("productTax");
        String prodShipping = bundle.getString("shippingPrice");
        String prodAmount = bundle.getString("productAmount");
        double prodMarketing = bundle.getDouble("marketingPrice");
        double prodStorage = bundle.getDouble("storagePrice");
        double totalProductCostPrice=bundle.getDouble("totalCP");

        DecimalFormat newFormat = new DecimalFormat("#.##");
        double roundedCP =  Double.valueOf(newFormat.format(totalProductCostPrice));


        setDetail(prodUnitPrice,prodTax,prodShipping,prodAmount,prodMarketing,prodStorage,roundedCP);

        return view;
    }

    private void setDetail(String prodUnitPrice,String prodTax,String prodShipping,String prodAmount
            ,double prodMarketing,double prodStorage,double totalProductCostPrice){
        unitCost.setText(prodUnitPrice);
        prodtax.setText(prodTax);
        shipping.setText(prodShipping);
        unitsAvailable.setText(prodAmount);
        marketingCost.setText(Double.toString(prodMarketing));
        storagePrice.setText(Double.toString(prodStorage));
        costPrice.setText(Double.toString(totalProductCostPrice));
    }
}
