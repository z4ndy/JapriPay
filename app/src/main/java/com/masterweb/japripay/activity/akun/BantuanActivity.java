package com.masterweb.japripay.activity.akun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;

public class BantuanActivity extends AppCompatActivity {
    ImageView back;
    FloatingActionButton cs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);
        back = findViewById(R.id.back);
        cs = findViewById(R.id.cs);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_VIEW);
                sendIntent.setPackage("com.whatsapp");
                String pesan = "Info, CS JapriPay.";
                String url = "https://api.whatsapp.com/send?phone=6285299998766&text=" + pesan;
                sendIntent.setData(Uri.parse(url));
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(sendIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(sendIntent);
                }else{
                    Toast.makeText(getApplicationContext(), "Pastikan anda memiliki Aplikasi WhatsApp", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}