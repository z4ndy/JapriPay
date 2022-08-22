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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.masterweb.japripay.classes.dialog.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukActivity extends AppCompatActivity {
    ImageView back;
    LinearLayout pulsa;
    Spinner produk;
    EditText phone;
    ImageView submit;
    TextView title,kategori;
    MasterFunction masterFunction;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView list_prabayar;
    List<HargaModel> listDaily;
    ApiInterface apiService;
    PrabayarAdapter hargaAdapter;
    Message message;
    ArrayList<String> ProviderName;
    String URLS = "https://windows.kitakaya.in/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        masterFunction = new MasterFunction(getApplicationContext(),ProdukActivity.this);
        message = new Message(ProdukActivity.this);
        String keys = getIntent().getExtras().getString("key");
        addClass(keys);


        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        list_prabayar = findViewById(R.id.list_pra);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        listDaily = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        list_prabayar.setLayoutManager(layoutManager);
        hargaAdapter = new PrabayarAdapter(getApplicationContext(),listDaily,ProdukActivity.this);
        list_prabayar.setAdapter(hargaAdapter);
        shimmerFrameLayout.setVisibility(View.GONE);
    }
    private void addClass(String key){
        viewId();
        setup(key);
    }
    private void viewId(){
        pulsa = findViewById(R.id.pulsa);
        produk = findViewById(R.id.produk);
        kategori = findViewById(R.id.kategori);
        phone = findViewById(R.id.pelanggan);
        submit = findViewById(R.id.submit);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        list_prabayar = findViewById(R.id.list_pra);
    }
    private void setup(String key){
        switch (key){
            case "pulsa":
                title.setText("Pulsa Prabayar");
                produk.setVisibility(View.GONE);
                phone.setHint("No. Handphone");
                submit.setImageResource(R.drawable.contact);
                ambilContact("Pulsa");
                phone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (start > 8){
                            getData(phone.getText().toString().substring(0,4),"Pulsa");
                        }
                        if (start == 0){
                            shimmerFrameLayout.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void afterTextChanged(final Editable s) {

                    }
                });
                break;
            case "kuota":
                title.setText("Kuota dan Paket Data");
                phone.setHint("No. Handphone");
                produk.setVisibility(View.GONE);
                submit.setImageResource(R.drawable.contact);
                ambilContact("Data");
                phone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (start > 8){
                            getData(phone.getText().toString().substring(0,4),"Data");
                        }
                        if (start == 0){
                            shimmerFrameLayout.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void afterTextChanged(final Editable s) {

                    }
                });
                break;
            case "pln":
                title.setText("Token PLN Prabayar");
                phone.setHint("No. Pelanggan");
                produk.setVisibility(View.GONE);
                submit.setOnClickListener(V->{
                    if (masterFunction.validate(phone,"Masukan no pelanggan atau meteran PLN anda")){
                        getKategori("PLN");
                    }
                });
                break;
            case "money":
                title.setText("Pengisian E-money");
                ProviderName=new ArrayList<>();
                getProvider(URLS+"produk/emoney");
                produk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        phone.requestFocus();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                submit.setOnClickListener(V->{
                    if (masterFunction.validate(phone,"Masukan no pelanggan atau meteran E-Money anda")){
                        getbrand(produk.getSelectedItem().toString());
                    }
                });
                break;
            case "tlp":
                title.setText("Paket Tlp. dan SMS");
                produk.setVisibility(View.GONE);
                phone.setHint("No. Handphone");
                submit.setImageResource(R.drawable.contact);
                ambilContact("Paket SMS & Telpon");
                phone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (start > 8){
                            getData(phone.getText().toString().substring(0,4),"Paket SMS & Telpon");
                        }
                        if (start == 0){
                            shimmerFrameLayout.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void afterTextChanged(final Editable s) {

                    }
                });

                break;
            case "tv":
                title.setText("TV Prabayar");
                produk.setVisibility(View.GONE);
                break;
            case "voucher":
                title.setText("Pengisian Voucher");
                ProviderName=new ArrayList<>();
                getProvider(URLS+"produk/vocher");
                produk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        phone.requestFocus();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                submit.setOnClickListener(V->{
                    getvoucher(produk.getSelectedItem().toString());
                });
                break;
            case "game":
                title.setText("Voucher Games");
                ProviderName=new ArrayList<>();
                getProvider(URLS+"produk/games");
                produk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        phone.requestFocus();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                submit.setOnClickListener(V->{
                    if (masterFunction.validate(phone,"Masukan no pelanggan atau meteran E-Money anda")){
                        getbrand(produk.getSelectedItem().toString());
                    }
                });
                break;
        }
    }
    private void getProvider(String url){
        message.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("viewprovider", "onResponse: "+response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String country=jsonObject1.getString("brand");
                        ProviderName.add(country);
                    }
                    produk.setAdapter(new ArrayAdapter<String>(ProdukActivity.this, R.layout.list_emmoney, ProviderName));
                    message.dismiss();
                }catch (JSONException e){
                    e.printStackTrace();
                    message.dismiss();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                message.dismiss();
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
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
    private void getKategori(String kategori){
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        Call<List<HargaModel>> call = apiService.getkatPrabayar(masterFunction.get_phone(),kategori,phone.getText().toString());
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
    private void getbrand(String kategori){
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        Call<List<HargaModel>> call = apiService.getbrandPrabayar(masterFunction.get_phone(),kategori,phone.getText().toString());
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
    private void getvoucher(String kategori){
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        Call<List<HargaModel>> call = apiService.getvoucherPrabayar(masterFunction.get_phone(),kategori,phone.getText().toString());
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
    private void ambilContact(String category){
        kategori.setText(category);
        submit.setOnClickListener(view -> {
            masterFunction.hideButton();
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, 1);
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Cursor cursor = null;
            try {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    String phones = cursor.getString(0);
                    String phone_member = phones.replaceAll("[^\\d.]", "");
                    String no = phone_member.substring(0,2);
                    if (no.matches("62")){
                        String no_hp = "0"+phone_member.substring(2);
                        phone.setText(no_hp);
                        getData(no_hp.substring(0,4),kategori.getText().toString());
                    }else{
                        phone.setText(phone_member);
                        getData(phone_member.substring(0,4),kategori.getText().toString());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}