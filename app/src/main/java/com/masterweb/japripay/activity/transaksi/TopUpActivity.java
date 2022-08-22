package com.masterweb.japripay.activity.transaksi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.Dialogs;
import com.masterweb.japripay.classes.request.post.Members;

public class TopUpActivity extends AppCompatActivity {
    LinearLayout bca,bni,bri,mandiri,sampoerna;
    LinearLayout GOPAY,OVO,DANA;
    Members members;
    TextView saldo;
    Dialogs dialogs;
    LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        bca = findViewById(R.id.bca);
        GOPAY = findViewById(R.id.GOPAY);
        OVO = findViewById(R.id.OVO);
        DANA = findViewById(R.id.DANA);
        back = findViewById(R.id.back);
        saldo = findViewById(R.id.saldo);
        members = new Members(getApplicationContext(),TopUpActivity.this);
        dialogs = new Dialogs(TopUpActivity.this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        bri = findViewById(R.id.bri);
        mandiri = findViewById(R.id.mandiri);
        sampoerna = findViewById(R.id.sampoerna);
        bni = findViewById(R.id.bni);
        members.getSaldo(saldo);
        bca.setOnClickListener(V->{
            dialogs.topUp("BCA");
        });
        bri.setOnClickListener(V->{
            dialogs.topUp("BRI");
        });
        mandiri.setOnClickListener(V->{
            dialogs.topUp("MANDIRI");
        });
        bni.setOnClickListener(V->{
            dialogs.topUp("BNI");
        });
        sampoerna.setOnClickListener(V->{
            dialogs.topUp("Bank Sahabat Sampoerna");
        });
        DANA.setOnClickListener(V->{
            dialogs.topUp("DANA");
        });
        OVO.setOnClickListener(V->{
            dialogs.topUp("OVO");
        });
        GOPAY.setOnClickListener(V->{
            dialogs.topUp("GOPAY");
        });
    }
}