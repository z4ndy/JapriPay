package com.masterweb.japripay.fragment.setting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;
import com.masterweb.japripay.classes.dialog.Message;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PinFragment extends Fragment {
    EditText pin,baru,ulangi;
    Button submit;
    MasterFunction masterFunction;
    Message message;
    Call<AuthModel> up;
    ApiInterface apiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pin, container, false);
        message = new Message(getActivity());
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        pin = root.findViewById(R.id.pin);
        baru = root.findViewById(R.id.baru);
        ulangi = root.findViewById(R.id.ulangi);
        submit = root.findViewById(R.id.submit);
        masterFunction = new MasterFunction(getContext(),getActivity());
        submit.setOnClickListener(V->{
            masterFunction.hideButton();
            if (masterFunction.validate(pin,"Mohon Isikan PIN yang aktif") &&
                    masterFunction.validate(baru,"Mohon Isikan PIN Baru Anda") &&
                    masterFunction.validate(ulangi,"Mohon Ulangi PIN Baru anda")){
                if (!baru.getText().toString().matches(ulangi.getText().toString())){
                    masterFunction.errorMessage("PIN yang anda masukan harus sama");
                }else{
                    CekPin(pin.getText().toString(),baru.getText().toString());
                }
            }
        });
        AdView mAdView = root.findViewById(R.id.adView);
        AdMob adMob = new AdMob(getContext());
        adMob.load(mAdView);
        return root;
    }
    private void CekPin(String pin,String baru){
        message.show();
        up = apiService.getPinTrx(masterFunction.get_phone(),pin);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("transaksi", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        UpdatePin(baru);
                    }else{
                        masterFunction.errorMessage("PIN yang anda masukan tidak sesuai");
                    }
                    message.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                message.dismiss();
            }
        });
    }
    private void UpdatePin(String pin){
        up = apiService.UpdatePinTrx(masterFunction.get_phone(),pin);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("transaksi", ""+new Gson().toJson(response.body()));
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
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());

            }
        });
    }
}