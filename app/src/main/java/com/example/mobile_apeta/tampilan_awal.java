package com.example.mobile_apeta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class tampilan_awal extends AppCompatActivity {
    private Button btn_masuk, btn_daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilan_awal);
        btn_masuk = findViewById(R.id.btn1);
        btn_daftar = findViewById(R.id.btn2);

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(tampilan_awal.this, login.class);
                startActivity(i);
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(tampilan_awal.this, register.class);
                startActivity(i);
            }
        });
    }
}