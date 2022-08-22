package com.masterweb.japripay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.request.post.Prabayar;

public class BaseApp extends AppCompatActivity {
    Prabayar prabayar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_app);
        prabayar = new Prabayar(getApplicationContext(),BaseApp.this);
        prabayar.prosesTransaksi(getIntent().getExtras().getString("price"),
                getIntent().getExtras().getString("pin"),
                getIntent().getExtras().getString("produk"),
                getIntent().getExtras().getString("cst"),
                getIntent().getExtras().getString("invoice")
                );
    }


}