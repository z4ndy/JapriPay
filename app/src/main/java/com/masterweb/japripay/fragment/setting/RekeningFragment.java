package com.masterweb.japripay.fragment.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.MembersModel;
import com.masterweb.japripay.classes.Model.ResultModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.GoogleCloud;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RekeningFragment extends Fragment {
    Call<ResultModel> data;
    Call<MembersModel> datas;
    ApiInterface apiService;
    MasterFunction masterFunction;
    EditText name,norek;
    AutoCompleteTextView bank;
    Button simpan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rekening, container, false);
        AdView mAdView = root.findViewById(R.id.adView);
        AdMob adMob = new AdMob(getContext());
        adMob.load(mAdView);
        masterFunction = new MasterFunction(getContext(),getActivity());
        apiService = GoogleCloud.getClient().create(ApiInterface.class);
        bank = root.findViewById(R.id.bank);
        name = root.findViewById(R.id.name);
        norek = root.findViewById(R.id.norek);
        simpan = root.findViewById(R.id.simpan);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_bank, R.id.text, masterFunction.bank_name());
        bank.setThreshold(1);
        bank.setAdapter(arrayAdapter);
        bank.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        getBank();
        simpan.setOnClickListener(v->{
            masterFunction.hideButton();
            Register();
        });
        return root;
    }
    public void Register(){
        datas = apiService.getMembers(masterFunction.get_phone());
        datas.enqueue(new Callback<MembersModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MembersModel> call, @NonNull Response<MembersModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getType().matches("2")){
                        masterFunction.errorMessage("Maaf belum bisa update nomor rekening, seilahkan withdraw menggunakan e-Money");
                    }else{
                        if (masterFunction.validate(bank,"Nama Bank")
                                && masterFunction.validate(norek,"Nomor Rekening")
                                && masterFunction.validate(name,"Nama Pemilik Rekening")){
                            masterFunction.hideButton();
                            UpdateProfile();
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
    public void getBank() {
        data = apiService.getBank(masterFunction.get_phone());
        data.enqueue(new Callback<ResultModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResultModel> call, @NonNull Response<ResultModel> response) {
                Log.d("retroffit_regis", "" + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    bank.setText(response.body().getPhone());
                    name.setText(response.body().getName());
                    norek.setText(response.body().getMessage());
                } else {
                    Log.d("body_auth", "" + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", "" + t.getMessage());
            }
        });
    }
    public void UpdateProfile(){
        data = apiService.updateBank(masterFunction.get_phone(),bank.getText().toString(),norek.getText().toString(),name.getText().toString());
        data.enqueue(new Callback<ResultModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResultModel> call, @NonNull Response<ResultModel> response) {
                Log.d("retroffit_regis", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        masterFunction.successMessage(response.body().getMessage());
                    }else{
                        masterFunction.errorMessage(response.body().getMessage());
                    }

                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                simpan.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(@NonNull Call<ResultModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());

            }
        });
    }
}