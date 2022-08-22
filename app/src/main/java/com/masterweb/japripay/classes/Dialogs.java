package com.masterweb.japripay.classes;

import static com.masterweb.japripay.classes.MasterFunction.rand;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.InvoiceActivity;
import com.masterweb.japripay.activity.transaksi.prabayar.KonfirmasiPrabayarActivity;
import com.masterweb.japripay.classes.Model.AuthModel;
import com.masterweb.japripay.classes.api.ApiInterface;
import com.masterweb.japripay.classes.api.RetrofitClient;
import com.masterweb.japripay.classes.dialog.Message;

import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dialogs {
    View view;
    Activity mActivity;
    Button ok;
    TextView mText;
    Call<AuthModel> up;
    ApiInterface apiService;
    Message message;
    Keyboard keyboard;
    static char[] NUMBERS = "0123456789".toCharArray();
    public Dialogs(Activity activity) {
        this.mActivity = activity;
        apiService = RetrofitClient.getClient().create(ApiInterface.class);
        message = new Message(mActivity);
        keyboard = new Keyboard();
    }
    public void showError(String text) {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.progress_error, null, false);
        ok = view.findViewById(R.id.button);
        mText = view.findViewById(R.id.text);
        mText.setText(text);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(in.aabhasjindal.otptextview.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        ok.setOnClickListener(V->{
            dialog.dismiss();
        });
    }
    public void showSuccess(String text) {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.progress_success, null, false);
        ok = view.findViewById(R.id.button);
        mText = view.findViewById(R.id.text);
        mText.setText(text);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(in.aabhasjindal.otptextview.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        ok.setOnClickListener(V->{
            dialog.dismiss();
        });
    }
    public void kofirmasi(String code, String pelanggan, String product, String harga, String desc, LinearLayout list){
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.konfirmasi, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(in.aabhasjindal.otptextview.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);
        TextView cst = view.findViewById(R.id.customers);
        TextView produk = view.findViewById(R.id.produk);
        TextView price = view.findViewById(R.id.price);
        TextView detail = view.findViewById(R.id.detail);
        Button next = view.findViewById(R.id.next);
        Button ubah = view.findViewById(R.id.ubah);
        cst.setText(pelanggan);
        produk.setText(product);
        price.setText(harga);
        detail.setText(desc);
        next.setOnClickListener(V->{
            Intent intent = new Intent(mActivity, KonfirmasiPrabayarActivity.class);
            intent.putExtra("code",code);
            intent.putExtra("cst",pelanggan);
            intent.putExtra("produk",product);
            intent.putExtra("detail",desc);
            intent.putExtra("price",harga);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        });
        dialog.show();
        ubah.setOnClickListener(V->{
            list.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_card));
            dialog.dismiss();
        });

    }
    public boolean validate(EditText editText,String pesan) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        showError(pesan);
        editText.requestFocus();
        return false;
    }
    public  String get_phone(){
        Session session = new Session(mActivity);
        HashMap<String, String> user = session.getUserDetails();
        return user.get(Session.kunci_email);
    }
    private void prosesTransaksi(String price, String pin, String produk, String customers, String invoice){
        message.show();
        up = apiService.getPinTrx(get_phone(),pin);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("transaksi", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        beliPulsa(produk,customers,invoice,price);
                    }else{
                        showError("PIN yang anda masukan tidak sesuai");
                    }
                    message.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                message.dismiss();
            }
        });
    }
    private void beliPulsa(String produk,String customers,String invoice,String jual){
        message.show();
        up = apiService.beliPulsa(get_phone(),produk,customers,invoice,jual);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("beli_pulsa", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        showSuccess("Transaksi berhasil masuk dalam antrian");
                        Intent intent = new Intent(mActivity, InvoiceActivity.class);
                        intent.putExtra("key","prabayar");
                        intent.putExtra("invoice",invoice);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mActivity.startActivity(intent);
                    }else{
                        showError("Transaksi gagal masuk dalam antrian");
                    }
                    message.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                message.dismiss();
            }
        });
    }
    public void pay_prabayar(String harga,String produk,String customers,String invoice){
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pay_prabayar, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(in.aabhasjindal.otptextview.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);
        EditText jual = view.findViewById(R.id.jual);
        jual.setText(harga);
        EditText pin = view.findViewById(R.id.pin);
        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(V->{
            if (validate(jual,"Harga Jual tidak boleh kosong")
                    && validate(pin,"PIN Transaksi tidak boleh Kosong")){
                dialog.dismiss();
                prosesTransaksi(jual.getText().toString(), pin.getText().toString(), produk, customers,invoice);
            }
        });
        Button ubah = view.findViewById(R.id.ubah);
        dialog.show();
        ubah.setOnClickListener(V->{
            dialog.dismiss();
        });
    }
    private void DepositSaldo(String bank,String amount,String invoice){
        message.show();
        up = apiService.topupDeposit(get_phone(),bank,amount,invoice);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("beli_pulsa", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        showSuccess("Transaksi berhasil masuk dalam antrian");
                        Intent intent = new Intent(mActivity, InvoiceActivity.class);
                        intent.putExtra("key","topup");
                        intent.putExtra("invoice",invoice);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mActivity.startActivity(intent);
                    }else{
                        showError("Transaksi gagal masuk dalam antrian");
                    }
                    message.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                message.dismiss();
            }
        });
    }
    public static String getInvoice() {
        char[] password = new char[12];
        password[0] = NUMBERS[rand.nextInt(NUMBERS.length)];
        //populate rest of the password with random chars
        for (int i = 0; i < 12; i++) {
            password[i] = NUMBERS[rand.nextInt(NUMBERS.length)];
        }
        //shuffle it up
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }
        return new String(password).trim();
    }
    public int kode_unik(){
        Random r = new Random();
        int i1 = r.nextInt(999 - 501) + 701;
        return i1;
    }
    public void topUp(String banks){
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.add_topup, null, false);
        dialog.setCancelable(true);
        dialog.setContentView(view);
        TextView bank = view.findViewById(R.id.bank);
        bank.setText(banks);
        keyboard.setUpView(view);
        keyboard.tulis();
        EditText amount = view.findViewById(R.id.result);
        Button proses = view.findViewById(R.id.proses);
        TextView inv = view.findViewById(R.id.invoice);
        inv.setText("djp"+getInvoice());
        proses.setOnClickListener(V->{
            if (validate(amount,"Jumlah Top Up tidak boleh kosong")){
                String[] jumlah = amount.getText().toString().split("Rp ");
                String total = jumlah[1].replace(".","");
                if (Integer.parseInt(total) < 9999){
                    showError("Minimal TopUp Sebesar Rp. 10.000");
                }else if (Integer.parseInt(total) > 1000000){
                    showError("Maksimal Top Up sebesar Rp. 1.000.000");
                }else{
                    int uang = Integer.parseInt(total)+kode_unik();
                    String deposit = String.valueOf(uang);
                    DepositSaldo(bank.getText().toString(),deposit,inv.getText().toString());
                }

            }
        });
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(in.aabhasjindal.otptextview.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                keyboard.finish();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg) {
                keyboard.finish();
            }
        });
    }

    public void showQrcode(String text) {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.show_qrcode, null, false);
        dialog.setCancelable(true);
        dialog.setContentView(view);
        ImageView images = view.findViewById(R.id.images);
        Qrcode(images,text);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(in.aabhasjindal.otptextview.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
    }
    public void Qrcode(ImageView tnsd_iv_qr,String content){
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? ContextCompat.getColor(mActivity, R.color.black) : Color.WHITE);
                }
            }
            tnsd_iv_qr.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    public void NotifKeluar() {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.progress_error, null, false);
        ok = view.findViewById(R.id.button);
        mText = view.findViewById(R.id.text);
        mText.setText("Apakah anda yakin, mau keluar aplikasi ini...??");
        dialog.setCancelable(false);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(in.aabhasjindal.otptextview.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        Session session = new Session(mActivity);
        ok.setOnClickListener(V->{
            session.logout();
        });
    }
    private void prosesBayar(String price, String pin, String produk, String customers, String invoice){
        message.show();
        up = apiService.getPinTrx(get_phone(),pin);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("pembayaran", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        bayarTagihan(produk,customers,invoice,price);
                    }else{
                        showError("PIN Transaksi yang anda masukan tidak sesuai");
                    }
                    message.dismiss();
                }else{
                    Log.d("body_auth", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("DataModel_auth", ""+t.getMessage());
                message.dismiss();
            }
        });
    }
    private void bayarTagihan(String produk, String customers, String invoice, String price){
        message.show();
        up = apiService.bayarTagihan(get_phone(),invoice,customers,produk,price);
        up.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthModel> call, @NonNull Response<AuthModel> response) {
                Log.d("bayartagihan", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getValue().matches("1")){
                        Intent intent = new Intent(mActivity, InvoiceActivity.class);
                        intent.putExtra("key","pascabayar");
                        intent.putExtra("invoice",invoice);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mActivity.startActivity(intent);
                    }else{
                        showError(response.body().getMessage());
                    }
                    message.dismiss();
                }else{
                    Log.d("pasca error", ""+response.errorBody());
                }
                message.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<AuthModel> call, @NonNull Throwable t) {
                Log.d("pasca error", ""+t.getMessage());
                message.dismiss();
            }
        });
    }
    public void pay_pascabayar(String harga,String produk,String customers,String invoice){
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pay_prabayar, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(in.aabhasjindal.otptextview.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);
        EditText jual = view.findViewById(R.id.jual);
        jual.setText(harga);
        EditText pin = view.findViewById(R.id.pin);
        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(V->{
            if (validate(jual,"Harga Jual tidak boleh kosong") && validate(pin,"PIN Pembayaran tidak boleh Kosong")){
                dialog.dismiss();
                //Toast.makeText(mActivity, "Bayar Tagihan", Toast.LENGTH_SHORT).show();
                prosesBayar(jual.getText().toString(), pin.getText().toString(), produk, customers,invoice);
            }
        });
        Button ubah = view.findViewById(R.id.ubah);
        dialog.show();
        ubah.setOnClickListener(V->{
            dialog.dismiss();
        });
    }
}
