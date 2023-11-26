package com.example.mobile_apeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class profil_mitra extends AppCompatActivity {
    TextView namaToko;
    private RecyclerView recyclerViewProductA;
    private RecyclerView recyclerViewProductB;
    private ProdukAdapter adapterProductA;
    private ProdukAdapter adapterProductB;
    private List<ProdukModels> productListA;
    private List<ProdukModels> productListB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_mitra);

        namaToko = findViewById(R.id.namaToko2); // Ganti dengan id TextView yang sesuai di XML layout Anda

        // Mengambil nilai dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("TokoData", MODE_PRIVATE);
        String nilaiNamaToko = sharedPreferences.getString("nama", "");

        // Menetapkan nilai dari SharedPreferences ke TextView
        namaToko.setText(nilaiNamaToko);

        ImageButton btn_homebar = findViewById(R.id.homebar4);
        ImageButton btn_mitrabar = findViewById(R.id.btn_mitra4);
        ImageButton btn_marketbar = findViewById(R.id.btn_market4);
        ImageButton btn_keranjangbar = findViewById(R.id.btn_keranjang4);
        ImageButton btn_profilebar = findViewById(R.id.btn_profil4);
        TextView btn_tambahProduk = findViewById(R.id.tambahProduk);

        btn_tambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambahDataProduk = new Intent(profil_mitra.this, admin_ketiga.class);
                startActivity(tambahDataProduk);
            }
        });

        btn_homebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profil_mitra.this, home.class);
                startActivity(i);
            }
        });
        btn_mitrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("isRegistered", false)) {
                    // User is already registered, open profil_mitra directly
                    Intent profileIntent = new Intent(profil_mitra.this, profil_mitra.class);
                    startActivity(profileIntent);
                } else {
                    // User is not registered, open admin_awal for registration
                    Intent i2 = new Intent(profil_mitra.this, admin_awal.class);
                    startActivity(i2);
                }
            }
        });
        btn_marketbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(profil_mitra.this, market.class);
                startActivity(i3);
            }
        });
        btn_keranjangbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(profil_mitra.this, chatbot.class);
                startActivity(i4);
            }
        });
        btn_profilebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(profil_mitra.this, profil_user.class);
                startActivity(i5);
            }
        });

        recyclerViewProductA = findViewById(R.id.ProductA2);
        recyclerViewProductB = findViewById(R.id.ProductB2);
        // Set layout managers
        recyclerViewProductA.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewProductB.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize product lists
        productListA = new ArrayList<>();
        productListB = new ArrayList<>();

        // Initialize adapters
        adapterProductA = new ProdukAdapter(productListA, this);
        adapterProductB = new ProdukAdapter(productListB, this);

        // Set adapters for RecyclerViews
        recyclerViewProductA.setAdapter(adapterProductA);
        recyclerViewProductB.setAdapter(adapterProductB);
        fetchDataFromUrl(nilaiNamaToko);
    }

    private void fetchDataFromUrl(String nilaiNamaToko) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-drzkm/endpoint/getDataProdukByToko?nama_toko=" + nilaiNamaToko;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                ProdukModels produk = new ProdukModels();
                                produk.set_id(jsonObject.getString("_id"));
                                produk.setNama_produk(jsonObject.getString("nama_produk"));
                                produk.setHarga_produk(jsonObject.getInt("harga_produk"));
                                produk.setNama_toko(jsonObject.getString("nama_toko"));

                                // Determine which list to add the product to (ProductA or ProductB)
                                // Based on some condition like 'i' being even or odd, or any other logic
                                if (i % 2 == 0) {
                                    productListA.add(produk);
                                } else {
                                    productListB.add(produk);
                                }
                            }

                            // Notify the adapters that the data has changed
                            adapterProductA.notifyDataSetChanged();
                            adapterProductB.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("VolleyError", "Error fetching data: " + error.getMessage());
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
