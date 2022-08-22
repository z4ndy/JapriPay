package com.masterweb.japripay.classes.request.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.masterweb.japripay.activity.InvoiceActivity;
import com.masterweb.japripay.activity.home.HomeActivity;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;
import com.masterweb.japripay.classes.dialog.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Prabayar {
    Context mcontext;
    Activity mactivity;
    Call<AuthModel> up;
    ApiInterface apiService;
    MasterFunction masterFunction;
    Message message;
    public Prabayar(Context context,Activity activity){
        this.mcontext = context;
        this.mactivity = activity;
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        masterFunction = new MasterFunction(mcontext,mactivity);
        message = new Message(mactivity);
    }
    public void prosesTransaksi(String price, String pin, String produk, String customers, String invoice){
        message.show();
        up = apiService.getPinTrx(masterFunction.get_phone(),pin);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("transaksi", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        beliPulsa(produk,customers,invoice,price);
                    }else{
                        masterFunction.errorMessage("PIN yang anda masukan tidak sesuai");
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(mcontext, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mcontext.startActivity(intent);
                        }, 3000);
                    }
                    message.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                message.dismiss();
            }
        });
    }
    public void beliPulsa(String produk,String customers,String invoice,String jual){
        message.show();
        up = apiService.beliPulsa(masterFunction.get_phone(),produk,customers,invoice,jual);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("beli_pulsa", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        masterFunction.successMessage("Transaksi berhasil masuk dalam antrian");
                        Intent intent = new Intent(mcontext, InvoiceActivity.class);
                        intent.putExtra("key","prabayar");
                        intent.putExtra("invoice",invoice);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mcontext.startActivity(intent);
                    }else{
                        masterFunction.errorMessage("Transaksi gagal masuk dalam antrian");
                    }
                    message.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                message.dismiss();
            }
        });
    }
}
