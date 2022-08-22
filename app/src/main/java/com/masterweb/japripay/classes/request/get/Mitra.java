package com.masterweb.japripay.classes.request.get;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.adapter.MitraAdapter;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mitra {
    List<MembersModel> listDaily;
    ApiInterface apiService;
    MitraAdapter hargaAdapter;
    MasterFunction masterFunction;
    Context mcontext;
    Activity mactivity;
    public Mitra(Context context,Activity activity,RecyclerView list_transaksi){
        this.mcontext = context;
        this.mactivity = activity;
        masterFunction = new MasterFunction(mcontext,mactivity);
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        listDaily = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);
        list_transaksi.setLayoutManager(layoutManager);
        hargaAdapter = new MitraAdapter(mcontext,listDaily);
        list_transaksi.setAdapter(hargaAdapter);
    }
    public void getData(ShimmerFrameLayout shimmerFrameLayout){
        Call<List<MembersModel>> call = apiService.getLevel(masterFunction.get_phone());
        call.enqueue(new Callback<List<MembersModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<MembersModel>> call, @NonNull Response<List<MembersModel>> response) {
                Log.d("list_transaksi", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    listDaily = response.body();
                    Log.d("TAG","Response = "+listDaily);
                    hargaAdapter.setHarga(listDaily);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }else{
                    Log.d("errt", ""+response.errorBody());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<MembersModel>> call, @NonNull Throwable t) {
                Log.d("DataModel", ""+t.getMessage());
            }
        });
    }

}
