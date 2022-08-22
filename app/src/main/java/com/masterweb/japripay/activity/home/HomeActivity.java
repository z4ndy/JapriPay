package com.masterweb.japripay.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.UnderActivity;
import com.masterweb.japripay.activity.members.DaftarActivity;
import com.masterweb.japripay.activity.members.MitraActivity;
import com.masterweb.japripay.activity.transaksi.LainnyaActivity;
import com.masterweb.japripay.activity.transaksi.TopUpActivity;
import com.masterweb.japripay.activity.transaksi.pascabayar.PaymentActivity;
import com.masterweb.japripay.activity.transaksi.prabayar.PrefixActivity;
import com.masterweb.japripay.activity.transaksi.prabayar.ProdukActivity;
import com.masterweb.japripay.activity.transfer.TransferActivity;
import com.masterweb.japripay.chat.ListUserActivity;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.request.get.Info;
import com.masterweb.japripay.classes.request.get.Promo;
import com.masterweb.japripay.classes.request.post.Members;
import com.masterweb.japripay.service.MasterService;

import java.util.TimerTask;

import io.reactivex.disposables.Disposable;

public class HomeActivity extends AppCompatActivity {
    TextView name,saldo,bonus;
    Members members;
    LinearLayout menu;
    FloatingActionButton fab;
    ShimmerFrameLayout shimmer_info,shimmer_promo;
    RecyclerView list_info,list_promo;
    Info info;
    FrameLayout frame;
    NestedScrollView scrool;
    Promo promo;
    LinearLayout qrcode,harga,laporan,akun,home;
    TimerTask timerTask;
    CountDownTimer timer;
    MasterService masterService;
    Intent mServiceIntent;
    Disposable disposable;
    TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        View();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("newToken", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        //Toast.makeText(HomeActivity.this, ""+token, Toast.LENGTH_SHORT).show();
                        members.UpdateToken(token);

                    }
                });


    }
    private void View(){
        members = new Members(getApplicationContext(),HomeActivity.this);
        name = findViewById(R.id.name);
        fab = findViewById(R.id.fab);
        menu = findViewById(R.id.menu);
        saldo = findViewById(R.id.saldo);
        bonus = findViewById(R.id.bonus);
        home = findViewById(R.id.home);
        txtStatus = findViewById(R.id.txtStatus);
        members.Home(bonus,name,saldo);

        //Head

        shimmer_info = findViewById(R.id.shimmer_info);
        list_info = findViewById(R.id.list_info);
        info = new Info(getApplicationContext(),list_info);
        info.getInfo(shimmer_info);
        //Info

        frame = findViewById(R.id.frame);
        scrool = findViewById(R.id.scrool);
        scrool.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY > oldScrollY) {
                frame.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                if (menu.getVisibility() == View.VISIBLE){
                    menu.setVisibility(View.GONE);
                }
                Log.d("scrooll", scrollY+" "+oldScrollY);
            } else if(scrollY == 0){
                frame.setVisibility(View.VISIBLE);
                if (menu.getVisibility() == View.VISIBLE){
                    menu.setVisibility(View.GONE);
                }
            }
            new Handler().postDelayed(() -> {
                fab.setVisibility(View.VISIBLE);
                if (menu.getVisibility() == View.VISIBLE){
                    menu.setVisibility(View.GONE);
                }
            }, 800);
        });
        fab.setOnClickListener(V->{
            if (menu.getVisibility() == View.VISIBLE){
                menu.setVisibility(View.GONE);
            }else{
                menu.setVisibility(View.VISIBLE);
            }
        });

        shimmer_promo = findViewById(R.id.shimmer_promo);
        list_promo = findViewById(R.id.list_promo);
        promo = new Promo(getApplicationContext(),list_promo);
        promo.getPromo(shimmer_promo);
        //Info

        qrcode = findViewById(R.id.qrcode);
        harga = findViewById(R.id.harga);
        laporan = findViewById(R.id.laporan);
        akun = findViewById(R.id.akun);
        home.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        qrcode.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), QrcodeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        harga.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), ListUserActivity.class);
            //intent.putExtra("key","Japri Messenger");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        laporan.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), LaporanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        akun.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), AkunActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout transfer = findViewById(R.id.transfer);
        transfer.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), TransferActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout topup = findViewById(R.id.topup);
        topup.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), TopUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout mitra = findViewById(R.id.mitra);
        mitra.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), MitraActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout daftar = findViewById(R.id.daftar);
        daftar.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), DaftarActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout pulsa = findViewById(R.id.pulsa);
        pulsa.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), PrefixActivity.class);
            intent.putExtra("key","Pulsa");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout pln = findViewById(R.id.pln);
        pln.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), ProdukActivity.class);
            intent.putExtra("key","pln");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout data = findViewById(R.id.data);
        data.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), ProdukActivity.class);
            intent.putExtra("key","kuota");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout donasi = findViewById(R.id.donasi);
        donasi.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), ProdukActivity.class);
            intent.putExtra("key","money");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout bpjs = findViewById(R.id.bpjs);
        bpjs.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
            intent.putExtra("key","bpjs");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout tv_pasca = findViewById(R.id.internet_pasca);
        tv_pasca.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
            intent.putExtra("key","internet_pasca");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout lainnya = findViewById(R.id.lainnya);
        lainnya.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), LainnyaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        LinearLayout streaming = findViewById(R.id.streaming);
        streaming.setOnClickListener(V->{
            menu.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), UnderActivity.class);
            intent.putExtra("key","Streaming");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
//        masterService = new MasterService();
//        mServiceIntent = new Intent(this, masterService.getClass());
//        if (!isMyServiceRunning(masterService.getClass())) {
//
//            startService(mServiceIntent);
//        }
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
    }
    @Override
    protected void onResume(){
        super.onResume();
       members.Home(bonus,name,saldo);
    }
    private boolean isMyServiceRunning(Class serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

}