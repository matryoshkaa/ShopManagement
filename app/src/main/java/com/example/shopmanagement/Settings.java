package com.example.shopmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFirebaseAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        @SuppressLint("UseCompatLoadingForDrawables") final Drawable goBack = getResources().getDrawable(R.drawable.back_button);
        getSupportActionBar().setHomeAsUpIndicator(goBack);

        //go back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void goToTermsAndCondition (View view){
        startActivity(new Intent(this, TermsAndCondition.class));
    }

    public void goToFeedbackAndSupport (View view){
        startActivity(new Intent(this, FeedbackAndSupport.class));
    }

    public void goToProfile (View view){
        startActivity(new Intent(this, UserProfile.class));
    }

    public void logout (View view){
        mFirebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, SignIn.class));

    }


}