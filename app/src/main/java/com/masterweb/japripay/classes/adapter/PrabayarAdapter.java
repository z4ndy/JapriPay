package com.masterweb.japripay.classes.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.masterweb.japripay.classes.Dialogs;
import com.masterweb.japripay.classes.Model.HargaModel;

import java.text.DecimalFormat;
import java.util.List;

public class PrabayarAdapter extends RecyclerView.Adapter<PrabayarAdapter.MyViewHolder> {
    Context context;
    List<HargaModel> list_today;
    Dialogs dialogs;
    Intent intent;
    Activity mactivity;
    public PrabayarAdapter(Context context, List<HargaModel> listSurat,Activity activity) {
        this.context = context;
        this.list_today = listSurat;
        this.mactivity = activity;
        dialogs = new Dialogs(mactivity);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setHarga(List<HargaModel> movieList) {
        this.list_today = movieList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_prabayar,parent,false);
        return new MyViewHolder(itemView);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.produk.setText(list_today.get(position).getName());
        holder.tanggal.setText(list_today.get(position).getName()+"\n"+list_today.get(position).getDesc());
        holder.jenis.setText(Rupiah(Integer.parseInt(list_today.get(position).getSale())));
        if (list_today.get(position).getStatus().matches("0")){
            holder.list_report.setBackground(context.getResources().getDrawable(R.drawable.bg_red));
            holder.garis.setBackgroundColor(context.getResources().getColor(R.color.red));
        }
        holder.list_report.setOnClickListener(v -> {
            holder.list_report.setBackground(context.getResources().getDrawable(R.drawable.bg_selected));
            dialogs.kofirmasi(list_today.get(position).getCode(),
                    list_today.get(position).getPelanggan(),
                    list_today.get(position).getName(),
                    list_today.get(position).getSale(),
                    list_today.get(position).getDesc(), holder.list_report);

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
        TextView produk,jenis,tanggal,code;
        LinearLayout list_report;
        View garis;
        public MyViewHolder(View itemView) {
            super(itemView);
            produk = itemView.findViewById(R.id.produk);
            code = itemView.findViewById(R.id.code);
            jenis = itemView.findViewById(R.id.jenis);
            garis = itemView.findViewById(R.id.view);
            tanggal = itemView.findViewById(R.id.tanggal);
            list_report = itemView.findViewById(R.id.list);
        }
    }
}
