package com.example.mobile_apeta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    private static final int SPLASH_SCREEN_DURATION = 3000; // Durasi Splash Screen dalam milidetik (3 detik)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(splash.this, tampilan_awal.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_SCREEN_DURATION);
    }
}