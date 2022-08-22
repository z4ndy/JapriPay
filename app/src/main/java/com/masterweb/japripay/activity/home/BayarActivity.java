package com.masterweb.japripay.activity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.request.post.Transfer;

public class BayarActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView name,phone;
    EditText amount;
    Button bayar;
    Transfer send;
    MasterFunction masterFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        toolbar = findViewById(R.id.toolbar);
        amount = findViewById(R.id.amount);
        bayar = findViewById(R.id.bayar);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        name.setText(getIntent().getExtras().getString("name"));
        phone.setText(getIntent().getExtras().getString("phone"));
        toolbar.setOnClickListener(v->{
            onBackPressed();
        });
        send = new Transfer(getApplicationContext(), BayarActivity.this);
        masterFunction = new MasterFunction(getApplicationContext(),BayarActivity.this);
        bayar.setOnClickListener(V->{
            masterFunction.hideButton();
            if (masterFunction.validate(amount,"Jumlah Pembayaran tidak boleh Kosong")){
                send.kirimQRcode(phone.getText().toString(),amount.getText().toString());
            }
        });
    }

}