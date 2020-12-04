package com.example.shopmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class AddForSaleFragment extends Fragment {

    ImageButton subtractProdAmount;
    ImageButton addProdAmount;
    TextView prodAmount;
    Button addForSaleButton;

    TextView vatSellingPriceTV;

    double vatSP = 0.0;
    int productAmount = 0;

    CollectionReference ref;
    private FirebaseFirestore db;

    String userId;
    String documentId;
    String productId;
    Map<String, Object> map;

    String productImage;
    String productSupplier;
    String productName;
    int initialProductAmount=0;
    int finalProductAmountForSale=0;
    int numOfUnits=0;

    int amt=0;
    String storeItemDocumentId;
    boolean check=false;

    String checkAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_for_sale, container, false);

        subtractProdAmount = view.findViewById(R.id.subtractProdAmount);
        addProdAmount = view.findViewById(R.id.addProdAmount);
        prodAmount = view.findViewById(R.id.prodAmount);
        vatSellingPriceTV = view.findViewById(R.id.vatSellingPrice);
        addForSaleButton = view.findViewById(R.id.addForSaleButton);

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
            documentId = bundle.getString("documentId");
            documentId = bundle.getString("documentId");
            productId = bundle.getString("prodId");
            initialProductAmount=bundle.getInt("productAmount");
        }

        db = FirebaseFirestore.getInstance();
        grabPrice();

        addProdAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(prodAmount.getText().toString());
                numOfUnits = grabAmount();

                if (num != numOfUnits) {
                    num++;
                    prodAmount.setText(Integer.toString(num));
                }
            }
        });

        subtractProdAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(prodAmount.getText().toString());

                if (num != 0) {
                    num--;
                    prodAmount.setText(Integer.toString(num));
                }
            }
        });


        addForSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(prodAmount.getText().toString());
                if (num != 0) {
                    new MaterialAlertDialogBuilder(getActivity())
                            .setTitle("Confirm Add for Sale")
                            .setMessage("Are you sure you want to add this product for sale?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addToStoreCollection();
                                    changeProductAmount();
                                    grabAmount();
                                    Toast.makeText(getActivity(), "Product has been successfully added to your store!", Toast.LENGTH_SHORT).show();
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

        return view;

    }

    public void addToStoreCollection() {

        String productAmountForSale = prodAmount.getText().toString();
        finalProductAmountForSale=Integer.parseInt(productAmountForSale);

        String priceForSale = vatSellingPriceTV.getText().toString();

        Map<String, String> userMap = new HashMap<>();

        userMap.put("prodAmount", productAmountForSale);
        userMap.put("sellingPrice", priceForSale);

        userMap.put("productImage", productImage);
        userMap.put("supplierName", productSupplier);
        userMap.put("productName", productName);
        userMap.put("productId", productId);


        boolean chk;
        chk=checkIfItemExistsInStore();
        if(!chk) {
            db.collection("Users")
                    .document(userId)
                    .collection("Store")
                    .add(userMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //add to store
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        else
            incrementProductInStore(checkAmount,storeItemDocumentId);

    }

    public boolean checkIfItemExistsInStore(){

        ref = db.collection("Users")
                .document(userId)
                .collection("Store");

        //grabbing data from firestore to display in list
        ref.whereEqualTo("productId",productId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        checkAmount = map.get("prodAmount").toString();
                        storeItemDocumentId=document.getId();
                    }
                    check=true;

                }
                else {
                    System.out.println("Error getting documents: ");
                    check=false;
                }
            }
        });

        return check;

    }

    public void incrementProductInStore(String amount,String storeItemDocumentId){
        Map<String, Integer> priceMap=new HashMap<>();

        int productAmount=Integer.parseInt(amount);
        productAmount++;
        priceMap.put("prodAmount",productAmount);

        if(productAmount!=0){
            db.collection("Users")
                    .document(userId)
                    .collection("Store")
                    .document(storeItemDocumentId)
                    .set(priceMap, SetOptions.merge());
        }else
            System.out.println("Error");

    }


    public void grabPrice(){

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
                        String finalPrice = map.get("sellingPrice").toString();
                        String amt = map.get("productAmount").toString();
                        productImage = map.get("productImage").toString();
                        productSupplier = map.get("supplierName").toString();
                        productName = map.get("productName").toString();

                        if(!finalPrice.isEmpty())
                        vatSP=Double.parseDouble(finalPrice);
                        productAmount=Integer.parseInt(amt);

                        vatSellingPriceTV.setText(Double.toString(vatSP));
                        prodAmount.setText(Integer.toString(productAmount));


                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });

    }

    public void changeProductAmount(){

        initialProductAmount=initialProductAmount-finalProductAmountForSale;
        Map<String, Integer> amountMap=new HashMap<>();
        amountMap.put("productAmount",initialProductAmount);

        db.collection("Users")
                .document(userId)
                .collection("Stock")
                .document(documentId)
                .set(amountMap, SetOptions.merge());

    }

    public int grabAmount(){

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
                        String amount = map.get("productAmount").toString();
                        amt=Integer.parseInt(amount);

                    }
                } else {
                    System.out.println("Error getting documents: ");
                }
            }
        });

        return amt;

    }



}

