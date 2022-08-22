package com.masterweb.japripay.activity.members;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.GoogleCloud;
import com.masterweb.japripay.classes.request.post.Members;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {
    ImageView back;
    EditText name,phone,reff;
    CountryCodePicker code;
    Members members;
    TextView link;
    Button reg_submit;
    MasterFunction masterFunction;
    Call<MembersModel> data;
    ApiInterface apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        apiService = GoogleCloud.getClient().create(ApiInterface.class);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        setup();
        members = new Members(getApplicationContext(),DaftarActivity.this);
        masterFunction = new MasterFunction(getApplicationContext(),DaftarActivity.this);
        members.Register(reff,link);
        reff.setEnabled(false);

    }
    private void setup(){
        back = findViewById(R.id.back);
        link = findViewById(R.id.link);
        name = findViewById(R.id.name);
        reff = findViewById(R.id.reff);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        reg_submit = findViewById(R.id.reg_submit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        reg_submit.setOnClickListener(view -> {
            masterFunction.hideButton();
            Register();
        });
    }
    public void Register(){
        data = apiService.getMembers(masterFunction.get_phone());
        data.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getType().matches("0")){
                        masterFunction.errorMessage("Maaf untuk pendaftaran mitra sementara off");
                    }else{
                        if (masterFunction.validate(name,"Nama Lengkap tidak boleh kosong")
                                && masterFunction.validate(phone,"No Handphone tidak boleh kosong")){
                            members.RegisterApp(name,phones_reg(),reff);
                        }
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
    String phones_reg(){
        return code.getSelectedCountryCode()+phone.getText().toString();
    }
}