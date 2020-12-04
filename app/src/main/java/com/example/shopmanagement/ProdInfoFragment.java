package com.example.shopmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ProdInfoFragment extends Fragment {


    TextView unitCost;
    TextView prodtax;
    TextView shipping;
    TextView marketingCost;
    TextView storagePrice;
    TextView costPrice;
    TextView unitsAvailable;

    CollectionReference ref;
    private FirebaseFirestore db;
    Map<String, Object> map;

    String userId;
    String documentId;
    String productId;

    double roundedCP=0.0;

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

        db = FirebaseFirestore.getInstance();


        Bundle bundle = getArguments();

        if(bundle!=null) {
            String prodUnitPrice = bundle.getString("unitPrice");
            String prodTax = bundle.getString("productTax");
            String prodShipping = bundle.getString("shippingPrice");
            String prodAmount = bundle.getString("productAmount");
            double prodMarketing = bundle.getDouble("marketingPrice");
            double prodStorage = bundle.getDouble("storagePrice");
            double totalProductCostPrice = bundle.getDouble("totalCP");
            documentId = bundle.getString("documentId");

            userId = bundle.getString("userId");
            productId = bundle.getString("prodId");

            grabAmount();

            DecimalFormat newFormat = new DecimalFormat("#.##");
            roundedCP = Double.valueOf(newFormat.format(totalProductCostPrice));
            setCostPrice();

            setDetail(prodUnitPrice, prodTax, prodShipping, prodAmount, prodMarketing, prodStorage, roundedCP);
        }

        return view;
    }

    private void setDetail(String prodUnitPrice,String prodTax,String prodShipping,String prodAmount
            ,double prodMarketing,double prodStorage,double totalProductCostPrice){
        unitCost.setText(prodUnitPrice);
        prodtax.setText(prodTax);
        shipping.setText(prodShipping);
        marketingCost.setText(Double.toString(prodMarketing));
        storagePrice.setText(Double.toString(prodStorage));
        costPrice.setText(Double.toString(totalProductCostPrice));
    }

    public void setCostPrice(){

        Map<String, Double> priceMap=new HashMap<>();
        priceMap.put("costPrice",roundedCP);

        if(roundedCP!=0){
            db.collection("Users")
                    .document(userId)
                    .collection("Stock")
                    .document(documentId)
                    .set(priceMap, SetOptions.merge());

        }else
         System.out.println("Error");

    }


    public void grabAmount(){

        ref = db.collection("Users")
                .document(userId)
                .collection("Stock");

        //grabbing data from firestore to display in list
        ref.whereEqualTo("productId",productId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        String amt = map.get("productAmount").toString();

                        if(amt.equals("0")){
                            unitsAvailable.setTextColor(Color.parseColor("#e60000"));
                            unitsAvailable.setText("Out of Stock");
                        }
                        else
                            unitsAvailable.setText(amt);
                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });

    }
}
