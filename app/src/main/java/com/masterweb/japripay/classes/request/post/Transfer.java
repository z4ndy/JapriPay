package com.masterweb.japripay.classes.request.post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.masterweb.japripay.activity.home.HomeActivity;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;
import com.masterweb.japripay.classes.dialog.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transfer {
    Call<MembersModel> members;
    Call<AuthModel> kirim;
    ApiInterface apiService;
    MasterFunction masterFunction;
    Context mContext;
    Activity mActivity;
    Message dialog;
    public Transfer(Context context,Activity activity){
        this.mContext = context;
        this.mActivity = activity;
        masterFunction = new MasterFunction(mContext,mActivity);
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        dialog = new Message(mActivity);
    }
    public void kirimSaldo(String phone,String amount){
        dialog.show();
        members = apiService.getMembers(masterFunction.get_phone());
        members.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (Integer.parseInt(amount) > Integer.parseInt(response.body().getSaldo())){
                        masterFunction.errorMessage("Maaf saldo anda tidak mencukupi");
                    }else{
                        send_balance(phone,amount);
                    }
                    dialog.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                dialog.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<MembersModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());

            }
        });
    }
    public void kirimQRcode(String phone,String amount){
        dialog.show();
        members = apiService.getMembers(masterFunction.get_phone());
        members.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (Integer.parseInt(amount) > Integer.parseInt(response.body().getSaldo())){
                        masterFunction.errorMessage("Maaf saldo anda tidak mencukupi");
                    }else{
                        proses(phone,amount);
                    }
                    dialog.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                    dialog.dismiss();
                }
                //message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<MembersModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                dialog.dismiss();
            }
        });
    }
    private void proses(String phone,String amount){
        kirim = apiService.transferQRMembers(masterFunction.get_phone(),phone,amount);
        kirim.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@androidx.annotation.NonNull Call<AuthModel> call, @androidx.annotation.NonNull Response<AuthModel> response) {
                Log.d("kirim_saldo", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        masterFunction.successMessage("Pembayaran berhasil dilakukan");
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }else{
                        masterFunction.errorMessage("Pembayaran gagal dilakukan, silahkan ulangi beberapa saat lagi");
                    }
                }else{
                    Log.d("body_auth", ""+response.errorBody());

                }
            }
            @Override
            public void onFailure(@androidx.annotation.NonNull Call<AuthModel> call, @androidx.annotation.NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
            }
        });
    }
    public void send_balance(String phone,String amount){
        dialog.show();
        kirim = apiService.transferSaldoMembers(masterFunction.get_phone(),phone,amount);
        kirim.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("kirim_saldo", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        masterFunction.successMessage("Pengiriman saldo berhasil diproses");
                    }else{
                        masterFunction.errorMessage("Pengiriman saldo gagal diproses");
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
    public void cekMembers(String phones, LinearLayout input){
        dialog.show();
        if (phones.matches(masterFunction.get_phone())){
            masterFunction.errorMessage("Tidak bisa mengirim ke nomor sendiri");
        }else{
            members = apiService.cekMembers(phones);
            members.enqueue(new Callback<MembersModel>() {
                @Override
                public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                    Log.d("kirim_saldo", ""+new Gson().toJson(response.body()));
                    if (response.isSuccessful()){
                        assert response.body() != null;
                        if (response.body().getPhone().matches("0")){
                            masterFunction.errorMessage("No. Handphone tidak terdaftar");
                            input.setVisibility(View.GONE);
                        }else{
                            masterFunction.successMessage("Yakin, kamu aka transfer saldo kepada : "+response.body().getName());
                            input.setVisibility(View.VISIBLE);
                        }
                        dialog.dismiss();
                    }else{
                        Log.d("body_auth", ""+response.errorBody());
                        dialog.dismiss();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<MembersModel> call, @NonNull Throwable t) {
                    Log.d("DataModel_auth", ""+t.getMessage());
                    dialog.dismiss();
                }
            });
        }

    }
}
