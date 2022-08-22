package com.masterweb.japripay.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.Session;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.GoogleCloud;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CronJob {
    Call<AuthModel> up;
    ApiInterface apiService = GoogleCloud.getClient().create(ApiInterface.class);
    Context mContext;
    Handler mainThreadHandler;
    Toast toast;

    public CronJob(Context context){
        this.mContext = context;

    }
    public void running(){
        if (checkInternet()){
            cek_saldo();
            cek_deposit();
        } else{
            showToast("Mohon Periksa Koneksi Iternet anda,agar aplikasi JapriPay Berjalan Normal",Toast.LENGTH_LONG);
        }
    }

    public Handler getMainThreadHandler() {
        if (mainThreadHandler == null) {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }
        return mainThreadHandler;
    }
    public void showToast(final String message, final int duration) {
        getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(message)) {
                    if (toast != null) {
                        toast.cancel(); //dismiss current toast if visible
                        toast.setText(message);
                    } else {
                        toast = Toast.makeText(mContext, message, duration);
                    }
                    toast.show();
                }
            }
        });
    }
    public boolean checkInternet(){
        boolean connectStatus = true;
        ConnectivityManager ConnectionManager=(ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        connectStatus = networkInfo != null && networkInfo.isConnected();
        return connectStatus;
    }
    public  String get_phone(){
        Session session = new Session(mContext);
        HashMap<String, String> user = session.getUserDetails();
        return user.get(Session.kunci_email);
    }

    public void cek_saldo(){
        up = apiService.CekPulsa(get_phone());
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("upgrade", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    Log.d("cek_saldo", "onResponse: "+new Gson().toJson(response.body()));
                }else{
                    Log.d("cek_saldo", ""+response.errorBody());
                }

            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("Error TRansaksi", ""+t.getMessage());

            }
        });
    }
    public void cek_deposit(){
        up = apiService.UpdateDepositService(get_phone());
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("upgrade", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    Log.d("cek_deposit", "onResponse: "+new Gson().toJson(response.body()));
                }else{
                    Log.d("cek_deposit", ""+response.errorBody());
                }

            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("Error Deposit", ""+t.getMessage());

            }
        });
    }
}
