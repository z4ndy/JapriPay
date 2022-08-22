package com.masterweb.japripay.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.home.AkunActivity;
import com.masterweb.japripay.activity.transaksi.TopUpActivity;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.Model.UpgradeModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;
import com.masterweb.japripay.classes.dialog.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPaketActivity extends AppCompatActivity {
    Toolbar toolbar;
    Call<UpgradeModel> data;
    Call<MembersModel> datas;
    Call<AuthModel> sale;
    ApiInterface apiInterface;
    TextView desc,amount,bonus,saldo;
    MasterFunction masterFunction;
    Button beli,topup;
    ImageView right;
    Message message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_paket);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        masterFunction = new MasterFunction(getApplicationContext(),DetailPaketActivity.this);
        message = new Message(DetailPaketActivity.this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("title"));
        toolbar.setNavigationIcon(R.drawable.left_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        desc = findViewById(R.id.desc);
        amount = findViewById(R.id.amount);
        bonus = findViewById(R.id.bonus);
        beli = findViewById(R.id.beli);
        saldo = findViewById(R.id.saldo);
        topup = findViewById(R.id.topup);
        right = findViewById(R.id.right);
        getDetail();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void getDetail(){
        message.show();
        data = apiInterface.DetailPaket(getIntent().getExtras().getString("id"));
        data.enqueue(new Callback<UpgradeModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<UpgradeModel> call, @NonNull Response<UpgradeModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    amount.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getHarga())));
                    bonus.setText(response.body().getBonus());
                    desc.setText(response.body().getKeterangan());
                    getMembers(response.body().getHarga());
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<UpgradeModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                message.dismiss();
            }
        });
    }
    private void getMembers(String harga){
        datas = apiInterface.getMembers(masterFunction.get_phone());
        datas.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    saldo.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getSaldo())));
                    if (Integer.parseInt(response.body().getSaldo()) < Integer.parseInt(harga)){
                        beli.setVisibility(View.GONE);
                        topup.setVisibility(View.VISIBLE);
                        right.setVisibility(View.GONE);
                        topup.setOnClickListener(V->{
                            Intent intent = new Intent(getApplicationContext(), TopUpActivity.class);
                            startActivity(intent);
                        });
                    }else{
                        beli.setVisibility(View.VISIBLE);
                        beli.setOnClickListener(V->{
                            beliPaket(getIntent().getExtras().getString("id"));
                        });
                    }
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                //message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<MembersModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());

            }
        });
    }
    private void beliPaket(String paket){
        sale = apiInterface.beliPaket(paket,masterFunction.get_phone());
        sale.enqueue(new Callback<AuthModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        masterFunction.successMessage(response.body().getMessage());
                        Intent intent = new Intent(getApplicationContext(), AkunActivity.class);
                        startActivity(intent);
                    }else{
                        masterFunction.successMessage(response.body().getMessage());
                    }
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                //message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());

            }
        });
    }
}