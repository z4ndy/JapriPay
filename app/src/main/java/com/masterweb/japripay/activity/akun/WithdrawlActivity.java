package com.masterweb.japripay.activity.akun;

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
import com.masterweb.japripay.activity.home.HomeActivity;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.Model.ResultModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.GoogleCloud;
import com.masterweb.japripay.classes.dialog.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawlActivity extends AppCompatActivity {
    ImageView back;
    MasterFunction masterFunction;
    Call<MembersModel> data;
    Call<ResultModel> datas;
    ApiInterface apiService;
    LinearLayout upgrade,tarik;
    Button aksi;
    TextView saldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawl);
        apiService = GoogleCloud.getClient().create(ApiInterface.class);
        masterFunction = new MasterFunction(getApplicationContext(), WithdrawlActivity.this);
        back = findViewById(R.id.back);
        upgrade = findViewById(R.id.upgrade);
        tarik = findViewById(R.id.tarik);
        aksi = findViewById(R.id.aksi);
        saldo = findViewById(R.id.saldo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Widrawal();
    }
    public void Widrawal(){
        data = apiService.getMembers(masterFunction.get_phone());
        data.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getType().matches("2")){
                        upgrade.setVisibility(View.VISIBLE);
                    }else{
                        upgrade.setVisibility(View.GONE);
                        tarik.setVisibility(View.VISIBLE);
                        saldo.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getSaldo())));
                        aksi.setOnClickListener(V->{
                            penarikan(response.body().getSaldo());
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
    public void penarikan(String amount){
        Message message = new Message(WithdrawlActivity.this);
        message.show();
        datas = apiService.TarikSaldo(masterFunction.get_phone(),amount);
        datas.enqueue(new Callback<ResultModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResultModel> call, @NonNull Response<ResultModel> response) {
                Log.d("penarikan", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getType().matches("1")){
                        message.dismiss();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<ResultModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());

            }
        });
    }
}