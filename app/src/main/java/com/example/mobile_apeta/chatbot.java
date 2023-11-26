package com.example.mobile_apeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class chatbot extends AppCompatActivity {

    private Spinner namaBuahSpinner;
    private Spinner tanamanBuahSpinner;
    private Spinner permasalahanBuahSpinner;
    private TextView hasilGptTextView, btn_home, tv_rekomendasi_obat;

    private ImageView homebar, produkbar, akunbar;
    private RecyclerView recyclerView;
    private ProdukAdapter produkAdapter;
    private List<ProdukModels> produkList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Initialize views
        namaBuahSpinner = findViewById(R.id.nama_buah);
        tanamanBuahSpinner = findViewById(R.id.tanaman_buah);
        permasalahanBuahSpinner = findViewById(R.id.permasalahan_buah);
        hasilGptTextView = findViewById(R.id.hasil_gpt);
        tv_rekomendasi_obat = findViewById(R.id.tv_rekomendasi_obat);
        recyclerView = findViewById(R.id.recyclerView3); // Pastikan id recyclerView sudah ada di layout XML Anda
        produkAdapter = new ProdukAdapter(new ArrayList<>(), this);
        produkList = new ArrayList<>();
        produkAdapter = new ProdukAdapter(produkList, this);
        ImageButton btn_homebar = findViewById(R.id.homebar5);
        ImageButton btn_mitrabar = findViewById(R.id.btn_mitra5);
        ImageButton btn_marketbar = findViewById(R.id.btn_market5);
        ImageButton btn_keranjangbar = findViewById(R.id.btn_keranjang5);
        ImageButton btn_profilebar = findViewById(R.id.btn_profil5);

        btn_homebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(chatbot.this, home.class);
                startActivity(i);
            }
        });
        btn_mitrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("isRegistered", false)) {
                    // User is already registered, open profil_mitra directly
                    Intent profileIntent = new Intent(chatbot.this, profil_mitra.class);
                    startActivity(profileIntent);
                } else {
                    // User is not registered, open admin_awal for registration
                    Intent i2 = new Intent(chatbot.this, admin_awal.class);
                    startActivity(i2);
                }
            }
        });
        btn_marketbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(chatbot.this, market.class);
                startActivity(i3);
            }
        });
        btn_keranjangbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(chatbot.this, chatbot.class);
                startActivity(i4);
            }
        });
        btn_profilebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(chatbot.this, profil_user.class);
                startActivity(i5);
            }
        });


        // Set up Spinner 1
        String[] namaBuahOptions = {"Jenis Ternak Apa yang ingin Kamu tanyakan?", "Sapi", "Kambing", "Ayam"};
        ArrayAdapter<String> namaBuahAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, namaBuahOptions);
        namaBuahSpinner.setAdapter(namaBuahAdapter);

        // Set up Spinner 2
        String[] PemilihanOptions = {"Hal Apa yang ingin kamu tanyakan?","Gizi", "Kandungan", "Sehat", "Fresh"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, PemilihanOptions);
        permasalahanBuahSpinner.setAdapter(adapter3);

        // Set up Spinner 3
        namaBuahSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTanaman = (String) parentView.getItemAtPosition(position);
                updatePermasalahanOptions(selectedTanaman);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Menambahkan listener untuk tombol diagnosis
        findViewById(R.id.btn_diagnosis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedBuah = namaBuahSpinner.getSelectedItem().toString();
                String selectedTanaman = tanamanBuahSpinner.getSelectedItem().toString();
                String selectedPermasalahan = permasalahanBuahSpinner.getSelectedItem().toString();

                // Kirim data ke API dan ambil hasilnya di AsyncTask
                try {
                    String chatbotResponse = new OpenAIChat().execute(selectedBuah, selectedTanaman, selectedPermasalahan).get();

                    // Tampilkan hasil dari chatbot ke TextView
                    hasilGptTextView.setText(chatbotResponse);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updatePermasalahanOptions(String selectedTanaman) {
        String[] BagianTanamanOptions;
        if ("Ayam".equals(selectedTanaman)) {
            BagianTanamanOptions = new String[]{"Telur", "Daging"};
        } else if ("Kambing".equals(selectedTanaman)) {
            BagianTanamanOptions = new String[]{"Susu", "Daging", "Karkas"};
        } else if ("Sapi".equals(selectedTanaman)) {
            BagianTanamanOptions = new String[]{"Susu", "Daging", "Karkas"};
        } else {
            BagianTanamanOptions = new String[]{"Hasil Ternak Apa yang Ingin ditanyakan?"};
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, BagianTanamanOptions);
        tanamanBuahSpinner.setAdapter(adapter2);
    }

    public class OpenAIChat extends AsyncTask<String, Void, String> {
        private static final String API_URL = "https://api.openai.com/v1/completions";
        private static final String MODEL_NAME = "text-davinci-003";
        private static final double TEMPERATURE = 0;
        private static final int MAX_TOKENS = 1000;
        private static final double TOP_P = 1;
        private static final double FREQUENCY_PENALTY = 0.0;
        private static final double PRESENCE_PENALTY = 0.0;
        private static final String YOUR_API_KEY = "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";

        @Override
        protected String doInBackground(String... params) {
            String selectedBuah = params[0];
            String selectedPermasalahan = params[1];
            String selectedTanaman = params[2];

            String prompt = String.format("Saya memilih jenis hewan ternak %s. Dengan hasil ternak  %s dengan mengingkan hasil %s yang baik. Berikan penjelasan untuk memilih hasil ternak tersebut dengan baik.",
                    selectedBuah, selectedPermasalahan, selectedTanaman);

            try {
                // Membuat body request
                String requestBody = String.format(
                        "{\"prompt\": \"%s\", \"model\": \"%s\", \"temperature\": %s, \"max_tokens\": %s, \"top_p\": %s, \"frequency_penalty\": %s, \"presence_penalty\": %s}",
                        prompt, MODEL_NAME, TEMPERATURE, MAX_TOKENS, TOP_P, FREQUENCY_PENALTY, PRESENCE_PENALTY
                );

                // Membuat koneksi HTTP
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Konfigurasi request
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + YOUR_API_KEY);
                connection.setDoOutput(true);

                // Mengirim data ke server
                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);
                    wr.write(postData);
                }

                // Membaca respons dari server
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    // Mengurai respons JSON
                    JSONObject jsonResponse = new JSONObject(result);

                    // Mendapatkan array choices
                    JSONArray choicesArray = jsonResponse.getJSONArray("choices");

                    if (choicesArray.length() > 0) {
                        // Mendapatkan objek pertama dari array choices
                        JSONObject firstChoice = choicesArray.getJSONObject(0);

                        // Mendapatkan nilai dari kunci "text"
                        String chatbotResponse = firstChoice.getString("text");

                        // Menghapus \n\n di awal teks
                        chatbotResponse = chatbotResponse.replaceFirst("^\\n\\n", "");

                        // Menampilkan hasilnya di TextView
                        hasilGptTextView.setText(chatbotResponse);

                        Log.d("ChatbotResponse", "Response: " + chatbotResponse);


//                        if (chatbotResponse.toLowerCase().contains("fungisida") || chatbotResponse.toLowerCase().contains("fungisida.") || chatbotResponse.toLowerCase().contains("fungisida,") || chatbotResponse.contains("Mancozeb")) {
//                            // Filter ProdukModels list based on conditions
//                            List<ProdukModels> filteredList = new ArrayList<>();
//                            for (ProdukModels produk : produkList) {
//                                if (produk.getNama_obat().equals("Mancozeb")) {
//                                    filteredList.add(produk);
//                                }
//                            }
//
//                            // Update RecyclerView with filtered data
//                            produkAdapter.setProdukList(filteredList);
//                            produkAdapter.notifyDataSetChanged();
//
//                            // Show RecyclerView
//                            tv_rekomendasi_obat.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.VISIBLE);
//                        } else if (chatbotResponse.toLowerCase().contains("herbisida") || chatbotResponse.toLowerCase().contains("herbisida.") || chatbotResponse.toLowerCase().contains("herbisida,") || chatbotResponse.contains("Gramoxone")) {
//                            // Filter ProdukModels list based on conditions
//                            List<ProdukModels> filteredList = new ArrayList<>();
//                            for (ProdukModels produk : produkList) {
//                                if (produk.getNama_obat().equals("Gramoxone")) {
//                                    filteredList.add(produk);
//                                }
//                            }
//
//                            // Update RecyclerView with filtered data
//                            produkAdapter.setProdukList(filteredList);
//                            produkAdapter.notifyDataSetChanged();
//
//                            // Show RecyclerView
//                            tv_rekomendasi_obat.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.VISIBLE);
//                        } else if (chatbotResponse.toLowerCase().contains("insektisida") || chatbotResponse.toLowerCase().contains("insektisida.") || chatbotResponse.toLowerCase().contains("insektisida,") || chatbotResponse.toLowerCase().contains("pestisida") || chatbotResponse.toLowerCase().contains("pestisida.") || chatbotResponse.toLowerCase().contains("pestisida,") || chatbotResponse.contains("Petrogenol")) {
//                            // Filter ProdukModels list based on conditions
//                            List<ProdukModels> filteredList = new ArrayList<>();
//                            for (ProdukModels produk : produkList) {
//                                if (produk.getNama_obat().equals("Petrogenol")) {
//                                    filteredList.add(produk);
//                                }
//                            }
//
//                            // Update RecyclerView with filtered data
//                            produkAdapter.setProdukList(filteredList);
//                            produkAdapter.notifyDataSetChanged();
//
//                            // Show RecyclerView
//                            tv_rekomendasi_obat.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.VISIBLE);
//                        }else if (chatbotResponse.toLowerCase().contains("bakterisida") || chatbotResponse.toLowerCase().contains("bakterisida.") || chatbotResponse.toLowerCase().contains("bakterisida,") || chatbotResponse.contains("Prima-Zeb")) {
//                            // Filter ProdukModels list based on conditions
//                            List<ProdukModels> filteredList = new ArrayList<>();
//                            for (ProdukModels produk : produkList) {
//                                if (produk.getNama_obat().equals("Prima-Zeb")) {
//                                    filteredList.add(produk);
//                                }
//                            }
//
//                            // Update RecyclerView with filtered data
//                            produkAdapter.setProdukList(filteredList);
//                            produkAdapter.notifyDataSetChanged();
//
//                            // Show RecyclerView
//                            tv_rekomendasi_obat.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.VISIBLE);
//                        } else {
//                            // Hide RecyclerView if conditions don't match
//                            tv_rekomendasi_obat.setVisibility(View.GONE);
//                            recyclerView.setVisibility(View.GONE);
//                        }
                    } else {
                        // Handle jika array choices kosong
                        hasilGptTextView.setText("Tidak ada respons dari chatbot.");
                        // Hide RecyclerView if conditions don't match
                        //recyclerView.setVisibility(View.GONE);
                        //tv_rekomendasi_obat.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hasilGptTextView.setText("Gagal mengurai respons JSON.");
                    // Hide RecyclerView if conditions don't match
                    //recyclerView.setVisibility(View.GONE);
                   // tv_rekomendasi_obat.setVisibility(View.GONE);
                }
            } else {
                // Handle jika result null
                hasilGptTextView.setText("Gagal mendapatkan respons dari chatbot.");
                // Hide RecyclerView if conditions don't match
               // recyclerView.setVisibility(View.GONE);
               // tv_rekomendasi_obat.setVisibility(View.GONE);
            }
        }
    }
}
