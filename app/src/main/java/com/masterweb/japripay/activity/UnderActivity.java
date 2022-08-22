package com.masterweb.japripay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;

public class UnderActivity extends AppCompatActivity {
    ImageView back;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        String keys = getIntent().getExtras().getString("key");
        title.setText(keys);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}