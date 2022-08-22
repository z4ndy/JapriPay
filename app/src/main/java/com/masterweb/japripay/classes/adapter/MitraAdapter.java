package com.masterweb.japripay.classes.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.Model.MembersModel;

import java.text.DecimalFormat;
import java.util.List;

public class MitraAdapter extends RecyclerView.Adapter<MitraAdapter.MyViewHolder> {
    Context context;
    List<MembersModel> list_today;
    Intent intent;
    public MitraAdapter(Context context, List<MembersModel> listSurat) {
        this.context = context;
        this.list_today = listSurat;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setHarga(List<MembersModel> movieList) {
        this.list_today = movieList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_mitra,parent,false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.name.setText(list_today.get(position).getName());
        holder.phone.setText(list_today.get(position).getPhone());
        holder.saldo.setText(list_today.get(position).getSaldo());
        holder.list_report.setOnClickListener(v -> {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String pesan = "Hallo, kak "+list_today.get(position).getName()+", bagaimana transaksi *Japri Pay* ya";
            String url = "https://api.whatsapp.com/send?phone=" + list_today.get(position).getPhone() + "&text=" + pesan;
            sendIntent.setData(Uri.parse(url));
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(sendIntent.resolveActivity(context.getPackageManager()) != null){
                context.startActivity(sendIntent);
            }else{
                Toast.makeText(context, "Pastikan anda memiliki Aplikasi WhatsApp", Toast.LENGTH_SHORT).show();
            }
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
        TextView name,phone,saldo;
        LinearLayout list_report;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            saldo = itemView.findViewById(R.id.saldo);
            list_report = itemView.findViewById(R.id.list);
        }
    }
}
