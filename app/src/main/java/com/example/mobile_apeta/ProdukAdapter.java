package com.example.mobile_apeta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> {

    private List<ProdukModels> produkList;
    private Context context;

    public ProdukAdapter(List<ProdukModels> produkList, Context context) {
        this.produkList = produkList;
        this.context = context;
    }
    public void setProdukList(List<ProdukModels> produkList) {
        this.produkList = produkList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)   {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_market, parent, false);
        return new ViewHolder(view);
    }

    // Update the onBindViewHolder method
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProdukModels produk = produkList.get(position);

        holder.productName.setText(produk.getNama_produk());
        holder.productPrice.setText("Rp" + produk.getHarga_produk());
        holder.storeName.setText(produk.getNama_toko());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click here, if necessary
                ProdukModels clickedProduk = produkList.get(holder.getAdapterPosition());
                showProductDetails(clickedProduk);
            }
        });
    }


    private void showProductDetails(ProdukModels clickedProduk) {
        Intent intent = new Intent(context, produk_details.class);
        intent.putExtra("productId", clickedProduk.get_id());
        intent.putExtra("productName", clickedProduk.getNama_produk());
        intent.putExtra("productPrice", clickedProduk.getHarga_produk());
        intent.putExtra("tokoName", clickedProduk.getNama_toko());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView storeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            storeName = itemView.findViewById(R.id.storeName);
        }
    }
    public void filterList(List<ProdukModels> filteredList) {
        produkList = filteredList;
        notifyDataSetChanged();
    }
}

