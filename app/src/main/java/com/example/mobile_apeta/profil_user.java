package com.example.mobile_apeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class profil_user extends AppCompatActivity {
    TextView namaUser, namaUser2, emailUser, noHpUser, jenisKelamin, tanggalLahir, editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);
        ImageButton btn_homebar = findViewById(R.id.homebar3);
        ImageButton btn_mitrabar = findViewById(R.id.btn_mitra3);
        ImageButton btn_marketbar = findViewById(R.id.btn_market3);
        ImageButton btn_keranjangbar = findViewById(R.id.btn_keranjang3);
        ImageButton btn_profilebar = findViewById(R.id.btn_profil3);
        Button btn_logout = findViewById(R.id.btnLogout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user data from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Redirect to the login activity
                Intent i = new Intent(profil_user.this, login.class);
                startActivity(i);
                finish(); // Finish the current activity
            }
        });

        btn_homebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profil_user.this, home.class);
                startActivity(i);
            }
        });
        btn_mitrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("isRegistered", false)) {
                    // User is already registered, open profil_mitra directly
                    Intent profileIntent = new Intent(profil_user.this, profil_mitra.class);
                    startActivity(profileIntent);
                } else {
                    // User is not registered, open admin_awal for registration
                    Intent i2 = new Intent(profil_user.this, admin_awal.class);
                    startActivity(i2);
                }
            }
        });
        btn_marketbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(profil_user.this, market.class);
                startActivity(i3);
            }
        });
        btn_keranjangbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(profil_user.this, chatbot.class);
                startActivity(i4);
            }
        });
        btn_profilebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(profil_user.this, profil_user.class);
                startActivity(i5);
            }
        });
        namaUser = findViewById(R.id.namaUser);
        namaUser2 = findViewById(R.id.namaUser2);
        emailUser = findViewById(R.id.emailUser);
        noHpUser = findViewById(R.id.noHpUser);
        jenisKelamin = findViewById(R.id.jenisKelamin);
        tanggalLahir = findViewById(R.id.tanggalLahir);
        editProfile = findViewById(R.id.editProfile);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");
        getDataByEmail(email);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profil_user.this, ubah_profil.class);
                startActivity(i);
            }
        });
    }

    public void getDataByEmail(String email) {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-drzkm/endpoint/getUserByEmail?email=" + email;

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
                                if (userJson.has("email") && userJson.has("username")) {
                                    String id = userJson.getString("_id");
                                    String username = userJson.getString("username");
                                    String retrievedEmail = userJson.getString("email");

                                    // Check and handle empty or null values for no_hp, jenis_kelamin, and tanggal_lahir
                                    String nama = userJson.optString("nama", "");
                                    String noHp = userJson.optString("no_hp", "");
                                    String jkelamin = userJson.optString("jenis_kelamin", "");
                                    String tanggallahir = userJson.optString("tanggal_lahir", "");

                                    // Set the data to the TextViews
                                    namaUser.setText(nama);
                                    namaUser2.setText(username);
                                    emailUser.setText(retrievedEmail);
                                    noHpUser.setText(noHp);
                                    jenisKelamin.setText(jkelamin);
                                    tanggalLahir.setText(tanggallahir);

                                    SharedPreferences.Editor editor = getSharedPreferences("UserData2", MODE_PRIVATE).edit();
                                    editor.putString("_id", id);
                                    editor.putString("nama", nama);
                                    editor.putString("username", username);
                                    editor.putString("email", retrievedEmail);
                                    editor.putString("no_hp", noHp);
                                    editor.putString("jenis_kelamin", jkelamin);
                                    editor.putString("tanggal_lahir", tanggallahir);
                                    editor.apply();

                                } else {
                                    // Log an error if the expected fields are not present in the JSON response
                                    Log.e("Data", "JSON response is missing expected fields");
                                    Toast.makeText(profil_user.this, "Login Unsuccessful! Unexpected response format", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("Data", "Empty JSON array");
                                Toast.makeText(profil_user.this, "Login Unsuccessful! Empty JSON array", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // Log the exception for debugging
                            Log.e("Data", "Error parsing JSON: " + e.getMessage());
                            Toast.makeText(profil_user.this, "Login Unsuccessful! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            Toast.makeText(profil_user.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(profil_user.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}