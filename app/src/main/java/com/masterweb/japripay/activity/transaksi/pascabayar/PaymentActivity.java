package com.masterweb.japripay.activity.transaksi.pascabayar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.transaksi.TopUpActivity;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.Dialogs;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.PascaModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;
import com.masterweb.japripay.classes.dialog.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class PaymentActivity extends AppCompatActivity {
    ImageView back,submit;
    TextView title,code,saldo;
    EditText phone;
    ApiInterface apiService;
    Spinner spinner;
    ArrayList<String> ProviderName;
    Message message;
    MasterFunction masterFunction;
    TextView invoice,product,customers,nama,tagihan,admin,jumlah;
    LinearLayout input,hasil;
    Call<PascaModel> data;
    ImageView right;
    Button topup;
    Button bayar;
    String URLS = "https://windows.kitakaya.in/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        message = new Message(PaymentActivity.this);
        masterFunction = new MasterFunction(getApplicationContext(),PaymentActivity.this);
        String keys = getIntent().getExtras().getString("key");
        ProviderName=new ArrayList<>();
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        spinner = findViewById(R.id.produk);
        code = findViewById(R.id.code);
        phone = findViewById(R.id.phone);
        bayar = findViewById(R.id.bayar);
        submit = findViewById(R.id.submit);

        invoice = findViewById(R.id.invoice);
        product = findViewById(R.id.product);
        customers = findViewById(R.id.customers);
        nama = findViewById(R.id.nama);
        tagihan = findViewById(R.id.tagihan);
        admin = findViewById(R.id.admin);
        jumlah = findViewById(R.id.jumlah);

        input = findViewById(R.id.input);
        hasil = findViewById(R.id.hasil);
        saldo = findViewById(R.id.saldo);
        right = findViewById(R.id.right);
        topup = findViewById(R.id.topup);

        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle(keys);
        topup.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), TopUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
    private void cek_tagihan(String product){
        input.setVisibility(View.GONE);
        message.show();
        data = apiService.cekTagihan(masterFunction.get_phone(), invoice.getText().toString(),phone.getText().toString(),product);
        data.enqueue(new Callback<PascaModel>() {
            @Override
            public void onResponse(@NonNull Call<PascaModel> call, @NonNull retrofit2.Response<PascaModel> response) {
                Log.d("cek_tagihan", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    hasil.setVisibility(View.VISIBLE);
                    nama.setText(response.body().getNama());
                    tagihan.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getPrice())));
                    admin.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getAdmin())));
                    jumlah.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getTotal())));
                    saldo.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(response.body().getSaldo())));
                    if (Integer.parseInt(response.body().getSaldo()) > Integer.parseInt(response.body().getTotal())){
                        bayar.setVisibility(View.VISIBLE);
                        bayar.setOnClickListener(V->{
                            Dialogs dialogs = new Dialogs(PaymentActivity.this);
                            dialogs.pay_pascabayar(response.body().getTotal(),product,phone.getText().toString(),invoice.getText().toString());
                        });
                    }else{
                        right.setVisibility(View.GONE);
                        topup.setVisibility(View.VISIBLE);
                    }
                    message.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }

            }
            @Override
            public void onFailure(@NonNull Call<PascaModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());

            }
        });
    }

    private void setTitle(String keys){
        switch (keys){
            case "payment":
                title.setText("MULTIFINANCE");
                getProvider(URLS+"produk/pasca_bayar?brand=MULTIFINANCE");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        phone.requestFocus();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        masterFunction.hideButton();
                        product.setText(spinner.getSelectedItem().toString());
                        invoice.setText("pasca"+MasterFunction.getInvoice());
                        customers.setText(phone.getText().toString());
                        if(masterFunction.validate(phone,"No Pelanggan tidak boleh kosong")){
                            cek_tagihan(spinner.getSelectedItem().toString());
                        }
                    }
                });
                break;
            case "bpjs":
                title.setText("BPJS KESEHATAN");
                spinner.setVisibility(View.GONE);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        masterFunction.hideButton();
                        invoice.setText("pasca"+MasterFunction.getInvoice());
                        product.setText("Bpjs Kesehatan");
                        customers.setText(phone.getText().toString());
                        if(masterFunction.validate(phone,"No Pelanggan tidak boleh kosong")){
                            cek_tagihan("Bpjs Kesehatan");
                        }
                    }
                });
                break;
            case "pln":
                title.setText("PLN PASCABAYAR");
                spinner.setVisibility(View.GONE);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        masterFunction.hideButton();
                        invoice.setText("pasca"+MasterFunction.getInvoice());
                        product.setText("Pln Pascabayar");
                        customers.setText(phone.getText().toString());
                        if(masterFunction.validate(phone,"No Pelanggan tidak boleh kosong")){
                            cek_tagihan("Pln Pascabayar");
                        }
                    }
                });
                break;
            case "pbb":
                title.setText("PBB");
                getProvider(URLS+"produk/pasca_bayar?brand=PBB");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        phone.requestFocus();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        masterFunction.hideButton();
                        product.setText(spinner.getSelectedItem().toString());
                        invoice.setText("pasca"+MasterFunction.getInvoice());
                        customers.setText(phone.getText().toString());
                        if(masterFunction.validate(phone,"No Pelanggan tidak boleh kosong")){
                            cek_tagihan(spinner.getSelectedItem().toString());
                        }
                    }
                });
                break;
            case "gas":
                title.setText("GAS NEGARA");
                spinner.setVisibility(View.GONE);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        masterFunction.hideButton();
                        product.setText("Gas Negara");
                        invoice.setText("pasca"+MasterFunction.getInvoice());
                        customers.setText(phone.getText().toString());
                        if(masterFunction.validate(phone,"No Pelanggan tidak boleh kosong")){
                            cek_tagihan("Gas Negara");
                        }
                    }
                });
                break;
            case "hp":
                title.setText("HP PASCABAYAR");
                getProvider(URLS+"produk/pasca_bayar?brand=HP%20PASCABAYAR");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        phone.requestFocus();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        masterFunction.hideButton();
                        product.setText(spinner.getSelectedItem().toString());
                        invoice.setText("pasca"+MasterFunction.getInvoice());
                        customers.setText(phone.getText().toString());
                        if(masterFunction.validate(phone,"No Pelanggan tidak boleh kosong")){
                            cek_tagihan(spinner.getSelectedItem().toString());
                        }
                    }
                });
                break;
            case "pdam":
                title.setText("PDAM");
                getProvider(URLS+"produk/pasca_bayar?brand=PDAM");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        phone.requestFocus();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        masterFunction.hideButton();
                        product.setText(spinner.getSelectedItem().toString());
                        invoice.setText("pasca"+MasterFunction.getInvoice());
                        customers.setText(phone.getText().toString());
                        if(masterFunction.validate(phone,"No Pelanggan tidak boleh kosong")){
                            cek_tagihan(spinner.getSelectedItem().toString());
                        }
                    }
                });
                break;
            case "internet_pasca":
                title.setText("INTERNET PASCABAYAR");
                getProvider(URLS+"produk/pasca_bayar?brand=INTERNET%20PASCABAYAR");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        phone.requestFocus();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // DO Nothing here
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        masterFunction.hideButton();
                        product.setText(spinner.getSelectedItem().toString());
                        invoice.setText("pasca"+MasterFunction.getInvoice());
                        customers.setText(phone.getText().toString());
                        if(masterFunction.validate(phone,"No Pelanggan tidak boleh kosong")){
                            cek_tagihan(spinner.getSelectedItem().toString());
                        }
                    }
                });
                break;
        }
    }
    void getProvider(String url){
        message.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("viewprovider", "onResponse: "+response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String country=jsonObject1.getString("name");
                        ProviderName.add(country);
                    }
                    spinner.setAdapter(new ArrayAdapter<String>(PaymentActivity.this, R.layout.list_emmoney, ProviderName));
                    message.dismiss();
                }catch (JSONException e){
                    e.printStackTrace();
                    message.dismiss();
                }
            }
        }, new Response.ErrorListener() {
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
}