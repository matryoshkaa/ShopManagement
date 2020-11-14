package com.example.shopmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends Activity {

    private static int SPLASH_SCREEN= 5000;

    //variables
    Animation topAnim,bottomAnim;
    ImageView logo;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //making sure user is logged out
        FirebaseAuth.getInstance().signOut();

        //Animations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.font_anim);

        logo=findViewById(R.id.logo);
        appName=findViewById(R.id.lien);

        logo.setAnimation(topAnim);
        appName.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,SignIn.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}