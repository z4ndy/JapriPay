package com.masterweb.japripay.activity.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.UpgradeActivity;
import com.masterweb.japripay.activity.WebsiteActivity;
import com.masterweb.japripay.activity.akun.BantuanActivity;
import com.masterweb.japripay.activity.akun.LaporaBonusActivity;
import com.masterweb.japripay.activity.akun.LaporanDepositActivity;
import com.masterweb.japripay.activity.akun.LapotanTransaksiActivity;
import com.masterweb.japripay.activity.akun.MutasiSaldoActivity;
import com.masterweb.japripay.activity.akun.SettingActivity;
import com.masterweb.japripay.activity.akun.WithdrawlActivity;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.Dialogs;
import com.masterweb.japripay.classes.request.post.Members;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class AkunActivity extends AppCompatActivity {
    LinearLayout back,privacy,term,view_data,upgrade;
    TextView name,phone,saldo,saham;
    Members members;
    ImageView qrcode;
    Dialogs dialogs;
    Button keluar;
    TimerTask timerTask;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        dialogs = new Dialogs(AkunActivity.this);
        back = findViewById(R.id.back);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        saldo = findViewById(R.id.saldo);
        privacy = findViewById(R.id.privacy);
        qrcode = findViewById(R.id.qrcode);
        term = findViewById(R.id.term);
        saham = findViewById(R.id.saham);
        view_data = findViewById(R.id.view_data);
        upgrade = findViewById(R.id.upgrade);
        upgrade.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), UpgradeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        privacy.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), WebsiteActivity.class);
            intent.putExtra("title","Kebijakan Privasi");
            intent.putExtra("url","windows.kitakaya.in/privacy");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        term.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), WebsiteActivity.class);
            intent.putExtra("title","Syarat dan Ketentuan");
            intent.putExtra("url","windows.kitakaya.in/term");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout transaksi = findViewById(R.id.transaksi);
        transaksi.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), LapotanTransaksiActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout bonus = findViewById(R.id.bonus);
        bonus.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), LaporaBonusActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout deposit = findViewById(R.id.deposit);
        deposit.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), LaporanDepositActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout mutasi = findViewById(R.id.mutasi);
        mutasi.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), MutasiSaldoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout setting = findViewById(R.id.setting);
        setting.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout wd = findViewById(R.id.wd);
        wd.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), WithdrawlActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout bantuan = findViewById(R.id.bantuan);
        bantuan.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), BantuanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        members = new Members(getApplicationContext(),AkunActivity.this);
        members.Akun(phone,name,saldo,saham,view_data,upgrade);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        qrcode.setOnClickListener(V->{
            String all = phone.getText().toString()+"#"+name.getText().toString()+"#"+saldo.getText().toString();
            String content = phone.getText().toString()+"#"
                    +name.getText().toString()+"#"
                    +saldo.getText().toString()
                    +"#"+toBase64(all);
            dialogs.showQrcode(content);
        });
        Dialogs dialogs = new Dialogs(AkunActivity.this);
        keluar = findViewById(R.id.keluar);
        keluar.setOnClickListener(v->{
            dialogs.NotifKeluar();
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        members.Akun(phone,name,saldo,saham,view_data,upgrade);
    }
    public static String toBase64(String message) {
        byte[] data;
        try {
            data = message.getBytes("UTF-8");
            String base64Sms = Base64.encodeToString(data, Base64.DEFAULT);
            return base64Sms;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

}