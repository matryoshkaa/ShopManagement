package com.example.shopmanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.SetOptions;


import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SellItem extends AppCompatActivity {

    CollectionReference ref;
    private FirebaseFirestore db;

    Map<String, Object> map;


    double totalProductCostPrice = 0.0;
    double marketingExpenses = 0.0;
    double storageExpenses = 0.0;


    String imageUrl;


    ImageView prodImageView;

    TextView productName;
    TextView supplierName;
    TextView productAmount;
    TextView sellingPrice;

    String productNameS;
    String supplierNameS;
    String productAmountS;
    String sellingPriceS;

    ImageButton add;
    ImageButton subtract;
    TextView amtBeingBought;

    Button sell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);

        Intent i = getIntent();
        String chosenProduct = i.getStringExtra("prodName");
        final String itemId = i.getStringExtra("prodId");
        String userId = i.getStringExtra("userId");

        productName = findViewById(R.id.prodName);
        supplierName = findViewById(R.id.supplierName);
        prodImageView = findViewById(R.id.prodImageView);
        productAmount = findViewById(R.id.amt);
        sellingPrice = findViewById(R.id.sellingPrice);
        add = findViewById(R.id.addbutton);
        subtract = findViewById(R.id.subtractbutton);
        amtBeingBought = findViewById(R.id.amtBeingBought);
        sell = findViewById(R.id.sellButton);


        //TEMPORARY DELETE LATER
        userId = "GUbm4ERwNTdm3TekcQ0UrpRAZYs1";


        db = FirebaseFirestore.getInstance();
        ref = db.collection("Users")
                .document(userId)
                .collection("Store");


        //grabbing data from firestore to display in list
        ref.whereEqualTo("productId", itemId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        productNameS = map.get("productName").toString();
                        supplierNameS = map.get("supplierName").toString();
                        productAmountS = map.get("prodAmount").toString();
                        sellingPriceS = map.get("sellingPrice").toString();

                        productName.setText(productNameS);
                        supplierName.setText(supplierNameS);
                        productAmount.setText(productAmountS);
                        sellingPrice.setText(sellingPriceS);

                        imageUrl = map.get("productImage").toString();
//
                        imageRetrieval(imageUrl);
//

                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(amtBeingBought.getText().toString());
                int numOfUnits = Integer.parseInt(productAmount.getText().toString());

                if (num != numOfUnits) {
                    num++;
                    amtBeingBought.setText(Integer.toString(num));
                }
            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(amtBeingBought.getText().toString());

                if (num != 0) {
                    num--;
                    amtBeingBought.setText(Integer.toString(num));
                }
            }
        });

        final String finalUserId = userId;
        final String finalUserId1 = userId;
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(amtBeingBought.getText().toString());
                double price = Double.parseDouble(sellingPrice.getText().toString());
                if (num != 0) {
                    final double totalPrice = num * price;
                    new MaterialAlertDialogBuilder(SellItem.this)
                            .setTitle("Confirm Sale")
                            .setMessage("Are you sure you want to confirm this sale of AED " + Double.toString(totalPrice) + "?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //adding to transaction
                                    Map<String, Object> transaction = new HashMap<>();
                                    transaction.put("Item Name", productName.getText().toString());
                                    transaction.put("Supplier", supplierName.getText().toString());
                                    transaction.put("Amount Purchased", amtBeingBought.getText().toString());
                                    transaction.put("Amount Paid", Double.toString(totalPrice));
                                    transaction.put("Time", getDateTime());
                                    transaction.put("Transaction Type", "Customer");

                                    // Add a new document with a generated ID
                                    db.collection("Users")
                                            .document(finalUserId)
                                            .collection("Transactions")
                                            .add(transaction)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });


                                    //adding to reports
                                    final DocumentReference docIdRef = db.collection("Users").document(finalUserId).collection("Reports").document(getDate());
                                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    double agg = Double.parseDouble(document.get("Aggregate").toString()) +totalPrice;
                                                    Map<String, Object> updateAmount = new HashMap<>();
                                                    updateAmount.put("Aggregate", Double.toString(agg));
                                                    docIdRef.set(updateAmount, SetOptions.merge());
                                                } else {
                                                    Map<String, Object> updateAmount = new HashMap<>();
                                                    updateAmount.put("Date", getDateTime().substring(0,10));
                                                    updateAmount.put("Aggregate", Double.toString(totalPrice));
                                                    docIdRef.set(updateAmount, SetOptions.merge());
                                                }
                                            } else {
                                            }
                                        }
                                    });

                                    db.collection("Users")
                                            .document(finalUserId)
                                            .collection("Reports")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                        }
                                                    } else {

                                                    }
                                                }
                                            });

                                    //subtracting from store amount
                                    final int newAmt = Integer.parseInt(productAmount.getText().toString()) - Integer.parseInt(amtBeingBought.getText().toString());
                                    ref.whereEqualTo("productId", itemId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    map = document.getData();
                                                    productNameS = map.get("productName").toString();

                                                    String fbItemId = document.getId();

                                                    DocumentReference updateDoc = db.collection("Users")
                                                            .document(finalUserId1)
                                                            .collection("Store")
                                                            .document(fbItemId);

                                                    Map<String, Object> updateAmount = new HashMap<>();
                                                    updateAmount.put("prodAmount", Integer.toString(newAmt));
                                                    updateDoc.set(updateAmount, SetOptions.merge());


                                                    TextView amt = findViewById(R.id.amt);
                                                    amt.setText(Integer.toString(newAmt));
                                                    amtBeingBought.setText("0");
                                                }
                                            } else {
                                                System.out.println("Error getting documents: ");
                                            }
                                        }
                                    });
//

                                }
                            })
                            .setNeutralButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();

                }
            }
        });


    }


    private void imageRetrieval(String imageUrl) {
        //image retrieval from db
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadImageTask(prodImageView).execute(imageUrl);
        } else {
            System.out.println("No network connection available.");
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmapImage;

        public DownloadImageTask(ImageView webImage) {
            this.bitmapImage = webImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String webUrl = urls[0];
            Bitmap fetchedImage = null;
            try {
                InputStream in = new java.net.URL(webUrl).openStream();
                fetchedImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return fetchedImage;
        }

        protected void onPostExecute(Bitmap result) {
            bitmapImage.setImageBitmap(result);
        }
    }


    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void goToSettings(View view) {
        Intent i = new Intent();
        startActivity(new Intent(SellItem.this, Settings.class));
    }

    public void goBack(View view) {
        Intent intent = new Intent(SellItem.this, Store.class);
        setResult(RESULT_OK, intent);
        startActivity(intent);
    }
}