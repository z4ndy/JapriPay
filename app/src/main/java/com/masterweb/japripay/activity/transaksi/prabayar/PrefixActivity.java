package com.masterweb.japripay.activity.transaksi.prabayar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.HargaModel;
import com.masterweb.japripay.classes.adapter.PrabayarAdapter;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrefixActivity extends AppCompatActivity {
    ImageView back;
    TextView title;
    EditText phone;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView list_prabayar;
    List<HargaModel> listDaily;
    ApiInterface apiService;
    PrabayarAdapter hargaAdapter;
    MasterFunction masterFunction;
    LinearLayout contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefix);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        masterFunction = new MasterFunction(getApplicationContext(),PrefixActivity.this);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        phone = findViewById(R.id.phone);
        String keys = getIntent().getExtras().getString("key");
        title.setText(keys);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 8){
                    getData(phone.getText().toString().substring(0,4),keys);
                }
                if (start == 0){
                    shimmerFrameLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        list_prabayar = findViewById(R.id.list_pra);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        listDaily = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        list_prabayar.setLayoutManager(layoutManager);
        hargaAdapter = new PrabayarAdapter(getApplicationContext(),listDaily,PrefixActivity.this);
        list_prabayar.setAdapter(hargaAdapter);
        shimmerFrameLayout.setVisibility(View.GONE);
        contact = findViewById(R.id.contact);
        contact.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, 1);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Cursor cursor = null;
            try {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    String phones = cursor.getString(0);
                    String phone_member = phones.replaceAll("[^\\d.]", "");
                    String no = phone_member.substring(0, 2);
                    if (no.matches("62")) {
                        String no_hp = "0"+phone_member.substring(2);
                        phone.setText(no_hp);
                        getData(no_hp.substring(0,4),title.getText().toString());
                    } else {
                        phone.setText(phone_member);
                        getData(phone_member.substring(0,4),title.getText().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void getData(String prefix,String kategori){
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        Call<List<HargaModel>> call = apiService.getPrabayar(masterFunction.get_phone(),prefix,kategori,phone.getText().toString());
        call.enqueue(new Callback<List<HargaModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<HargaModel>> call, @NonNull Response<List<HargaModel>> response) {
                Log.d("list_harga", ""+new Gson().toJson(response.body()));
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
            public void onFailure(@NonNull Call<List<HargaModel>> call, @NonNull Throwable t) {
                Log.d("DataModel", ""+t.getMessage());
            }
        });
    }
}