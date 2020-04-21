package com.example.hrithik.googleauth;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    private int SPLASH_SCREEN_LENGTH=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent= new Intent(SplashActivity.this, AuthActivity.class);
                startActivity(myIntent);
                finish();
            }
        },SPLASH_SCREEN_LENGTH);
    }
}
