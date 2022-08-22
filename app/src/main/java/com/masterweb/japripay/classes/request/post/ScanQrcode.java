package com.masterweb.japripay.classes.request.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.masterweb.japripay.activity.home.BayarActivity;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;
import com.masterweb.japripay.classes.dialog.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQrcode {
    Context context;
    Activity activity;
    MasterFunction masterFunction;
    Message dialog;
    ApiInterface apiService;
    Call<MembersModel> data;
    public ScanQrcode(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
        masterFunction = new MasterFunction(context,activity);
        dialog = new Message(activity);
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
    }
    public void getData(String result){
        String[] row = result.split("#");
        if (row[0].matches(masterFunction.get_phone())){
            masterFunction.errorMessage("Tidak bisa melakukan pembayaran ke akun sendiri");
        }else{
            dialog.show();
            data = apiService.cekMembers(row[0]);
            data.enqueue(new Callback<MembersModel>() {
                @Override
                public void onResponse(@androidx.annotation.NonNull Call<MembersModel> call, @androidx.annotation.NonNull Response<MembersModel> response) {
                    Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                    if (response.isSuccessful()){
                        assert response.body() != null;
                        if (response.body().getPhone().matches("0")){
                            masterFunction.errorMessage("QRCode tidak terdaftar disistem");
                        }else{
                            Intent intent = new Intent(context, BayarActivity.class);
                            intent.putExtra("name",response.body().getName());
                            intent.putExtra("phone",response.body().getPhone());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }

                    }else{
                        Log.d("body_auth", ""+response.errorBody());
                    }
                    dialog.dismiss();
                }
                @Override
                public void onFailure(@androidx.annotation.NonNull Call<MembersModel> call, @androidx.annotation.NonNull Throwable t) {
                    Log.d("DataModel_auth", ""+t.getMessage());

                }
            });
        }
    }
}
