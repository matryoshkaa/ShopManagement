package com.example.shopmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

//Activity where user can look into customer and supplier transaction(all expenditure/sales/profits)

public class Transactions extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DocumentReference documentReference;
    String userId;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    CollectionReference ref;
    private TransactionDisplayAdapter adapter;
    ListView transactionListView;
    ArrayList<TransactionDisplay> transactionList = new ArrayList<TransactionDisplay>();
    String chosenProduct;

    Map<String, Object> map;

    TabLayout tabLayout;
    TabItem customer,supplier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        transactionListView = (ListView) findViewById(R.id.transactionList);
        adapter = new TransactionDisplayAdapter(this, transactionList);
        transactionListView.setAdapter(adapter);

        tabLayout=findViewById(R.id.tabLayout);
        customer=findViewById(R.id.customer);
        supplier=findViewById(R.id.supplier);

        //user information retrieval
        db = FirebaseFirestore.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
            documentReference = db.collection("Users").document(userId);
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Transactions.this, SignIn.class));
                }
            }
        };

        //TEMPORARY DELETE LATER
        userId = "GUbm4ERwNTdm3TekcQ0UrpRAZYs1";


        //checking if product list is empty
        db.collection("Users")
                .document(userId)
                .collection("Transactions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().size()==0)

                        Toast.makeText(Transactions.this, "No products to display", Toast.LENGTH_SHORT).show();

                } else {

                }
            }
        });

        ref = db.collection("Users")
                .document(userId)
                .collection("Transactions");
        Log.d("boop", "uwu");
        Query bbb = ref.whereEqualTo("Transaction Type","Customer");
        bbb.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        String id = document.getId();
                        String itemName = map.get("Item Name").toString();
                        String supplier = map.get("Supplier").toString();
                        String date = map.get("Time").toString();
                        String amountPurchased = map.get("Amount Purchased").toString();
                        String amountPaid = map.get("Amount Paid").toString();

                        int amountPurchasedI = Integer.parseInt(amountPurchased);
                        double amountPaidD = Double.parseDouble(amountPaid);
                        addToStockList(id, itemName, supplier, date, amountPurchasedI,amountPaidD);


                    }
                } else {
                    Log.d("BOOP", "AAAAAAAAAAAAA");
                }
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch(tab.getPosition()) {
                    case 0:{

                        adapter.clear();
                        transactionListView.setAdapter(adapter);

                        Log.d("boop", "uwu");
                        Query aaa = ref.whereEqualTo("Transaction Type","Customer");
                        aaa.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        map = document.getData();
                                        String id = document.getId();
                                        String itemName = map.get("Item Name").toString();
                                        String supplier = map.get("Supplier").toString();
                                        String date = map.get("Time").toString();
                                        String amountPurchased = map.get("Amount Purchased").toString();
                                        String amountPaid = map.get("Amount Paid").toString();

                                        int amountPurchasedI = Integer.parseInt(amountPurchased);
                                        double amountPaidD = Double.parseDouble(amountPaid);
                                        addToStockList(id, itemName, supplier, date, amountPurchasedI,amountPaidD);


                                    }
                                } else {
                                    Log.d("BOOP", "AAAAAAAAAAAAA");
                                }
                            }
                        });

                        break;
                    }
                    case 1:{
                        adapter.clear();
                        transactionListView.setAdapter(adapter);

                        Log.d("boop", "uwu");
                        Query aaa = ref.whereEqualTo("Transaction Type","Supplier");
                        aaa.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        map = document.getData();
                                        String id = document.getId();
                                        String itemName = map.get("Item Name").toString();
                                        String supplier = map.get("Supplier").toString();
                                        String date = map.get("Time").toString();
                                        String amountPurchased = map.get("Amount Purchased").toString();
                                        String amountPaid = map.get("Amount Paid").toString();

                                        int amountPurchasedI = Integer.parseInt(amountPurchased);
                                        double amountPaidD = Double.parseDouble(amountPaid);
                                        addToStockList(id, itemName, supplier, date, amountPurchasedI,amountPaidD);


                                    }
                                } else {
                                    Log.d("BOOP", "AAAAAAAAAAAAA");
                                }
                            }
                        });

                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        customer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //grabbing data from firestore to display in list
//                ref.whereEqualTo("Transaction Type","Customer").orderBy("Time").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                map = document.getData();
//                                String id = document.getId();
//                                String itemName = map.get("Item Name").toString();
//                                String supplier = map.get("Supplier").toString();
//                                String date = map.get("Time").toString();
//                                String amountPurchased = map.get("Amount Purchased").toString();
//                                String amountPaid = map.get("Amount Paid").toString();
//
//                                int amountPurchasedI = Integer.parseInt(amountPurchased);
//                                double amountPaidD = Double.parseDouble(amountPaid);
//                                addToStockList(id, itemName, supplier, date, amountPurchasedI,amountPaidD);
//
//
//                            }
//                        } else {
//                            Log.d("BOOP", "AAAAAAAAAAAAA");
//                        }
//                    }
//                });
//            }
//        });
    }

    public void addToStockList(String id, String itemName, String supplier, String date, Integer amountPurchasedI, double amountPaidD){

        transactionList.add(new TransactionDisplay(id, itemName, supplier, date, amountPurchasedI,amountPaidD));
        adapter.notifyDataSetChanged();

    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(Transactions.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}