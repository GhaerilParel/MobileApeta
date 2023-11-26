package com.example.mobile_apeta;
import com.example.mobile_apeta.R;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView btn_homebar = findViewById(R.id.btn_homebar);
        ImageView btn_mitrabar = findViewById(R.id.btn_mitra);
        ImageView btn_marketbar = findViewById(R.id.btn_market);
        ImageView btn_keranjangbar = findViewById(R.id.btn_keranjang);
        ImageView btn_profilebar = findViewById(R.id.btn_profil);

        btn_homebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this, home.class);
                startActivity(i);
            }
        });
        btn_mitrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("isRegistered", false)) {
                    // User is already registered, open profil_mitra directly
                    Intent profileIntent = new Intent(home.this, profil_mitra.class);
                    startActivity(profileIntent);
                } else {
                    // User is not registered, open admin_awal for registration
                    Intent i2 = new Intent(home.this, admin_awal.class);
                    startActivity(i2);
                }
            }
        });
        btn_marketbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(home.this, market.class);
                startActivity(i3);
            }
        });
        btn_keranjangbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(home.this, chatbot.class);
                startActivity(i4);
            }
        });
        btn_profilebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(home.this, profil_user.class);
                startActivity(i5);
            }
        });
    }
}
