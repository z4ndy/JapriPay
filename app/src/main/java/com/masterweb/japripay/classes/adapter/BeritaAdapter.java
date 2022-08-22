package com.masterweb.japripay.classes.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.masterweb.japripay.classes.Model.BeritaModel;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.news.BeritaActivity;

import java.util.List;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.MyViewHolder> {
    Context context;
    List<BeritaModel> list_today;

    public BeritaAdapter(Context context, List<BeritaModel> listSurat) {
        this.context = context;
        this.list_today = listSurat;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setHarga(List<BeritaModel> movieList) {
        this.list_today = movieList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_berita,parent,false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.judul.setText(list_today.get(position).getJudul());
        holder.subjudul.setText(list_today.get(position).getSubjudul());
        holder.tanggal.setText(list_today.get(position).getTanggal());
        holder.jam.setText(list_today.get(position).getJam());
        Glide.with(context).load(list_today.get(position).getImages()).into(holder.images);
        holder.list_report.setOnLongClickListener(v -> {
            Toast.makeText(context, list_today.get(position).getJudul(), Toast.LENGTH_SHORT).show();
            return false;
        });
        holder.list_report.setOnClickListener(v -> {
            Intent intent = new Intent(context, BeritaActivity.class);
            if (list_today.get(position).getType().matches("1")){
                intent.putExtra("title","Info japripay");
            }else{
                intent.putExtra("title","Promo Members");
            }
            intent.putExtra("judul",list_today.get(position).getJudul());
            intent.putExtra("subjudul",list_today.get(position).getSubjudul());
            intent.putExtra("images",list_today.get(position).getImages());
            intent.putExtra("tanggal",list_today.get(position).getTanggal());
            intent.putExtra("jam",list_today.get(position).getJam());
            intent.putExtra("konten",list_today.get(position).getKonten());
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
        TextView judul,subjudul,tanggal,jam;
        LinearLayout list_report;
        ImageView images;
        public MyViewHolder(View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.images);
            judul = itemView.findViewById(R.id.judul);
            subjudul = itemView.findViewById(R.id.subjudul);
            tanggal = itemView.findViewById(R.id.tanggal);
            jam = itemView.findViewById(R.id.jam);
            list_report = itemView.findViewById(R.id.list);
        }
    }
}
