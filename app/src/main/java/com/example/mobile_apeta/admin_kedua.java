package com.example.mobile_apeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class admin_kedua extends AppCompatActivity {

    EditText emailToko, namaToko, alamatToko, teleponToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kedua);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        emailToko = findViewById(R.id.emailToko);
        namaToko = findViewById(R.id.namaToko);
        alamatToko = findViewById(R.id.alamatToko);
        teleponToko = findViewById(R.id.teleponToko);
        Button btn_kembali = findViewById(R.id.btnback);
        Button btn_lanjut = findViewById(R.id.btnnext);

        emailToko.setText(email);

        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", emailToko.getText().toString());
                editor.putString("nama", namaToko.getText().toString());
                editor.putString("alamat", alamatToko.getText().toString());
                editor.putString("telepon", teleponToko.getText().toString());
                editor.apply();

                Intent i = new Intent(admin_kedua.this, admin_ketiga.class);
                startActivity(i);
            }
        });
    }
}
