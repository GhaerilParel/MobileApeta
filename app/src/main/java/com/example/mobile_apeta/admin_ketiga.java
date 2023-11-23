package com.example.mobile_apeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class admin_ketiga extends AppCompatActivity {
    private Spinner spinnerKategori;
    private EditText namaProduk, deskripsiProduk, hargaProduk;
    private String namaToko, alamatToko;
    Button btnsaveProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ketiga);
        namaProduk = findViewById(R.id.namaProduk);
        deskripsiProduk = findViewById(R.id.deskripsiProduk);
        hargaProduk = findViewById(R.id.hargaProduk);
        btnsaveProduk = findViewById(R.id.simpanProduk);
        SharedPreferences sharedPreferences = getSharedPreferences("TokoData", MODE_PRIVATE);
        namaToko = sharedPreferences.getString("nama", "");
        alamatToko = sharedPreferences.getString("alamat", "");

        // Inisialisasi Spinner dari layout XML
        spinnerKategori = findViewById(R.id.spinnerKategori);

        // Buat array untuk menyimpan pilihan kategori
        String[] kategori = {"Ayam", "Sapi", "Kambing"};

        // Buat adapter untuk Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategori);

        // Set tata letak dropdown yang akan digunakan adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter ke Spinner
        spinnerKategori.setAdapter(adapter);
        btnsaveProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("ProdukData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String selectedKategori = spinnerKategori.getSelectedItem().toString();
                editor.putString("kategoriProduk", selectedKategori);
                editor.putString("namaProduk", namaProduk.getText().toString());
                editor.putString("deskripsiProduk", deskripsiProduk.getText().toString());
                editor.putString("hargaProduk", hargaProduk.getText().toString());
                editor.apply();
                aksiTambahProduk();
            }
        });
    }

    public void aksiTambahProduk() {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-drzkm/endpoint/insertProduk";

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Regist Toko", "Raw Server Response: " + response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success")) {
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    ///.....
                                }
                            } else {
                                Log.e("Regist Toko", "Unexpected response format: " + response);
                                Toast.makeText(admin_ketiga.this, "Unexpected responnse format", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
//                            Log.e("Registration", "Error parsing JSON: " + e.getMessage());
////                            Toast.makeText(SignUp.this, "Registration failed! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(admin_ketiga.this, "Toko Berhasil Terdaftar!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(admin_ketiga.this, profil_mitra.class);
                        startActivity(i);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleVolleyError(error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_produk", namaProduk.getText().toString());
                params.put("deskripsi_produk", deskripsiProduk.getText().toString());
                params.put("harga_produk", hargaProduk.getText().toString());
                params.put("jenis_produk", spinnerKategori.getSelectedItem().toString());
                params.put("nama_toko", namaToko);
                params.put("alamat_toko", alamatToko);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }
    private void handleVolleyError(VolleyError error) {
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            String errorMessage = new String(error.networkResponse.data);
            Log.e("Registration", "Error: " + statusCode + ", Response: " + errorMessage);
            Toast.makeText(admin_ketiga.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(admin_ketiga.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}
