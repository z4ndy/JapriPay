package com.masterweb.japripay.classes.request.get;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.masterweb.japripay.classes.Model.BeritaModel;
import com.masterweb.japripay.classes.adapter.BeritaAdapter;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Promo {
    List<BeritaModel> listPromo;
    ApiInterface apiService;
    Context context;
    BeritaAdapter promosAdapter;
    public Promo(Context context, RecyclerView list_promo){
        this.context = context;
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        listPromo = new ArrayList<>();
        LinearLayoutManager layoutManagers = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        list_promo.setLayoutManager(layoutManagers);
        promosAdapter = new BeritaAdapter(context,listPromo);
        list_promo.setAdapter(promosAdapter);
    }
    public void getPromo(ShimmerFrameLayout shimmerFrameLayout){
        Call<List<BeritaModel>> call = apiService.get_promo();
        call.enqueue(new Callback<List<BeritaModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BeritaModel>> call, @NonNull Response<List<BeritaModel>> response) {
                Log.d("list_info", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    listPromo = response.body();
                    Log.d("TAG","Response = "+listPromo);
                    promosAdapter.setHarga(listPromo);
                }else{
                    Log.d("errt", ""+response.errorBody());
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(@NonNull Call<List<BeritaModel>> call, @NonNull Throwable t) {
                Log.d("DataModel", ""+t.getMessage());
            }
        });
    }
}
