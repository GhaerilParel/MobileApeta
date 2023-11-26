package com.example.mobile_apeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class ubah_profil extends AppCompatActivity {
    private EditText editTextUsername, editTextNama, editTextEmail, editTextNoHp, editTextTanggalLahir, editTextId;
    private RadioButton radioButtonLakiLaki, radioButtonPerempuan;
    private Button btn_editt;
    private RadioGroup radioGroupJenisKelamin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);

        // Inisialisasi komponen UI
        editTextUsername = findViewById(R.id.ubahUsername);
        editTextNama = findViewById(R.id.ubahNama);
        editTextEmail = findViewById(R.id.ubahEmail);
        editTextNoHp = findViewById(R.id.ubahNoHp);
        editTextTanggalLahir = findViewById(R.id.ubahTanggalLahir);
        radioButtonLakiLaki = findViewById(R.id.lakilaki);
        radioButtonPerempuan = findViewById(R.id.perempuan);
        btn_editt = findViewById(R.id.btn_edit);
        editTextId = findViewById(R.id.IdData);
        radioGroupJenisKelamin = findViewById(R.id.radioGroupJenisKelamin);
        ImageButton btn_homebar = findViewById(R.id.homebar6);
        ImageButton btn_mitrabar = findViewById(R.id.btn_mitra6);
        ImageButton btn_marketbar = findViewById(R.id.btn_market6);
        ImageButton btn_keranjangbar = findViewById(R.id.btn_keranjang6);
        ImageButton btn_profilebar = findViewById(R.id.btn_profil6);

        btn_homebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ubah_profil.this, home.class);
                startActivity(i);
            }
        });
        btn_mitrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("isRegistered", false)) {
                    // User is already registered, open profil_mitra directly
                    Intent profileIntent = new Intent(ubah_profil.this, profil_mitra.class);
                    startActivity(profileIntent);
                } else {
                    // User is not registered, open admin_awal for registration
                    Intent i2 = new Intent(ubah_profil.this, admin_awal.class);
                    startActivity(i2);
                }
            }
        });
        btn_marketbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(ubah_profil.this, market.class);
                startActivity(i3);
            }
        });
        btn_keranjangbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(ubah_profil.this, chatbot.class);
                startActivity(i4);
            }
        });
        btn_profilebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(ubah_profil.this, profil_user.class);
                startActivity(i5);
            }
        });


        // Mendapatkan data dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData2", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String nama = sharedPreferences.getString("nama", "");
        String email = sharedPreferences.getString("email", "");
        String noHp = sharedPreferences.getString("no_hp", "");
        String jenisKelamin = sharedPreferences.getString("jenis_kelamin", "");
        String tanggalLahir = sharedPreferences.getString("tanggal_lahir", "");
        String id = sharedPreferences.getString("_id", "");

        // Mengatur nilai EditText dengan data dari SharedPreferences
        editTextUsername.setText(username);
        editTextNama.setText(nama);
        editTextEmail.setText(email);
        editTextNoHp.setText(noHp);
        editTextTanggalLahir.setText(tanggalLahir);
        editTextId.setText(id);
        editTextId.setVisibility(View.INVISIBLE);

        // Mengatur RadioButton sesuai dengan jenis kelamin
        if (jenisKelamin.equalsIgnoreCase("Laki-Laki")) {
            radioButtonLakiLaki.setChecked(true);
            radioButtonPerempuan.setChecked(false);
        } else if (jenisKelamin.equalsIgnoreCase("Perempuan")) {
            radioButtonLakiLaki.setChecked(false);
            radioButtonPerempuan.setChecked(true);
        }

        btn_editt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aksiUpdateData(id);
            }
        });
    }

    public void aksiUpdateData(String id) {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-drzkm/endpoint/editPengguna?id=" + id;

        StringRequest sr = new StringRequest(
                Request.Method.PUT,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Registration", "Raw Server Response: " + response);

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success")) {
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    // Save user data to SharedPreferences

                                }

                            } else {
                                Log.e("Registration", "Unexpected response format: " + response);
                                Toast.makeText(ubah_profil.this, "Unexpected response format", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
//                            Log.e("Registration", "Error parsing JSON: " + e.getMessage());
//                            Toast.makeText(SignUp.this, "Registration failed! Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(ubah_profil.this, "Data Berhasil Terupdate!", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(ubah_profil.this, profil_user.class);
                        startActivity(loginIntent);
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
                params.put("_id", editTextId.getText().toString());
                params.put("username", editTextUsername.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                params.put("nama", editTextNama.getText().toString());
                params.put("no_hp", editTextNoHp.getText().toString());
                params.put("tanggal_lahir", editTextTanggalLahir.getText().toString());
                String jenisKelamin = radioButtonLakiLaki.isChecked() ? "Laki-Laki" : "Perempuan";
                params.put("jenis_kelamin", jenisKelamin);
                params.put("jenis_kelamin", jenisKelamin);
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
            Toast.makeText(ubah_profil.this, "Error: " + statusCode, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ubah_profil.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}
