package com.masterweb.japripay.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.request.post.Login;

public class AuthActivity extends AppCompatActivity {
    MasterFunction master;
    Login Auth;
    Button login;
    TextView register;
    EditText phone,pin;
    CountryCodePicker code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        View();
    }
    private void View(){
        master = new MasterFunction(getApplicationContext(),AuthActivity.this);
        Auth = new Login(getApplicationContext(),AuthActivity.this);
        login = findViewById(R.id.signin);
        register = findViewById(R.id.register);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        pin = findViewById(R.id.pin);
        login.setOnClickListener(V->{
            master.hideButton();
            if (master.validate(phone,"No Handphone tidak boleh kosong") && master.validate(pin,"PIN Transaksi tidak boleh kosong")){
                Auth.masuk(getPhone(),pin.getText().toString());
            }
        });
    }
    private String getPhone(){
        return code.getSelectedCountryCode()+phone.getText().toString();
    }
}