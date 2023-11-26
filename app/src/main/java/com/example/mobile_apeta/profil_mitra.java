package com.example.mobile_apeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class profil_mitra extends AppCompatActivity {
    TextView namaToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_mitra);

        namaToko = findViewById(R.id.namaToko2); // Ganti dengan id TextView yang sesuai di XML layout Anda

        // Mengambil nilai dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("TokoData", MODE_PRIVATE);
        String nilaiNamaProduk = sharedPreferences.getString("nama", "");

        // Menetapkan nilai dari SharedPreferences ke TextView
        namaToko.setText(nilaiNamaProduk);
    }
}
