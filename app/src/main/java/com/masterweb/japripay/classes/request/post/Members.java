package com.masterweb.japripay.classes.request.post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.members.MitraActivity;
import com.masterweb.japripay.classes.Dialogs;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.GoogleCloud;
import com.masterweb.japripay.classes.dialog.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Members {
    Call<MembersModel> data;
    Call<AuthModel> up;
    ApiInterface apiService;
    Context context;
    Activity activity;
    MasterFunction masterFunction;
    Message dialog;
    Dialogs message;

    public Members(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        masterFunction = new MasterFunction(context,activity);
        apiService = GoogleCloud.getClient().create(ApiInterface.class);
        dialog = new Message(activity);
        message = new Dialogs(activity);
    }
    public void Home(TextView bonus, TextView name, TextView saldo){
        data = apiService.getMembers(masterFunction.get_phone());
        data.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    bonus.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getBonus())));
                    name.setText(response.body().getName());
                    saldo.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getSaldo())));
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
    public void Akun(TextView phone, TextView name, TextView saldo,TextView gift,LinearLayout view_data,LinearLayout upgrade){
        data = apiService.getMembers(masterFunction.get_phone());
        data.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    phone.setText(response.body().getPhone());
                    name.setText(response.body().getName());
                    saldo.setText("Saldo : Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getSaldo())));
                    gift.setText(masterFunction.Rupiah(Integer.parseInt(response.body().getSaham()))+" Saham pra IPO");
                    if (response.body().getType().matches("0")){
                        view_data.setBackgroundColor(context.getResources().getColor(R.color.red));
                    }else {
                        upgrade.setVisibility(View.GONE);
                        view_data.setBackgroundColor(context.getResources().getColor(R.color.green));
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
    public void UpdateProfile(EditText phone, EditText name, EditText address){
        dialog.show();
        up = apiService.updateMembers(phone.getText().toString(),name.getText().toString(),address.getText().toString());
        up.enqueue(new Callback<AuthModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        message.showSuccess(response.body().getMessage());
                    }else{
                        message.showError(response.body().getMessage());
                    }
                  dialog.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                dialog.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                dialog.dismiss();
            }
        });
    }
    public void Profile(EditText phone, EditText name, EditText address){
        data = apiService.getMembers(masterFunction.get_phone());
        data.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    phone.setText(response.body().getPhone());
                    name.setText(response.body().getName());
                    address.setText(response.body().getAddress());
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
    public void Mitra(TextView downline){
        data = apiService.getMembers(masterFunction.get_phone());
        data.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    downline.setText(response.body().getDownline()+" Orang");
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
    public void Register(EditText reff,TextView link){
        data = apiService.getMembers(masterFunction.get_phone());
        data.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    reff.setText(response.body().getReff());
                    link.setText("https://japrime.id/reff/"+response.body().getReff());
                    link.setOnClickListener(V->{
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://japrime.id/reff/"+response.body().getReff());
                        sendIntent.setType("text/plain");
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(shareIntent);
                    });
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
    public void UpdateToken(String token){
        up = apiService.insertToken(masterFunction.get_phone(),token);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("upgrade", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;

                }else{
                    Log.d("body_auth_login", ""+response.errorBody());
                }

            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("Error Token", ""+t.getMessage());

            }
        });
    }
    public void RegisterApp(EditText name, String phones, EditText reg_reff){
        dialog.show();
        up = apiService.register(name.getText().toString(), phones, "123456", reg_reff.getText().toString(),"");
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        masterFunction.successMessage("Pendaftaran Mitra berhasil");
                        Intent intent = new Intent(context, MitraActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        masterFunction.errorMessage("Pendaftaran Mitra Gagal, Silahka Coba beberapa Saat lagi");
                    }
                    dialog.dismiss();
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
    public void getKomirmasiSaldo(TextView saldo,
                                  String harga,
                                  Button topup,
                                  ImageView right,
                                  Button lanjutkan,
                                  String produk,
                                  String customers,
                                  String invoice){
        data = apiService.getMembers(masterFunction.get_phone());
        data.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    saldo.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getSaldo())));
                    if (Integer.parseInt(response.body().getSaldo()) < Integer.parseInt(harga)){
                        lanjutkan.setVisibility(View.GONE);
                        right.setVisibility(View.GONE);
                        topup.setVisibility(View.VISIBLE);
                    }else{
                        lanjutkan.setVisibility(View.VISIBLE);
                        lanjutkan.setOnClickListener(V->{
                            message.pay_prabayar(harga,produk,customers,invoice);
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
    public void getSaldo(TextView saldo){
        data = apiService.getMembers(masterFunction.get_phone());
        data.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    saldo.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getSaldo())));

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
}
