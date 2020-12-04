package com.example.shopmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

//Activity where user can sell products+ send transaction details to transactions activity

public class Store extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DocumentReference documentReference;
    String userId;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    CollectionReference ref;
    private StoreDisplayAdapter adapter;
    ListView storeListView;
    ArrayList<StoreDisplay> storeList = new ArrayList<StoreDisplay>();
    String chosenProduct;

    Map<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        storeListView = (ListView) findViewById(R.id.storeList);
        adapter = new StoreDisplayAdapter(this, storeList);
        storeListView.setAdapter(adapter);

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
                    startActivity(new Intent(Store.this, SignIn.class));
                }
            }
        };

        //TEMPORARY DELETE LATER
        userId = "GUbm4ERwNTdm3TekcQ0UrpRAZYs1";


        //checking if product list is empty
        db.collection("Users")
                .document(userId)
                .collection("Store")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().size()==0)
                        Toast.makeText(Store.this, "No products to display", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

        ref = db.collection("Users")
                .document(userId)
                .collection("Store");




        //grabbing data from firestore to display in list
        ref.orderBy("productName").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        String itemId = map.get("productId").toString();
                        String prodName = map.get("productName").toString();
                        String supplierName = map.get("supplierName").toString();

                        String prodAmount = map.get("prodAmount").toString();
                        String sellingPrice = map.get("sellingPrice").toString();

                        int prodAmountD=Integer.parseInt(prodAmount);
                        double sellingPriceD=Double.parseDouble(sellingPrice);
                        addToStockList(itemId, prodName, supplierName, prodAmountD,sellingPriceD);

                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });
        storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView amtTV = (TextView) view.findViewById(R.id.amt);
                String amt = amtTV.getText().toString();

                if (amt != "Out of Stock"){
                Intent intent = new Intent(Store.this, SellItem.class);

                TextView chosen = (TextView) view.findViewById(R.id.prodNameStore);
                chosenProduct= chosen.getText().toString();
                TextView itemIdTV = (TextView) view.findViewById(R.id.itemid);
                String itemId = itemIdTV.getText().toString();
                if(userId!=null)
                    intent.putExtra("userId",userId);


                intent.putExtra("prodName",chosenProduct);
                intent.putExtra("prodId",itemId);

                setResult(RESULT_OK, intent);
                startActivity(intent);}
            }
        });
    }

    public void addToStockList(String itemId,String prodName, String supplierName, Integer prodAmountD,double sellingPriceD){

        storeList.add(new StoreDisplay(itemId, prodName, supplierName,prodAmountD,sellingPriceD));
        adapter.notifyDataSetChanged();

    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(Store.this, Settings.class));
    }

    public void goBack (View view){
        Intent intent = new Intent(Store.this, Dashboard.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);
    }
}