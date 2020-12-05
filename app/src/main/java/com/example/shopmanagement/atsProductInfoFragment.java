package com.example.shopmanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class atsProductInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    TextView unitPriceTV;
    TextView taxTV;
    TextView shippingTV;
    TextView costPriceTV;
    TextView amountTV;

    double shippingPrice = 0.0;
    double tax = 0.0;
    double unitPrice = 0.0;
    double initialCostPrice = 0.0;
    int amountInStock = 0;


    DocumentReference productReference;
    private FirebaseFirestore mFireStore;
    private static final String TAG = "Firelog";

    String productDocumentId;
    double costPrice = 0.0;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productpage_productinfo, container, false);

        unitPriceTV = view.findViewById(R.id.unitPriceValue);
        shippingTV = view.findViewById(R.id.shippingPriceValue);
        taxTV = view.findViewById(R.id.taxValue);
        amountTV = view.findViewById(R.id.amountValue);

        mFireStore = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();
        if(bundle!=null) {
             unitPrice = bundle.getDouble("unitPrice");
             shippingPrice = bundle.getDouble("shippingPrice");
             tax = bundle.getDouble("productTax");
             amountInStock = bundle.getInt("productAmount");
             initialCostPrice = bundle.getDouble("initialCostPrice");
             productDocumentId = bundle.getString("productDocumentId");

            unitPriceTV.setText(String.valueOf(unitPrice));
            taxTV.setText(String.valueOf(tax));
            shippingTV.setText(String.valueOf(shippingPrice));
            costPriceTV.setText(String.valueOf(initialCostPrice));
            amountTV.setText(String.valueOf(amountInStock));

        }



        return view;
    }

//    private void setDetail(double unitPrice, double tax, double shippingPrice,double initialCostPrice, int amountInStock){
//        unitPriceTV.setText(String.valueOf(unitPrice));
//        taxTV.setText(String.valueOf(tax));
//        shippingTV.setText(String.valueOf(shippingPrice));
//        costPriceTV.setText(String.valueOf(initialCostPrice));
//        amountTV.setText(String.valueOf(amountInStock));
//    }


}