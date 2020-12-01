package com.example.shopmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.Map;

public class AddToStore extends AppCompatActivity {

    CollectionReference ref;
    private FirebaseFirestore db;

    Map<String, Object> map;

    TextView productName;
    TextView supplier;
    TextView unitCost;
    TextView prodtax;
    TextView shipping;
    TextView marketingCost;
    TextView storagePrice;
    TextView costPrice;
    TextView unitsAvailable;
    TextView sellingPriceTV;

    EditText productBenefit;
    EditText productDiscount;

    double profit=0.0;
    double discount=0.0;
    double sellingPrice=0.0;
    double totalProductCostPrice=0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_store);

        Intent i = getIntent();
        String chosenProduct=i.getStringExtra("prodName");
        String userId=i.getStringExtra("userId");

        productName=findViewById(R.id.prodName);
        supplier=findViewById(R.id.supplierName);
        unitCost=findViewById(R.id.unitPrice);
        prodtax=findViewById(R.id.tax);
        shipping=findViewById(R.id.shippingPrice);
        marketingCost=findViewById(R.id.marketingCost);
        storagePrice=findViewById(R.id.storagePrice);
        costPrice=findViewById(R.id.costPrice);
        unitsAvailable=findViewById(R.id.unitsAvailable);
        productBenefit=findViewById(R.id.productBenefit);
        productDiscount=findViewById(R.id.productDiscount);
        sellingPriceTV=findViewById(R.id.sellingPrice);


        db = FirebaseFirestore.getInstance();
        ref = db.collection("Users")
                .document(userId)
                .collection("Stock");


        //grabbing data from firestore to display in list
        ref.whereEqualTo("productName",chosenProduct).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        String prodName = map.get("productName").toString();
                        String supplierName = map.get("supplierName").toString();
                        String price = map.get("unitPrice").toString();
                        String tax = map.get("productTax").toString();
                        String shipping = map.get("shippingPrice").toString();
                        String productAmount = map.get("productAmount").toString();

                        setDetails(prodName, supplierName,price,tax,shipping,productAmount);

                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });


    }

    public void setDetails(String prodName,String supplierName,String unitPrice,String prodTax,String shippingPrice,String prodAmount){

        productName.setText(prodName);
        supplier.setText(supplierName);
        unitCost.setText(unitPrice);
        prodtax.setText(prodTax);
        shipping.setText(shippingPrice);
        unitsAvailable.setText(prodAmount);

        double marketingExpenses=(Double.parseDouble(unitPrice)*2);
        double storageExpenses=(Double.parseDouble(prodAmount)*2);
        double totalCostPrice=(Double.parseDouble(prodAmount)*2);

        marketingCost.setText(Double.toString(marketingExpenses));
        storagePrice.setText(Double.toString(storageExpenses));

        totalProductCostPrice=(Double.parseDouble(unitPrice))+(Double.parseDouble(prodTax))+(Double.parseDouble(shippingPrice))+marketingExpenses+storageExpenses;
        costPrice.setText(Double.toString(totalProductCostPrice));


    }

    public double setSellingPrice(){

        if(!productBenefit.getText().toString().isEmpty())
            profit=Double.parseDouble(String.valueOf(productBenefit.getText()));

        sellingPrice=(totalProductCostPrice)+((profit/100)*totalProductCostPrice);

        if(!productDiscount.getText().toString().isEmpty()){
            discount=Double.parseDouble(String.valueOf(productDiscount.getText()));
            sellingPrice=sellingPrice-(sellingPrice*(discount/100));
        }
            return sellingPrice;

    }

    public void calculateTotalSellingPrice(View view){

        double finalSellingPrice = setSellingPrice();

        DecimalFormat newFormat = new DecimalFormat("#.##");
        double roundedSP =  Double.valueOf(newFormat.format(finalSellingPrice));

        sellingPriceTV.setText(Double.toString(roundedSP));


    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(AddToStore.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}