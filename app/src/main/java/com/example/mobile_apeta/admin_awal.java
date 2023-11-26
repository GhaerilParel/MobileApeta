package com.example.mobile_apeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class admin_awal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_awal);

        Button btnPendaftaran = findViewById(R.id.btn1);

        btnPendaftaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("isRegistered", false)) {
                    // User is already registered, open profil_mitra directly
                    Intent profileIntent = new Intent(admin_awal.this, profil_mitra.class);
                    startActivity(profileIntent);
                } else {
                    // User is not registered, open admin_kedua for registration
                    Intent i = new Intent(admin_awal.this, admin_kedua.class);
                    startActivity(i);
                }
            }
        });
    }
}