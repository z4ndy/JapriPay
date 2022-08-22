package com.masterweb.japripay.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.request.post.Login;

public class RegisterActivity extends AppCompatActivity {
    MasterFunction master;
    Login Auth;
    Button login;
    TextView register;
    EditText phone;
    CountryCodePicker code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        View();
    }
    private void View(){
        master = new MasterFunction(getApplicationContext(),RegisterActivity.this);
        Auth = new Login(getApplicationContext(),RegisterActivity.this);
        login = findViewById(R.id.signup);
        register = findViewById(R.id.register);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
//        login.setOnClickListener(V->{
//            master.hideButton();
//            if (master.validate(phone,"No Handphone tidak boleh kosong")){
//                Auth.masuk(getPhone());
//            }
//        });
        register.setOnClickListener(V->{
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
    private String getPhone(){
        return code.getSelectedCountryCode()+phone.getText().toString();
    }
}