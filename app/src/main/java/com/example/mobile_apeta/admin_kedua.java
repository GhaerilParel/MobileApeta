package com.example.mobile_apeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                SharedPreferences sharedPreferences = getSharedPreferences("TokoData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", emailToko.getText().toString());
                editor.putString("nama", namaToko.getText().toString());
                editor.putString("alamat", alamatToko.getText().toString());
                editor.putString("telepon", teleponToko.getText().toString());
                editor.apply();
                aksiRegistToko();
            }
        });
    }

    public void aksiRegistToko(){
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-drzkm/endpoint/insertToko";

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
                                Toast.makeText(admin_kedua.this, "Unexpected responnse format", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
//                            Log.e("Registration", "Error parsing JSON: " + e.getMessage());
////                            Toast.makeText(SignUp.this, "Registration failed! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(admin_kedua.this, "Toko Berhasil Terdaftar!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(admin_kedua.this, admin_ketiga.class);
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
                params.put("nama_toko", namaToko.getText().toString());
                params.put("alamat_toko", alamatToko.getText().toString());
                params.put("no_tlpn", teleponToko.getText().toString());
                params.put("email_toko", emailToko.getText().toString());
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
            Toast.makeText(admin_kedua.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(admin_kedua.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}
