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

import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.MyViewHolder> {
    Context context;
    List<TransaksiModel> list_today;
    Intent intent;
    DateIndo dateIndo;
    public TransaksiAdapter(Context context, List<TransaksiModel> listSurat) {
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
        switch (list_today.get(position).getType()){
            case "1":
                holder.jenis.setText("Prabayar");
                break;
            case "2":
                holder.jenis.setText("Pascabayar");
                break;
            case "3":
                holder.jenis.setText("Transfer Bank");
                break;
            case "4":
                holder.jenis.setText("Upgrade");
                break;
            case "5":
                holder.jenis.setText("Claim Bonus");
                break;
            case "6":
                holder.jenis.setText("Bonus Transaksi");
                break;
            case "7":
                holder.jenis.setText("TopUp Saldo");
                break;
            case "8":
                holder.jenis.setText("Transfer Saldo");
                break;
        }
        if (list_today.get(position).getStatus().matches("1") && Integer.parseInt(list_today.get(position).getType()) == 8){
            holder.produk.setTextColor(context.getResources().getColor(R.color.green));
            holder.jenis.setTextColor(context.getResources().getColor(R.color.green));
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
            holder.tanggal.setTextColor(context.getResources().getColor(R.color.green));

        }else if (list_today.get(position).getStatus().matches("1") && Integer.parseInt(list_today.get(position).getType()) < 6){
            holder.produk.setTextColor(context.getResources().getColor(R.color.green));
            holder.jenis.setTextColor(context.getResources().getColor(R.color.green));
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
            holder.tanggal.setTextColor(context.getResources().getColor(R.color.green));

        }else if (list_today.get(position).getStatus().matches("1") && Integer.parseInt(list_today.get(position).getType()) > 5){
            holder.produk.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.jenis.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tanggal.setTextColor(context.getResources().getColor(R.color.colorPrimary));

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
            switch (list_today.get(position).getType()){
                case "1":
                    intent.putExtra("key","prabayar");
                    break;
                case "2":
                    intent.putExtra("key","pasca");
                    break;
                case "3":
                    intent.putExtra("key","transfer");
                    break;
                case "4":
                    intent.putExtra("key","upgrade");
                    break;
                case "5":
                    intent.putExtra("key","claim");
                    break;
                case "6":
                    intent.putExtra("key","bonus");
                    break;
                case "7":
                    intent.putExtra("key","topup");
                    break;
                case "8":
                    intent.putExtra("key","kirim_saldo");
                    break;
            }
            intent.putExtra("invoice",list_today.get(position).getInvoice());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
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
