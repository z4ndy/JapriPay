package com.masterweb.japripay.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.request.post.Login;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpActivity extends AppCompatActivity {
    TextView phone_text,tTimer;
    Button resend;
    OtpTextView otpTextView;
    Login Auth;
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Auth = new Login(getApplicationContext(),OtpActivity.this);
        String no_ho = getIntent().getExtras().getString("phone");
        Auth.sendOtp(no_ho);
        setView();
        phone_text.setText(no_ho);
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }
            @Override
            public void onOTPComplete(String otp) {
                Auth.validasiOtp(phone_text.getText().toString(),otp.trim());
            }
        });
        showTimer();
        resend.setOnClickListener(view -> {
            Auth.sendOtp(no_ho);
            resend.setVisibility(View.GONE);
            showTimer();

        });

    }
    void setView(){
        phone_text = findViewById(R.id.phone_text);
        tTimer = findViewById(R.id.timmer);
        resend = findViewById(R.id.resend);
        icon = findViewById(R.id.img);
        otpTextView = findViewById(R.id.otp_view);
    }
    void showTimer(){
        new CountDownTimer(120000, 1000) {
            public void onTick(long duration) {
                long Mmin = (duration / 1000) / 60;
                long Ssec = (duration / 1000) % 60;
                if (Ssec < 10) {
                    tTimer.setText("" + Mmin + ":0" + Ssec);
                } else tTimer.setText("" + Mmin + ":" + Ssec);
            }
            public void onFinish() {
                tTimer.setVisibility(View.GONE);
                resend.setVisibility(View.VISIBLE);
            }

        }.start();
    }
}