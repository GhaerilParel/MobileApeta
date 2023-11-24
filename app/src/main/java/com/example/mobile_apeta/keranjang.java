package com.example.mobile_apeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class keranjang extends AppCompatActivity {
    private Spinner metodePembayaran;
    private String no_hp, alamatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        // Retrieve data from Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        int productPrice = intent.getIntExtra("productPrice", 0);
        int quantity = intent.getIntExtra("quantity", 0);
        int subtotal = intent.getIntExtra("totalPrice", 0);

        // Calculate total
        int total = subtotal; // You can add additional logic for tax, shipping, etc. if needed

        // Find views by their IDs
        TextView productNameTextView = findViewById(R.id.productName3);
        TextView productPriceTextView = findViewById(R.id.productPrice3);
        TextView quantityTextView = findViewById(R.id.quantityEditText2);
        TextView subtotalTextView = findViewById(R.id.perhitungan);
        TextView totalPembayaranTextView = findViewById(R.id.totalPembayaran);
        metodePembayaran = findViewById(R.id.metode_pembayaran);

        // Get username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String emailakun = sharedPreferences.getString("email","");
        getDataByUsername(username);

        // Fetch data from the getPetaniByUsername endpoint using Volley

        // Set data to views
        productNameTextView.setText(productName);
        productPriceTextView.setText("Harga Item : Rp" + productPrice);
        quantityTextView.setText("Jumlah Item : " + quantity);
        subtotalTextView.setText("Subtotal : Rp" + subtotal);
        totalPembayaranTextView.setText("Total Belanja : Rp" + total);

        // Implement your spinner logic here for the metode_pembayaran Spinner
        String[] pembayaranOption = {"BCA", "Mandiri", "BRI", "BNI", "OVO", "GOPAY"};
        ArrayAdapter<String> pembayaranAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pembayaranOption);
        metodePembayaran.setAdapter(pembayaranAdapter);

        metodePembayaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the Spinner
                String selectedPaymentMethod = pembayaranOption[position];

                // Find the TextView for displaying the selected payment method
                TextView metodePembayaranTextView = findViewById(R.id.metode_pembayaran2);

                // Set the selected payment method to the TextView
                metodePembayaranTextView.setText("Metode Pembayaran : " + selectedPaymentMethod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        Button btnKonfirmasi = findViewById(R.id.btn_konfirm);
        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get all data for email
                String emailMessage = "Detail Pembayaran:\n" +
                        "Nama Produk: " + productName + "\n" +
                        "Harga Item: Rp" + productPrice + "\n" +
                        "Jumlah Item: " + quantity + "\n" +
                        "Subtotal: Rp" + subtotal + "\n" +
                        "Total Belanja: Rp" + total + "\n" +
                        "Metode Pembayaran: " + metodePembayaran.getSelectedItem().toString();

                // Include data from the user's account
                emailMessage += "\n\nData Pembeli:\n" +
                        "Username: " + username + "\n" +
                        "Email: " + emailakun + "\n" +
                        "No Hp: " + no_hp + "\n" +
                        "Alamat: " + alamatt;

                // Get the recipient email address
                String recipientEmail = emailakun;
                String phoneNumber = "6285779410576"; // Nomor WhatsApp yang akan dikirimkan pesan konfirmasi
                String message = "Halo! Saya ingin melakukan konfirmasi pesanan dengan detail berikut:\n" + emailMessage;

                // Buat Intent untuk membuka WhatsApp
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                whatsappIntent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message));

                // Periksa apakah WhatsApp terinstall pada perangkat
                PackageManager packageManager = getPackageManager();
                if (whatsappIntent.resolveActivity(packageManager) != null) {
                    startActivity(whatsappIntent);
                } else {
                    // Jika WhatsApp tidak terinstall, tampilkan pesan
                    Toast.makeText(keranjang.this, "WhatsApp tidak terinstall pada perangkat ini", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getDataByUsername(String username) {
        String urlEndPoints = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-fjrfb/endpoint/getPetaniByUsername?username=" + username;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                urlEndPoints,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Akun", "Raw Server Response: " + response);

                        try {
                            JSONArray userArray = new JSONArray(response);

                            if (userArray.length() > 0) {
                                JSONObject userJson = userArray.getJSONObject(0); // Get the first object in the array

                                // Check if the JSON response contains expected fields
                                if (userJson.has("username")) {
                                    String retrievedUsername = userJson.getString("username");
                                    String email = userJson.getString("email");
                                    String noHp = userJson.getString("no_hp");
                                    String alamat = userJson.getString("alamat");

                                    no_hp = noHp;
                                    alamatt = alamat;

                                } else {
                                    // Log an error if the expected fields are not present in the JSON response
                                    Log.e("Data", "JSON response is missing expected fields");
                                    Toast.makeText(keranjang.this, "Login Unsuccessful! Unexpected response format", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("Data", "Empty JSON array");
                                Toast.makeText(keranjang.this, "Login Unsuccessful! Empty JSON array", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Log the exception for debugging
                            Log.e("Data", "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(keranjang.this, "Login Unsuccessful! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            Toast.makeText(keranjang.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(keranjang.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}