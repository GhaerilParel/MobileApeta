package com.example.mobile_apeta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class produk_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_details);


        // Find views by their IDs
        ImageView productImageView = findViewById(R.id.gambar);
        TextView productNameTextView = findViewById(R.id.namaToko2);
        TextView productPriceTextView = findViewById(R.id.hargaProduk2);
        EditText quantityEditText = findViewById(R.id.quantityEditText);
        TextView productDescriptionTextView = findViewById(R.id.deskripsiProduk2);
        TextView tokoNameTextView = findViewById(R.id.namaToko3);
        TextView buyButton = findViewById(R.id.buyButton);

        // Get data from Intent
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        String productName = intent.getStringExtra("productName");
        int productPrice = intent.getIntExtra("productPrice", 0);
        String productImage = intent.getStringExtra("productImage");
        String tokoName = intent.getStringExtra("tokoName");
        String productDescription = intent.getStringExtra("productDescription");

        productDescription = productDescription.replace("<br>", "\n");

        // Set data to views
        // Assuming you have a method to load an image from a URL, replace it with your implementation

        productImageView.setImageDrawable(getDrawable(R.drawable.logo)); // Replace with actual image loading code

        productNameTextView.setText(productName);
        productPriceTextView.setText("Rp" + productPrice);
        productDescriptionTextView.setText(productDescription);
        tokoNameTextView.setText(tokoName);

        // Handle the "Beli" button click event if needed
        buyButton.setOnClickListener(view -> {
            // Calculate total price based on quantity
            int quantity = Integer.parseInt(quantityEditText.getText().toString());
            int totalPrice = productPrice * quantity;

            // Pass data to DetailPembayaran activity
            Intent pembayaranIntent = new Intent(produk_details.this, keranjang.class);
            pembayaranIntent.putExtra("productName", productName);
            pembayaranIntent.putExtra("productPrice", productPrice);
            pembayaranIntent.putExtra("quantity", quantity);
            pembayaranIntent.putExtra("totalPrice", totalPrice);

            startActivity(pembayaranIntent);
        });
    }
}

