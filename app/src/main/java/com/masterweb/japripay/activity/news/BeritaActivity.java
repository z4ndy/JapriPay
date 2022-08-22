package com.masterweb.japripay.activity.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;

public class BeritaActivity extends AppCompatActivity {
    TextView title,judul,subjudul,tanggal,jam,konten;
    ImageView images,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        title = findViewById(R.id.title);
        judul = findViewById(R.id.judul);
        subjudul = findViewById(R.id.subjudul);
        tanggal = findViewById(R.id.tanggal);
        jam = findViewById(R.id.jam);
        konten = findViewById(R.id.konten);
        images = findViewById(R.id.images);
        back = findViewById(R.id.back);
        title.setText(getIntent().getExtras().getString("title"));
        judul.setText(getIntent().getExtras().getString("judul"));
        subjudul.setText(getIntent().getExtras().getString("subjudul"));
        tanggal.setText(getIntent().getExtras().getString("tanggal"));
        jam.setText(getIntent().getExtras().getString("jam"));
        Glide.with(getApplicationContext()).load(getIntent().getExtras().getString("images")).into(images);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            konten.setText(Html.fromHtml(getIntent().getExtras().getString("konten"), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            konten.setText(Html.fromHtml(getIntent().getExtras().getString("konten")));
//        }
        konten.setText(Html.fromHtml(getIntent().getExtras().getString("konten")));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}