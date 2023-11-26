package com.example.mobile_apeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log; // Tambahkan ini jika Anda menggunakan Log
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError; // Tambahkan ini jika Anda menggunakan VolleyError


public class market extends AppCompatActivity {

    private RecyclerView recyclerViewProductA;
    private RecyclerView recyclerViewProductB;
    private ProdukAdapter adapterProductA;
    private ProdukAdapter adapterProductB;
    private List<ProdukModels> productListA;
    private List<ProdukModels> productListB;
    private EditText searchProduk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        ImageButton btn_homebar = findViewById(R.id.homebar2);
        ImageButton btn_mitrabar = findViewById(R.id.btn_mitra2);
        ImageButton btn_marketbar = findViewById(R.id.btn_market2);
        ImageButton btn_keranjangbar = findViewById(R.id.btn_keranjang2);
        ImageButton btn_profilebar = findViewById(R.id.btn_profil2);

        btn_homebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(market.this, home.class);
                startActivity(i);
            }
        });
        btn_mitrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("isRegistered", false)) {
                    // User is already registered, open profil_mitra directly
                    Intent profileIntent = new Intent(market.this, profil_mitra.class);
                    startActivity(profileIntent);
                } else {
                    // User is not registered, open admin_awal for registration
                    Intent i2 = new Intent(market.this, admin_awal.class);
                    startActivity(i2);
                }
            }
        });
        btn_marketbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(market.this, market.class);
                startActivity(i3);
            }
        });
        btn_keranjangbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(market.this, chatbot.class);
                startActivity(i4);
            }
        });
        btn_profilebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(market.this, profil_user.class);
                startActivity(i5);
            }
        });

        // Initialize RecyclerViews
        recyclerViewProductA = findViewById(R.id.ProductA);
        recyclerViewProductB = findViewById(R.id.ProductB);
        searchProduk = findViewById(R.id.searchProduk);

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
        fetchDataFromUrl();
        adapterProductA.setOnItemClickListener(new ProdukAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProdukModels clickedProduk) {
                showProductDetails(clickedProduk);
            }
        });

        // Handle item click in RecyclerView B
        adapterProductB.setOnItemClickListener(new ProdukAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProdukModels clickedProduk) {
                showProductDetails(clickedProduk);
            }
        });

        searchProduk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void fetchDataFromUrl() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-drzkm/endpoint/getAllProduk";

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
    private void filter(String text) {
        List<ProdukModels> filteredListA = new ArrayList<>();
        List<ProdukModels> filteredListB = new ArrayList<>();

        for (ProdukModels produk : productListA) {
            if (produk.getNama_produk().toLowerCase().contains(text.toLowerCase())) {
                filteredListA.add(produk);
            }
        }
        adapterProductA.filterList(filteredListA);

        for (ProdukModels produk : productListB) {
            if (produk.getNama_produk().toLowerCase().contains(text.toLowerCase())) {
                filteredListB.add(produk);
            }
        }
        adapterProductB.filterList(filteredListB);
    }
    private void showProductDetails(ProdukModels clickedProduk) {
        Intent intent = new Intent(this, produk_details.class);
        intent.putExtra("productId", clickedProduk.get_id());
        intent.putExtra("productName", clickedProduk.getNama_produk());
        intent.putExtra("productPrice", clickedProduk.getHarga_produk());
        intent.putExtra("tokoName", clickedProduk.getNama_toko());
        intent.putExtra("productDescription", clickedProduk.getDeskripsi_produk());
        // Add any other data you want to pass

        startActivity(intent);
    }

}
