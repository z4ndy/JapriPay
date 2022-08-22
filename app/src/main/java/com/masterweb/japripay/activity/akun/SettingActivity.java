package com.masterweb.japripay.activity.akun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.adapter.SettingAdapter;

public class SettingActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    SettingAdapter settingAdapter;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        viewPager = findViewById(R.id.viewPagers_transfer);
        tabLayout = findViewById(R.id.tabs_transfer);
        settingAdapter = new SettingAdapter(getSupportFragmentManager());
        viewPager.setAdapter(settingAdapter);
        tabLayout.setupWithViewPager(viewPager);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
    }
}