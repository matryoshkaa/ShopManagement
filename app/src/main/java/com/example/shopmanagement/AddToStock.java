package com.example.shopmanagement;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.util.ArrayList;

public class AddToStock extends AppCompatActivity {
    ArrayList<Products> productsList;
    DocumentReference productReference;
    private FirebaseFirestore mFireStore;
    private static final String TAG = "Firelog";

    TextView productNameView;
    TextView supplierNameView;
    ImageView productImageView;

    String productDocumentId;


    String productName;
    String supplierName;
    String imageUrl;


    double unitPrice;
    double tax;
    double shipping;
    int productAmount;
    double initialCostPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_stock);

        Intent i = getIntent();
        productDocumentId = i.getStringExtra("product_id");

        BottomNavigationView navBar = findViewById(R.id.atsNavbar);
        navBar.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.atsInfoFrame,
                new ProdDescriptionFragment()).commit();

        productNameView = findViewById(R.id.atsProductName);
        supplierNameView = findViewById(R.id.atsSupplierName);
        productImageView = findViewById(R.id.atsProdImageView);
//        addToStockBtn = (ImageButton) findViewById(R.id.AddtoStockButton);

        mFireStore = FirebaseFirestore.getInstance();
        mFireStore.collection("Products").document(productDocumentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    productName = doc.getString("productName");
                    supplierName = doc.getString("supplierName");
                    unitPrice = doc.getLong("unitPrice").doubleValue();
                    tax = doc.getLong("productTax").doubleValue();
                    shipping = doc.getLong("shippingPrice").doubleValue();
                    productAmount = doc.getLong("productAmount").intValue();
                    imageUrl=doc.get("productImage").toString();
                    imageRetrieval(imageUrl);

                    setDetails(productName, supplierName, unitPrice, tax, shipping, productAmount);
                }
                else{
                    Log.d(TAG, "Error" + task.getException().getMessage());
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
                        case R.id.ats_nav_help:
                            selectedFragment = new ProdDescriptionFragment();
                            break;
                        case R.id.ats_nav_prod_info: {
                            selectedFragment = new atsProductInfoFragment();
                            sendDataToProdInfoFragment(selectedFragment);
                        }
                        break;

                        case R.id.ats_nav_buy: {
                            selectedFragment = new AddToStockFragment();
                            sendInfoToAddToStockFragment(selectedFragment);
                        }
                        break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.atsInfoFrame,
                            selectedFragment).commit();
                    return true;
                }
            };

    private void imageRetrieval(String imageUrl){
        //image retrieval from db
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new AddToStock.DownloadImageTask(productImageView).execute(imageUrl);
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

    public void setDetails(String prodName,String supplierName, double unitPrice, double productTax, double shippingPrice, int amount){
        productNameView.setText(prodName);
        supplierNameView.setText(supplierName);
        initialCostPrice = unitPrice + productTax + shippingPrice;

    }

    private void sendDataToProdInfoFragment(Fragment fragment)
    {
        //sending data to product info fragment
        Bundle b = new Bundle();
        b.putDouble("unitPrice", unitPrice);
        b.putDouble("productTax", tax);
        b.putDouble("shippingPrice", shipping);
        b.putInt("productAmount", productAmount);
        b.putDouble("initialCost", initialCostPrice);
        b.putString("product_id", productDocumentId);
        fragment.setArguments(b);
    }

    private void sendInfoToAddToStockFragment(Fragment fragment)
    {
        Bundle b = new Bundle();
        b.putDouble("unitPrice", unitPrice);
        b.putDouble("productTax", tax);
        b.putDouble("shippingPrice", shipping);
        b.putDouble("initialCost", initialCostPrice);
        b.putInt("productAmount", productAmount);
        b.putString("product_id", productDocumentId);
        fragment.setArguments(b);
    }

    private void sendDataToFragmentPurchase(Fragment fragment)
    {
        Bundle b = new Bundle();
        b.putDouble("unitPrice", unitPrice);
        b.putDouble("productTax", tax);
        b.putDouble("shippingPrice", shipping);
        b.putDouble("productAmount", productAmount);
        b.putDouble("initialCost", initialCostPrice);
        b.putInt("amount", productAmount);
        b.putString("product_id", productDocumentId);
        fragment.setArguments(b);
    }
    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(AddToStock.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}
