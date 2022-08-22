package com.masterweb.japripay.classes;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masterweb.japripay.R;

import java.text.DecimalFormat;

public class Keyboard {
    TextView satu,dua,tiga,empat,lima,enam,tujuh,delapan,sembilan,nol,titik,title;
    EditText result;
    LinearLayout delete;
    int counter,del;
    String isian;
    public Keyboard(){
        counter = 0;
        del=0;
        isian = null;
    }
    public void setUpView(View v){
        result = v.findViewById(R.id.result);
        satu = v.findViewById(R.id.satu);
        dua = v.findViewById(R.id.dua);
        tiga = v.findViewById(R.id.tiga);
        empat = v.findViewById(R.id.empat);
        lima = v.findViewById(R.id.lima);
        enam = v.findViewById(R.id.enam);
        tujuh = v.findViewById(R.id.tujuh);
        delapan = v.findViewById(R.id.delapan);
        sembilan = v.findViewById(R.id.sembilan);
        nol = v.findViewById(R.id.nol);
        titik = v.findViewById(R.id.titik);
        delete = v.findViewById(R.id.del);
        title = v.findViewById(R.id.title);
    }
    public void tulis(){

        satu.setOnClickListener(v->{
            counter ++;
            isian = "1";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"1");
                result.setText(Rupiah(hasil));
            }
        });
        dua.setOnClickListener(v->{
            counter ++;
            isian = "2";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"2");
                result.setText(Rupiah(hasil));
            }
        });
        tiga.setOnClickListener(v->{
            counter ++;
            isian = "3";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"3");
                result.setText(Rupiah(hasil));
            }
        });
        empat.setOnClickListener(v->{
            counter ++;
            isian = "4";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"4");
                result.setText(Rupiah(hasil));
            }
        });
        lima.setOnClickListener(v->{
            counter ++;
            isian = "5";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"5");
                result.setText(Rupiah(hasil));
            }
        });
        enam.setOnClickListener(v->{
            counter ++;
            isian = "6";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"6");
                result.setText(Rupiah(hasil));
            }
        });
        tujuh.setOnClickListener(v->{
            counter ++;
            isian = "7";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"7");
                result.setText(Rupiah(hasil));
            }
        });
        delapan.setOnClickListener(v->{
            counter ++;
            isian = "8";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"8");
                result.setText(Rupiah(hasil));
            }
        });
        sembilan.setOnClickListener(v->{
            counter ++;
            isian = "9";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"9");
                result.setText(Rupiah(hasil));
            }
        });
        nol.setOnClickListener(v->{
            counter ++;
            isian = "0";
            if (counter == 1){
                int hasil = Integer.parseInt(isian);
                result.setText(Rupiah(hasil));
            }else {
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","")+"0");
                result.setText(Rupiah(hasil));
            }
        });
        titik.setOnClickListener(v->{

        });
        delete.setOnClickListener(v->{
            del ++;
            int nilai = counter-del;
            if (nilai == 0){
                result.setText(Rupiah(0));
            }else if(nilai < 0){
                result.setText(Rupiah(0));
            }else{
                String[] rp = result.getText().toString().split("Rp ");
                int hasil = Integer.parseInt(rp[1].replace(".","").substring(0,nilai));
                result.setText(Rupiah(hasil));
            }

        });
    }
    public String Rupiah(int angka){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(angka);
        return "Rp "+yourFormattedString.replace(",", ".");
    }
    public void finish(){
        counter=0;
    }
}
