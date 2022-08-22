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
import com.masterweb.japripay.classes.DateIndo;
import com.masterweb.japripay.classes.Model.TransaksiModel;

import java.text.DecimalFormat;
import java.util.List;

public class MutasiAdapter extends RecyclerView.Adapter<MutasiAdapter.MyViewHolder> {
    Context context;
    List<TransaksiModel> list_today;
    Intent intent;
    DateIndo dateIndo;
    public MutasiAdapter(Context context, List<TransaksiModel> listSurat) {
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
        View itemView = inflater.inflate(R.layout.list_mutasi,parent,false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.desc.setText(list_today.get(position).getDesc());
        holder.date.setText(dateIndo.show(list_today.get(position).getDate()));
        holder.masuk.setText(Rupiah(Integer.parseInt(list_today.get(position).getMasuk())));
        holder.keluar.setText(Rupiah(Integer.parseInt(list_today.get(position).getKeluar())));
        holder.saldo.setText(Rupiah(Integer.parseInt(list_today.get(position).getSaldo())));
        holder.list_report.setOnClickListener(v -> {

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
        TextView date,desc,masuk,keluar,saldo;
        LinearLayout list_report;

        public MyViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            desc = itemView.findViewById(R.id.desc);
            masuk = itemView.findViewById(R.id.masuk);
            keluar = itemView.findViewById(R.id.keluar);
            saldo = itemView.findViewById(R.id.saldo);
            list_report = itemView.findViewById(R.id.list);
        }
    }
}
