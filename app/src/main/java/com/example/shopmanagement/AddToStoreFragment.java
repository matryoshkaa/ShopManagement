package com.example.shopmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class AddToStoreFragment extends Fragment {

    double profit=0.0;
    double discount=0.0;
    double sellingPrice=0.0;
    double promoSellingPrice=0.0;
    double totalCostPrice=0.0;

    TextView sellingPriceTV;
    TextView promoSellingPriceTV;
    TextView promoProfit;
    EditText productBenefit;
    EditText productDiscount;
    Button calculateButton;
    Button fixPriceButton;

    double finalSellingPrice=0.0;
    double finalPromoSellingPrice=0.0;
    double fixedSellingPrice=0.0;

    String productName;
    String userId;
    String documentId;

    CollectionReference ref;
    DocumentReference docRef;
    private FirebaseFirestore db;
    Map<String, Object> map;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add_to_store, container, false);

        productBenefit=view.findViewById(R.id.productBenefit);
        promoProfit=view.findViewById(R.id.promoProfit);
        productDiscount=view.findViewById(R.id.productDiscount);
        sellingPriceTV=view.findViewById(R.id.sellingPrice);
        promoSellingPriceTV=view.findViewById(R.id.promoSellingPrice);
        calculateButton=view.findViewById(R.id.calculateButton);
        fixPriceButton=view.findViewById(R.id.fixPrice);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            totalCostPrice = bundle.getDouble("totalCP");
            productName = bundle.getString("prodName");
            userId = bundle.getString("userId");
            documentId = bundle.getString("documentId");
        }

        db = FirebaseFirestore.getInstance();
        ref = db.collection("Users")
                .document(userId)
                .collection("Stock");


        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSellingPrice = setSellingPrice();
                finalPromoSellingPrice = setPromoSellingPrice();

                DecimalFormat newFormat = new DecimalFormat("#.##");
                double roundedSP =  Double.valueOf(newFormat.format(finalSellingPrice));
                sellingPriceTV.setText(" AED "+ Double.toString(roundedSP));

                double roundedPSP =  Double.valueOf(newFormat.format(finalPromoSellingPrice));
                if(roundedPSP!=0.0){
                promoSellingPriceTV.setText(" AED "+ Double.toString(roundedPSP));}

            }
        });



        fixPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double price=returnSellingPrice();
                System.out.println("PRICE FIXED "+price);

                Map<String, Double> priceMap=new HashMap<>();
                priceMap.put("sellingPrice",price);

                if(price!=0){
                    db.collection("Users")
                            .document(userId)
                            .collection("Stock")
                            .document(documentId)
                            .set(priceMap, SetOptions.merge());

                    Toast.makeText(getActivity(), "Product price has been successfully fixed!", Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(getActivity(), "Appropriate price has not been fixed!", Toast.LENGTH_SHORT);

            }
        });
        return view;

    }

    public double returnSellingPrice(){
        if(finalSellingPrice!=0.0) {
            if (finalPromoSellingPrice == 0.0) {

                //adding VAT
                fixedSellingPrice=(finalSellingPrice*0.05)+finalSellingPrice;

            } else {
                fixedSellingPrice=(finalPromoSellingPrice*0.05)+finalPromoSellingPrice;

            }
        }
        else
            Toast.makeText(getActivity(),"You have not fixed the price!",Toast.LENGTH_LONG).show();

        DecimalFormat newFormat = new DecimalFormat("#.##");
        double roundedFSP =  Double.valueOf(newFormat.format(fixedSellingPrice));

        return roundedFSP;
    }


    public double setSellingPrice(){

        if(!productBenefit.getText().toString().isEmpty())
            profit=Double.parseDouble(String.valueOf(productBenefit.getText()));

        sellingPrice=(totalCostPrice)+((profit/100)*totalCostPrice);
        return sellingPrice;

    }

    public double setPromoSellingPrice(){

        if(!productDiscount.getText().toString().isEmpty()){
            discount=Double.parseDouble(String.valueOf(productDiscount.getText()));

            if(!productBenefit.getText().toString().isEmpty()){
            profit=Double.parseDouble(String.valueOf(productBenefit.getText()));
            profit=profit-discount;
            promoProfit.setText(Double.toString(profit));
            promoSellingPrice=sellingPrice-(sellingPrice*(discount/100));}
            else
                promoSellingPrice=sellingPrice-(sellingPrice*(discount/100));
        }
        return promoSellingPrice;

    }

}
