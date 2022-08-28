package com.example.apphienmau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.window.SplashScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
              if (user != null){
                  Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                  startActivity(intent);
                  finish();
              }else {
                  Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                  startActivity(intent);
                  finish();
              }
            }
        }, 3000);
    }

}