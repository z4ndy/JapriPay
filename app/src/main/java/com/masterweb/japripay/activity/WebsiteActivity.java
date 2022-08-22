package com.masterweb.japripay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.Website;

public class WebsiteActivity extends AppCompatActivity {
    Website website;
    WebView webView;
    TextView title;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
        String url = getIntent().getExtras().getString("url");
        String judul = getIntent().getExtras().getString("title");
        webView = findViewById(R.id.webview);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        title.setText(judul);
        website = new Website(getApplicationContext(),WebsiteActivity.this);
        website.Views(webView,url);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}