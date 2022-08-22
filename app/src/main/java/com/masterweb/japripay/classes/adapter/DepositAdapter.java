package com.masterweb.japripay.classes.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.InvoiceActivity;
import com.masterweb.japripay.classes.DateIndo;
import com.masterweb.japripay.classes.Model.TransaksiModel;

import java.text.DecimalFormat;
import java.util.List;

public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.MyViewHolder> {
    Context context;
    List<TransaksiModel> list_today;
    Intent intent;
    DateIndo dateIndo;
    public DepositAdapter(Context context, List<TransaksiModel> listSurat) {
        this.context = context;
        this.list_today = listSurat;
        dateIndo = new DateIndo();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setHarga(List<TransaksiModel> movieList) {
        this.list_today = movieList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_transaksi,parent,false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.produk.setText(list_today.get(position).getProduk());
        holder.tanggal.setText(dateIndo.show(list_today.get(position).getCreated_at()));
        holder.jenis.setText(Rupiah(Integer.parseInt(list_today.get(position).getPrice())));
        if (list_today.get(position).getStatus().matches("1")){
            holder.produk.setTextColor(context.getResources().getColor(R.color.green));
            holder.jenis.setTextColor(context.getResources().getColor(R.color.green));
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
            holder.tanggal.setTextColor(context.getResources().getColor(R.color.green));
        }else if(list_today.get(position).getStatus().matches("2")){
            holder.produk.setTextColor(context.getResources().getColor(R.color.red));
            holder.jenis.setTextColor(context.getResources().getColor(R.color.red));
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.tanggal.setTextColor(context.getResources().getColor(R.color.red));
        }
        if (list_today.get(position).getStatus().matches("1")){
            holder.status.setText("Sukses");
        }else if (list_today.get(position).getStatus().matches("2")){
            holder.status.setText("Gagal");
        }else{
            holder.status.setText("Pending");
        }

        holder.list_report.setOnClickListener(v -> {
            intent = new Intent(context, InvoiceActivity.class);
            intent.putExtra("key","topup");
            intent.putExtra("invoice",list_today.get(position).getInvoice());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }
    public String Rupiah(int angka){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(angka);
        return yourFormattedString.replace(",", ".");
    }
    @Override
    public int getItemCount() {
        if(list_today != null){
            return list_today.size();
        }
        return 0;
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView produk,jenis,tanggal,status;
        LinearLayout list_report;

        public MyViewHolder(View itemView) {
            super(itemView);
            produk = itemView.findViewById(R.id.produk);
            jenis = itemView.findViewById(R.id.jenis);
            tanggal = itemView.findViewById(R.id.tanggal);
            status = itemView.findViewById(R.id.status);
            list_report = itemView.findViewById(R.id.list);
        }
    }
}
