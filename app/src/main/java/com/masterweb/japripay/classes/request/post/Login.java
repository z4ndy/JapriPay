package com.masterweb.japripay.classes.request.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.masterweb.japripay.activity.home.HomeActivity;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;
import com.masterweb.japripay.classes.dialog.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login {
    Call<AuthModel> data;
    ApiInterface apiService;
    MasterFunction master;
    Activity activity;
    Context context;
    Message dialog;
    public Login(Context context,Activity activity){
        this.activity = activity;
        this.context = context;
        master = new MasterFunction(context,activity);
        dialog = new Message(activity);
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
    }
    public void masuk(String phone, String pin){
        dialog.show();
        data = apiService.auth(phone,pin);
        data.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("auth_login", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        master.createSession(phone);
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else {
                        master.errorMessage(response.body().getMessage());
                    }
                    dialog.dismiss();
                }else{
                    Log.d("body_auth_login", ""+response.errorBody());
                    dialog.dismiss();
                }
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth_login", ""+t.getMessage());
                dialog.dismiss();
            }
        });
    }
    public void sendOtp(String phone){
        dialog.show();
        data = apiService.sendOtp(phone,MasterFunction.getOtp());
        data.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("sen_otp", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    dialog.dismiss();
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        master.successMessage(response.body().getMessage());
                    }
                }else{
                    Log.d("body", ""+response.errorBody());
                }
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("error_otp", ""+t.getMessage());

            }
        });
    }
    public void validasiOtp(String phones, String otp){
        dialog.show();
        data = apiService.validasiOtp(phones,otp);
        data.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("retroffit_auth", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        master.createSession(phones);
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        master.errorMessage(response.body().getMessage());
                    }
                    dialog.dismiss();
                }else{
                    Log.d("body", ""+response.errorBody());
                }

            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel", ""+t.getMessage());

            }
        });
    }
}
