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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    ImageView prodImageView;
    ImageButton addToStockBtn;
    String productName;
    String supplierName;
    String imageUrl;
    double unitPrice;
//    double sellingPrice;
    double tax;
    double shipping;
    int productAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_stock);

        Intent i = getIntent();
        String productId = i.getStringExtra("product_id");

        productNameView = findViewById(R.id.productNameAddView);
        supplierNameView = findViewById(R.id.supplierNameAddView);
        prodImageView = findViewById(R.id.prodImageAddView);
        addToStockBtn = (ImageButton) findViewById(R.id.AddtoStockButton);

        mFireStore = FirebaseFirestore.getInstance();
        mFireStore.collection("Products").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                    setDetails(productName, supplierName);
                }
                else{
                    Log.d(TAG, "Error" + task.getException().getMessage());
                }
            }
        });
    }

    private void imageRetrieval(String imageUrl){
        //image retrieval from db
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new AddToStock.DownloadImageTask(prodImageView).execute(imageUrl);
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

    public void setDetails(String prodName,String supplierName){

        productNameView.setText(prodName);
        supplierNameView.setText(supplierName);

    }

    public void goToSettings (View view){
        Intent i=new Intent();
        startActivity(new Intent(AddToStock.this, Settings.class));
    }

    public void goBack (View view){
        finish();
    }
}
