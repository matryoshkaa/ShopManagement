package com.example.shopmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Map;

public class AddToStore extends AppCompatActivity {

    CollectionReference ref;
    private FirebaseFirestore db;

    Map<String, Object> map;

    double totalProductCostPrice=0.0;
    double marketingExpenses=0.0;
    double storageExpenses=0.0;

    String imageUrl;
    String prodName;
    ImageView prodImageView;

    TextView productName;
    TextView supplier;
    String price;
    String supplierName;
    String tax;
    String shipping;
    String productAmount;

    String userId;
    String documentId;
    String chosenProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_store);

        Intent i = getIntent();
        chosenProduct=i.getStringExtra("prodId");
        userId=i.getStringExtra("userId");

        productName=findViewById(R.id.prodName);
        supplier=findViewById(R.id.supplierName);
        prodImageView=findViewById(R.id.prodImageView);

        BottomNavigationView navBar = findViewById(R.id.navbar);
        navBar.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.info_frame,
                new ProdDescriptionFragment()).commit();

        db = FirebaseFirestore.getInstance();
        ref = db.collection("Users")
                .document(userId)
                .collection("Stock");

        //getting document Id
        ref.whereEqualTo("productId",chosenProduct).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


        //grabbing data from firestore to display in list
        ref.whereEqualTo("productId",chosenProduct).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        prodName = map.get("productName").toString();
                        supplierName = map.get("supplierName").toString();
                        price = map.get("unitPrice").toString();
                        tax = map.get("productTax").toString();
                        shipping = map.get("shippingPrice").toString();
                        productAmount = map.get("productAmount").toString();
                        imageUrl=map.get("productImage").toString();

                        imageRetrieval(imageUrl);
                        setDetails(prodName, supplierName,price,tax,shipping,productAmount);

                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_prod_info:{
                            selectedFragment = new ProdInfoFragment();
                            sendDataToProdInfoFragment(selectedFragment);
                            }
                            break;
                        case R.id.nav_add:{
                            selectedFragment = new AddToStoreFragment();
                            sendInfoToAddToStoreFragment(selectedFragment);}
                            break;
                        case R.id.nav_help:
                            selectedFragment = new ProdDescriptionFragment();
                            break;
                        case R.id.nav_add_for_sale:{
                            selectedFragment = new AddForSaleFragment();
                            sendDataToFragmentSale(selectedFragment);
                        }
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.info_frame,
                            selectedFragment).commit();
                    return true;
                }
            };

    private void imageRetrieval(String imageUrl){
        //image retrieval from db
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public void setDetails(String prodName,String supplierName,String unitPrice,String prodTax,String shippingPrice,String prodAmount){

        productName.setText(prodName);
        supplier.setText(supplierName);

        marketingExpenses=(Double.parseDouble(unitPrice)*2);
        storageExpenses=(Double.parseDouble(prodAmount)*2);

        totalProductCostPrice=(Double.parseDouble(unitPrice))+(Double.parseDouble(prodTax))+(Double.parseDouble(shippingPrice))+marketingExpenses+storageExpenses;


    }

    private void sendInfoToAddToStoreFragment(Fragment fragment)
    {
        //sending data to product info fragment
        Bundle b = new Bundle();
        b.putDouble("totalCP", totalProductCostPrice);
        b.putString("prodName", prodName);
        b.putString("userId", userId);
        b.putString("documentId", documentId);
        fragment.setArguments(b);
    }

    private void sendDataToProdInfoFragment(Fragment fragment)
    {
        //sending data to product info fragment
        Bundle b = new Bundle();
        b.putString("unitPrice", price);
        b.putString("productTax", tax);
        b.putString("shippingPrice", shipping);
        b.putString("productAmount", productAmount);
        b.putDouble("marketingPrice", marketingExpenses);
        b.putDouble("storagePrice", storageExpenses);
        b.putDouble("totalCP", totalProductCostPrice);
        b.putString("userId", userId);
        b.putString("prodId", chosenProduct);
        b.putString("documentId", documentId);

        fragment.setArguments(b);
    }

    private void sendDataToFragmentSale(Fragment fragment)
    {
        //sending data to product info fragment
        Bundle b = new Bundle();

        int prodAmt=Integer.parseInt(productAmount);
        b.putInt("productAmount", prodAmt);
        b.putString("prodId", chosenProduct);
        b.putString("userId", userId);
        b.putString("documentId", documentId);

        fragment.setArguments(b);
    }


    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(AddToStore.this, Settings.class));
    }

    public void goBack (View view){
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}