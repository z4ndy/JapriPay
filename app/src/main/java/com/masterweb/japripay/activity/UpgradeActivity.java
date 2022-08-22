package com.masterweb.japripay.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.home.AkunActivity;
import com.masterweb.japripay.activity.transaksi.TopUpActivity;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;
import com.masterweb.japripay.classes.dialog.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpgradeActivity extends AppCompatActivity {
    LinearLayout back,master,konter,kios,sewa_konter,sewa_kios;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        master = findViewById(R.id.master);
        konter = findViewById(R.id.konter);
        kios = findViewById(R.id.kios);
        sewa_konter = findViewById(R.id.sewa_konter);
        sewa_kios = findViewById(R.id.sewa_kios);
        master.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),DetailPaketActivity.class);
                intent.putExtra("id","1");
                intent.putExtra("title","Paket Master Agen");
                startActivity(intent);
            }
        });
        konter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(),DetailPaketActivity.class);
                intent.putExtra("id","2");
                intent.putExtra("title","Paket Konter");
                startActivity(intent);
            }
        });
        kios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),DetailPaketActivity.class);
                intent.putExtra("id","3");
                intent.putExtra("title","Paket Kios");
                startActivity(intent);

            }
        });
        sewa_konter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(),DetailPaketActivity.class);
                intent.putExtra("id","4");
                intent.putExtra("title","Sewa Paket Konter");
                startActivity(intent);
            }
        });
        sewa_kios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(),DetailPaketActivity.class);
                intent.putExtra("id","5");
                intent.putExtra("title","Sewa Paket Kios");
                startActivity(intent);
            }
        });
    }


}