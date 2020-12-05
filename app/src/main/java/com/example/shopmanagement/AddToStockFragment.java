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

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class AddToStockFragment extends Fragment {

    double shippingPrice = 0.0;
    double tax = 0.0;
    double unitPrice = 0.0;
    double initialCostPrice = 0.0;
    int amountAvailable;
    int amountToBuy;
    double totalCost;

    TextView unitPriceTV;
    TextView shippingTV;
    TextView taxTV;
    EditText productAmountToBuy;
    TextView productAmountAvailable;
    TextView totalCostView;

    Button calculateTotalCost;
    Button sendTransaction;

    String productDocId;

    FirebaseFirestore mFireStore;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_to_stock_transaction, container, false);

        mFireStore = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            unitPrice = bundle.getDouble("unitPrice");
            shippingPrice = bundle.getDouble("shippingPrice");
            tax = bundle.getDouble("productTax");
            initialCostPrice = bundle.getDouble("initialCostPrice");
            amountAvailable = bundle.getInt("productAmount");
            String productDocumentId = bundle.getString("productDocumentId");

        }

        unitPriceTV = view.findViewById(R.id.unitPriceValue);
        shippingTV = view.findViewById(R.id.shippingPriceValue);
        taxTV = view.findViewById(R.id.taxValue);
        productAmountAvailable = view.findViewById(R.id.amountAvailableValue);
        totalCostView = view.findViewById(R.id.totalCostView);

        productAmountToBuy = view.findViewById(R.id.amountValue);
        calculateTotalCost = (Button)view.findViewById(R.id.calculateCostButton);
        calculateTotalCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountToBuy = Integer.parseInt(productAmountToBuy.getText().toString());
                totalCost = amountToBuy * initialCostPrice;
                showValues();
            }
        });

        return view;
    }

    public void showValues(){
        unitPriceTV.setText(String.valueOf(unitPrice));
        shippingTV.setText(String.valueOf(shippingPrice));
        taxTV.setText(String.valueOf(tax));
        productAmountAvailable.setText(String.valueOf(String.valueOf(amountAvailable)));
        productAmountToBuy.setText(String.valueOf(amountToBuy));
        totalCostView.setText("AED: " + String.valueOf(totalCost));
    }

}


