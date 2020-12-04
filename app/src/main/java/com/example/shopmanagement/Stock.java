package com.example.shopmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Activity where products bought from supplier is displayed, user can set price/discount details and upload it to the shop

public class Stock extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DocumentReference documentReference;
    String userId;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    CollectionReference ref;
    private StockDisplayAdapter adapter;
    ListView stockListView;
    ArrayList<StockDisplay> stockList = new ArrayList<>();
    String chosenProduct;

    Map<String, Object> map;

    String prodId;
    String documentId;

    String chosenProductToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        stockListView = (ListView) findViewById(R.id.stockList);
        adapter = new StockDisplayAdapter(this, stockList);
        stockListView.setAdapter(adapter);

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
                    startActivity(new Intent(Stock.this, SignIn.class));
                }
            }
        };

        //checking if product list is empty
       db.collection("Users")
                .document(userId)
                .collection("Stock")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().size()==0)
                        Toast.makeText(Stock.this, "No products to display", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

        ref = db.collection("Users")
                .document(userId)
                .collection("Stock");


        getDataFromDatabase();

        stockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Stock.this, AddToStore.class);

                TextView chosen = (TextView) view.findViewById(R.id.productId);
                chosenProduct= chosen.getText().toString();

                if(userId!=null)
                    intent.putExtra("userId",userId);

                intent.putExtra("prodId",chosenProduct);
                startActivityForResult(intent,0);
            }
        });

        stockListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                TextView chosenId = (TextView) view.findViewById(R.id.productId);
                chosenProductToDelete= chosenId.getText().toString();

                ref = db.collection("Users")
                        .document(userId)
                        .collection("Stock");


                ref.whereEqualTo("productId",chosenProductToDelete).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                map = document.getData();
                                documentId =document.getId();
                            }
                        } else {
                            System.out.println("Error getting documents: ");
                        }
                    }
                });

                new MaterialAlertDialogBuilder(Stock.this)
                        .setTitle("Confirm delete from stock")
                        .setMessage("Are you sure you want to remove this product from your stock list?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                stockList.remove(position);
                                adapter.notifyDataSetChanged();
                                deleteFromDatabase();
                                Toast.makeText(Stock.this, "Product has been successfully removed from your stock!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();

                return true;
            }
        });


    }

    public void deleteFromDatabase(){

        if(documentId!=null && !documentId.isEmpty()) {
            db.collection("Users")
                    .document(userId)
                    .collection("Stock")
                    .document(documentId).delete();
        }




    }

    public void addToStockList(String prodName,String supplierName,String prodId,double unitPrice,double prodTax,double shippingPrice,String prodAmountInStock){

        stockList.add(new StockDisplay(prodName, supplierName,prodId,unitPrice,prodTax,shippingPrice, prodAmountInStock));
        adapter.notifyDataSetChanged();

    }

    public void getDataFromDatabase(){
        //grabbing data from firestore to display in list
        ref.orderBy("productName").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        String prodAmountInStock = map.get("productAmount").toString();

                        if(prodAmountInStock.equals("0"))
                            prodAmountInStock="Out of Stock";

                        prodId = map.get("productId").toString();

                        double unitPrice=Double.parseDouble(price);
                        double prodTax=Double.parseDouble(tax);
                        double shippingPrice=Double.parseDouble(shipping);

                        addToStockList(prodName, supplierName,prodId,unitPrice,prodTax,shippingPrice,prodAmountInStock);

                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                adapter.clear();
                getDataFromDatabase();
            }
        }
    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(Stock.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}