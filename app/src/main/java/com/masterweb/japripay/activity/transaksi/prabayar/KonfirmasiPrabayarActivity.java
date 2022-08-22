package com.masterweb.japripay.activity.transaksi.prabayar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.transaksi.TopUpActivity;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.request.post.Members;

public class KonfirmasiPrabayarActivity extends AppCompatActivity {
    TextView produk,detail,customers,price,invoice,saldo,subtotal;
    ImageView back,right;
    MasterFunction masterFunction;
    Members members;
    Button topup,lanjutkan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_prabayar);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        masterFunction = new MasterFunction(getApplicationContext(),KonfirmasiPrabayarActivity.this);
        members = new Members(getApplicationContext(),KonfirmasiPrabayarActivity.this);
        produk = findViewById(R.id.produk);
        detail = findViewById(R.id.detail);
        customers = findViewById(R.id.customers);
        price = findViewById(R.id.price);
        back = findViewById(R.id.back);
        saldo = findViewById(R.id.saldo);
        subtotal = findViewById(R.id.total);
        invoice = findViewById(R.id.invoice);
        right = findViewById(R.id.right);
        topup = findViewById(R.id.topup);
        lanjutkan = findViewById(R.id.lanjutkan);
        invoice.setText("PRA"+masterFunction.getInvoice());
        produk.setText(getIntent().getExtras().getString("produk"));
        detail.setText(getIntent().getExtras().getString("detail"));
        customers.setText(getIntent().getExtras().getString("cst"));
        price.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(getIntent().getExtras().getString("price"))));
        subtotal.setText("Rp. "+masterFunction.Rupiah(Integer.parseInt(getIntent().getExtras().getString("price"))));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        members.getKomirmasiSaldo(saldo,
                getIntent().getExtras().getString("price"),
                topup,
                right,
                lanjutkan,
                getIntent().getExtras().getString("code"),
                getIntent().getExtras().getString("cst"),
                invoice.getText().toString()
        );
        topup.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), TopUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}