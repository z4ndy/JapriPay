package com.masterweb.japripay.activity.transfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.MasterFunction;
import com.masterweb.japripay.classes.request.post.Transfer;

public class TransferActivity extends AppCompatActivity {
    ImageView back;
    LinearLayout contact,input;
    EditText phone,amount;
    Transfer send;
    Button btn_kirim;
    MasterFunction masterFunction;
    TextView saldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        setView();
        AdView mAdView = findViewById(R.id.adView);
        AdMob adMob = new AdMob(getApplicationContext());
        adMob.load(mAdView);
    }
    private void setView(){
        saldo = findViewById(R.id.saldo);
        contact = findViewById(R.id.contact);
        phone = findViewById(R.id.phone);
        input = findViewById(R.id.input);
        amount = findViewById(R.id.amount);
        btn_kirim = findViewById(R.id.btn_kirim);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addcotact();
        send = new Transfer(getApplicationContext(),TransferActivity.this);
        masterFunction = new MasterFunction(getApplicationContext(),TransferActivity.this);
        btn_kirim.setOnClickListener(V->{
            masterFunction.hideButton();
            if (masterFunction.validate(phone,"No. HP Penerima tidak boleh kosong") &&
                    masterFunction.validate(amount,"Jumlah Kirim tidak boleh Kosong")){
                kirim_saldo();
            }
        });
        phone.setEnabled(false);
    }
    void kirim_saldo(){
        input.setVisibility(View.GONE);
        int jumlah = Integer.parseInt(amount.getText().toString());
        if(jumlah < 4999){
            masterFunction.errorMessage("Minimal pengiriman saldo 5.000");
        }else{
            send.kirimSaldo(phone.getText().toString(),amount.getText().toString());
        }
    }
    private void addcotact(){
        contact.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, 1);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Cursor cursor = null;
            try {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    String phones = cursor.getString(0);
                    String phone_member = phones.replaceAll("[^\\d.]", "");
                    String no = phone_member.substring(0, 1);
                    if (no.matches("0")) {
                        String no_hp = "62" + phone_member.substring(1);
                        phone.setText(no_hp);
                        send.cekMembers(no_hp,input);
                    } else {
                        phone.setText(phone_member);
                        send.cekMembers(phone_member,input);
                    }
                    amount.requestFocus();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}