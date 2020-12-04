package com.example.shopmanagement;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class ProductId {
    public String productId;

    public <T extends ProductId> T withId(@NonNull final String id){
        this.productId = id;
        return (T)this;
    }


}
