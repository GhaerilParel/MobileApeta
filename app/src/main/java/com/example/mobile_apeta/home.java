package com.example.mobile_apeta;
import com.example.mobile_apeta.R;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                Intent i = new Intent(home.this, home.class);
                                startActivity(i);
                                return true;
                            case R.id.mitra:
                                Intent i2 = new Intent(home.this, admin_awal.class);
                                startActivity(i2);
                                return true;
                            case R.id.market:
                                // Tindakan saat item "Market" dipilih
                                // Ganti kode di sini sesuai dengan tindakan yang diinginkan
                                return true;
                            case R.id.profil:
                                Intent i3 = new Intent(home.this, ubah_profil.class);
                                startActivity(i3);
                                return true;
                        }
                        return false;
                    }
                });
    }
}
