package com.masterweb.japripay.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.home.HomeActivity;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.DateIndo;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.Model.TransaksiModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceActivity extends AppCompatActivity {
    Call<TransaksiModel> data;
    Call<AuthModel> datas;
    ApiInterface apiService;
    MasterFunction masterFunction;
    TextView code,status,tanggal,nominal,desc,produk,customers,admin,total,sn;
    TimerTask timerTask;
    LinearLayout pending;
    Timer timer;
    DateIndo dateIndo;
    Toolbar toolbar;
    ClipboardManager myClipboard;
    ClipData myClip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        masterFunction = new MasterFunction(getApplicationContext(),InvoiceActivity.this);
        dateIndo = new DateIndo();
        produk = findViewById(R.id.produk);
        code = findViewById(R.id.code);
        status = findViewById(R.id.status);
        tanggal = findViewById(R.id.tanggal);
        nominal = findViewById(R.id.nominal);
        customers = findViewById(R.id.customers);
        pending = findViewById(R.id.pending);
        desc = findViewById(R.id.desc);
        toolbar = findViewById(R.id.toolbar);
        admin = findViewById(R.id.admin);
        total = findViewById(R.id.total);
        sn = findViewById(R.id.sn);
        String keys = getIntent().getExtras().getString("key");
        String inv = getIntent().getExtras().getString("invoice");
        if (keys.matches("prabayar")){
            getTransaksi(inv);
            UpdatePrabayar(inv);
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    getTransaksi(inv);
                    UpdatePrabayar(inv);
                }
            };
            timer.schedule(timerTask, 2000, 2000);
        }else  if(keys.matches("topup")){
            getTransaksi(inv);
            UpdateDeposit();
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    getTransaksi(inv);
                }
            };
            timer.schedule(timerTask, 2000, 2000);
        }else{
            getTransaksi(inv);
        }
        toolbar.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
    }
    @Override
    protected void onResume(){
        super.onResume();
        String inv = getIntent().getExtras().getString("invoice");
        String keys = getIntent().getExtras().getString("key");
        if (keys.matches("prabayar")){
            getTransaksi(inv);
            UpdatePrabayar(inv);

        }else  if(keys.matches("topup")){
            getTransaksi(inv);
            UpdateDeposit();

        }else{
            getTransaksi(inv);
        }
    }
    void UpdateDeposit(){
        datas = apiService.UpdateDeposit(masterFunction.get_phone());
        datas.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("0")){
                        status.setText("Pending");
                        status.setTextColor(getResources().getColor(R.color.red));
                    }else{
                        pending.setVisibility(View.GONE);
                        status.setText("Sukses");
                        status.setTextColor(getResources().getColor(R.color.green));
                    }
                }else{
                    Log.d("body_auth", ""+response.errorBody());

                }

            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());


            }
        });
    }
    void UpdatePrabayar(String inv){
        data = apiService.cekPulsa(masterFunction.get_phone(),inv);
        data.enqueue(new Callback<TransaksiModel>() {
            @Override
            public void onResponse(@NonNull Call<TransaksiModel> call, @NonNull Response<TransaksiModel> response) {
                Log.d("get_depo", inv+" : "+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getStatus().matches("0")){
                        status.setText("Pending");
                        status.setTextColor(getResources().getColor(R.color.red));
                    }else if(response.body().getStatus().matches("2")){
                        pending.setVisibility(View.GONE);
                        status.setText("Gagal");
                        status.setTextColor(getResources().getColor(R.color.red));
                    }else{
                        pending.setVisibility(View.GONE);
                        status.setText("Sukses");
                        status.setTextColor(getResources().getColor(R.color.green));
                    }

                }else{
                    Log.d("body_auth", ""+response.errorBody());

                }

            }
            @Override
            public void onFailure(@NonNull Call<TransaksiModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());


            }
        });
    }
    void getTransaksi(String inv){
        data = apiService.trx(masterFunction.get_phone(),inv);
        data.enqueue(new Callback<TransaksiModel>() {
            @Override
            public void onResponse(@NonNull Call<TransaksiModel> call, @NonNull Response<TransaksiModel> response) {
                Log.d("get_depo", inv+" : "+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    produk.setText(response.body().getProduk());
                    code.setText(response.body().getInvoice());
                    desc.setText(response.body().getDesc());
                    tanggal.setText(dateIndo.show(response.body().getCreated_at()));
                    customers.setText(response.body().getCustomers());
                    nominal.setText("Rp. "+masterFunction.Rupiah(Integer.valueOf(response.body().getSale())));
                    admin.setText("Rp. "+masterFunction.Rupiah(Integer.valueOf(response.body().getAdmin())));
                    total.setText("Rp. "+masterFunction.Rupiah(Integer.valueOf(response.body().getPrice())));
                    desc.setText(response.body().getDesc());
                    sn.setText(response.body().getSn());
                    sn.setOnClickListener(V->{
                        myClip = ClipData.newPlainText("text", response.body().getSn());
                        myClipboard.setPrimaryClip(myClip);
                        Toast.makeText(getApplicationContext(), "Serial Number berhasil disalin", Toast.LENGTH_SHORT).show();
                    });
                    if (response.body().getStatus().matches("0")){
                        status.setText("Pending");
                        status.setTextColor(getResources().getColor(R.color.red));
                    }else if(response.body().getStatus().matches("2")){
                        status.setText("Gagal");
                        pending.setVisibility(View.GONE);
                        status.setTextColor(getResources().getColor(R.color.red));
                    }else{
                        status.setText("Sukses");
                        pending.setVisibility(View.GONE);
                        status.setTextColor(getResources().getColor(R.color.green));
                    }
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }

            }
            @Override
            public void onFailure(@NonNull Call<TransaksiModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());

            }
        });
    }
}