package com.example.mobile_apeta;

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
import android.widget.EditText;

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
        String url = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-fjrfb/endpoint/getAllDataObat";

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

}
