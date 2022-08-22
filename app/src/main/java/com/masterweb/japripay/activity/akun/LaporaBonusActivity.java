package com.masterweb.japripay.activity.akun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.TransaksiModel;
import com.masterweb.japripay.classes.adapter.DepositAdapter;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporaBonusActivity extends AppCompatActivity {
    List<TransaksiModel> listDaily;
    ApiInterface apiService;
    RecyclerView list_transaksi;
    DepositAdapter hargaAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    MasterFunction masterFunction;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapora_bonus);
        masterFunction = new MasterFunction(getApplicationContext(),LaporaBonusActivity.this);
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        list_transaksi = findViewById(R.id.list_transaksi);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        listDaily = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        list_transaksi.setLayoutManager(layoutManager);
        hargaAdapter = new DepositAdapter(getApplicationContext(),listDaily);
        list_transaksi.setAdapter(hargaAdapter);
        getData();
        shimmerFrameLayout.startShimmer();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
    }
    private void getData(){
        Call<List<TransaksiModel>> call = apiService.get_bonus(masterFunction.get_phone());
        call.enqueue(new Callback<List<TransaksiModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<TransaksiModel>> call, @NonNull Response<List<TransaksiModel>> response) {
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
            public void onFailure(@NonNull Call<List<TransaksiModel>> call, @NonNull Throwable t) {
                Log.d("DataModel", ""+t.getMessage());
            }
        });
    }
}