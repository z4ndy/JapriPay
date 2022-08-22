package com.masterweb.japripay.activity.members;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.request.get.Mitra;
import com.masterweb.japripay.classes.request.post.Members;

public class MitraActivity extends AppCompatActivity {
    ImageView back;
    RecyclerView list_mitra;
    ShimmerFrameLayout shimmer;
    Mitra mitra;
    TextView jumlah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra);
        setup();
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
    }
    public void setup(){
        back = findViewById(R.id.back);
        jumlah = findViewById(R.id.jumlah);
        list_mitra = findViewById(R.id.list_mitra);
        shimmer = findViewById(R.id.shimmer);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mitra = new Mitra(getApplicationContext(),MitraActivity.this,list_mitra);
        Members members = new Members(getApplicationContext(),MitraActivity.this);
        mitra.getData(shimmer);
        members.Mitra(jumlah);
    }
}