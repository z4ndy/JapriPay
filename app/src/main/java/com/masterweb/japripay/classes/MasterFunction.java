package com.masterweb.japripay.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.masterweb.japripay.R;
import com.masterweb.japripay.activity.InvoiceActivity;
import com.masterweb.japripay.activity.auth.OtpActivity;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

public class MasterFunction {
    Activity mActivity;
    Context mcontext;
    static char[] NUMBERS = "0123456789".toCharArray();
    static Random rand = new SecureRandom();
    Dialogs dialogs;
    public MasterFunction(Context context, Activity activity){
        this.mcontext = context;
        this.mActivity = activity;
        dialogs = new Dialogs(mActivity);
    }
    public String cara_pasca(){
        String cara = "<h3 style='text-align:center'>Cara menghitung Tagihan</h3>" +
                "<p>Ketika anda melakukan cek tagihan pascabayar," +
                "maka cara menghitungnya seperti berikut :</p>" +
                "<p>Tahihan : Rp. 125.000<br>" +
                "Admin : Rp.1.000<br>" +
                "Komisi : Rp.750<br><br>" +
                "Rumus : Total Tagihan+Admin-Komisi<br>" +
                "Saldo Terpotong : <b>Rp. 125.250</b><br>" +
                "Tagihan ke customer : <i>Rp. 126.000</i></p>";
        return cara;
    }


    public  String get_phone(){
        Session session = new Session(mcontext);
        HashMap<String, String> user = session.getUserDetails();
        return user.get(Session.kunci_email);
    }
    public  void cekLogin(){
        Session session = new Session(mcontext);
        session.checkLogin();
    }
    public  void LogOut(){
        Session session = new Session(mcontext);
        session.logout();
    }

    public  void createSession(String username){
        Session session = new Session(mcontext);
        session.createSession(username);
    }

    public static String getOtp() {
        char[] password = new char[6];
        password[0] = NUMBERS[rand.nextInt(NUMBERS.length)];
        //populate rest of the password with random chars
        for (int i = 0; i < 6; i++) {
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
    public void hideButton(){
        try {
            InputMethodManager imm = (InputMethodManager)mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            //Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public boolean validate(EditText editText,String pesan) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        dialogs.showError(pesan);
        editText.requestFocus();
        return false;
    }
    public void errorMessage(String pesan){
        dialogs.showError(pesan);
    }
    public void successMessage(String pesan){
        dialogs.showSuccess(pesan);
    }

    public void move_otp(String phone){
        Intent intent = new Intent(mcontext, OtpActivity.class);
        intent.putExtra("phone",phone);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mcontext.startActivity(intent);
    }
    public void move_invoice(String key,String phone){
        Intent intent = new Intent(mcontext, InvoiceActivity.class);
        intent.putExtra("key",key);
        intent.putExtra("invoice",phone);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mcontext.startActivity(intent);
    }

    public String nomorHp(String nomor){
        String hasil;
        hasil = nomor.replace(" ","");
        if (!nomor.matches("/[^+0-9]/")){
            String no = nomor.substring(0,1);
            if(no.matches("0")){
                hasil = "62"+nomor.substring(1);
            }
            Log.d("nomor_hp", "nomorHp: "+no);
        }
        return hasil.trim();
    }
    public String Rupiah(int angka){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(angka);
        return yourFormattedString.replace(",", ".");
    }
    public String device(){
        String s = "";
        s += android.os.Build.VERSION.RELEASE;
        s += android.os.Build.BRAND;
        s += android.os.Build.HARDWARE;
        s += android.os.Build.ID;
        return s;
    }
    public String[] bank_name(){
        String[] name = {"Bank Central Asia - BCA","Bank Mandiri - MANDIRI","Bank Negara Indonesia - BNI","Bank Permata - PERMATA","Bank Rakyat Indonesia - BRI","CIMB Niaga Bank - CIMB_NIAGA","Bank Danamon - DANAMON","Bank Panin - PANIN","Bank Maybank - BII","Anglomas International Bank - ANGLOMAS","Bangkok Bank - BANGKOK","Bank IBK Indonesia - IBK","Sinarmas - SINARMAS","Bank BRI Agro - BRI_AGRO","Bank Andara - ANDARA","Bank CCB Indonesia - ANTAR_DAERAH","Bank ANZ Indonesia - ANZ","Bank Artha Graha International - ARTHA","Bank Artos Indonesia - ARTOS","Bank Bisnis Internasional - BISNIS","Bank BJB - BJB","Bank BNP Paribas - BNP","Bank Bukopin - BUKOPIN","Bank Bumi Arta - BUMI_ARTA","Bank Capital Indonesia - CAPITAL","Bank Central Asia (BCA) Syariah - BCA_SYR","Bank Chinatrust Indonesia - CHINATRUST","Bank CIMB Niaga UUS - CIMB_UUS","Bank Commonwealth - COMMONWEALTH","Bank Danamon UUS - DANAMON_UUS","Bank DBS Indonesia - DBS","Bank Dinar Indonesia - DINAR_INDONESIA","Bank OKE - OKE","Bank DKI - DKI","Bank DKI UUS - DKI_UUS","Bank Fama International - FAMA","Bank Ganesha - GANESHA","Bank Hana - HANA","Bank Harda Internasional - HARDA_INTERNASIONAL","Bank Woori Saudara - WOORI_SAUDARA","Bank ICBC Indonesia - ICBC","Bank Ina Perdana - INA_PERDANA","Bank Index Selindo - INDEX_SELINDO","Bank Jasa Jakarta - JASA_JAKARTA","Bank Kesejahteraan Ekonomi - KESEJAHTERAAN_EKONOMI","Bank Maspion Indonesia - MASPION","Bank Mayapada International - MAYAPADA","Bank Maybank Syariah Indonesia - MAYBANK_SYR","Bank Mayora - MAYORA","Bank Mega - MEGA","Bank Mestika Dharma - MESTIKA_DHARMA","Bank Shinhan Indonesia - SHINHAN","Bank Mizuho Indonesia - MIZUHO","Bank MNC Internasional - MNC_INTERNASIONAL","Bank Muamalat Indonesia - MUAMALAT","Bank Multi Arta Sentosa - MULTI_ARTA_SENTOSA","Bank J Trust Indonesia - J_TRUST","Bank Nationalnobu - NATIONALNOBU","Bank Nusantara Parahyangan - NUSANTARA_PARAHYANGAN","Bank OCBC NISP - OCBC","Bank OCBC NISP UUS - OCBC_UUS","Bank of America Merill-Lynch - BAML","Bank of China (BOC) - BOC","Bank of India Indonesia - INDIA","Bank of Tokyo Mitsubishi UFJ - TOKYO","Bank Panin Syariah - PANIN_SYR","Bank Permata UUS - PERMATA_UUS","Bank QNB Indonesia - QNB","Bank Rabobank International Indonesia - RABOBANK","Bank Resona Perdania - RESONA","Bank Royal Indonesia - ROYAL","Bank BTPN Syariah - BTPN_SYR","Bank Sahabat Sampoerna - SAHABAT_SAMPOERNA","Bank SBI Indonesia - SBI_INDONESIA","Bank Mandiri Taspen - MANDIRI_TASPEN","Bank Sumitomo Mitsui Indonesia - MITSUI","Bank Syariah Bukopin - BUKOPIN_SYR","Bank Syariah Mandiri - MANDIRI_SYR","Bank Syariah Mega - MEGA_SYR","Bank Tabungan Negara (BTN) - BTN","Bank Tabungan Negara (BTN) UUS - BTN_UUS","Bank Tabungan Pensiunan Nasional - TABUNGAN_PENSIUNAN_NASIONAL","Bank Tabungan Pensiunan Nasional UUS - TABUNGAN_PENSIUNAN_NASIONAL_UUS","Bank UOB Indonesia - UOB","Bank Victoria Internasional - VICTORIA_INTERNASIONAL","Bank Victoria Syariah - VICTORIA_SYR","Bank CCB Indonesia - CCB","Bank Woori Indonesia - WOORI","Bank Yudha Bhakti - YUDHA_BHAKTI","BPD Aceh - ACEH","BPD Aceh UUS - ACEH_UUS","BPD Banten - BANTEN","BPD Bali - BALI","BPD Bengkulu - BENGKULU","Bank Pembangunan Daerah (BPD DIY) - BPD_DIY","Bank Pembangunan Daerah (BPD DIY) Syariah - BPD_DIY_SYR","BPD Jambi - JAMBI","BPD Jambi UUS - JAMBI_UUS","BPD Jawa Tengah - JAWA_TENGAH","BPD Jawa Tengah UUS - JAWA_TENGAH_UUS","BPD Jawa Timur - JAWA_TIMUR","BPD Jawa Timur UUS - JAWA_TIMUR_UUS","BPD Kalimantan Barat - KALIMANTAN_BARAT","BPD Kalimantan Barat UUS - KALIMANTAN_BARAT_UUS","BPD Kalimantan Selatan - KALIMANTAN_SELATAN","BPD Kalimantan Selatan UUS - KALIMANTAN_SELATAN_UUS","BPD Kalimantan Tengah - KALIMANTAN_TENGAH","BPD Kalimantan Timur - KALIMANTAN_TIMUR","BPD Kalimantan Timur UUS - KALIMANTAN_TIMUR_UUS","BPD Lampung - LAMPUNG","BPD Maluku - MALUKU","BPD Nusa Tenggara Barat - NUSA_TENGGARA_BARAT","BPD Nusa Tenggara Barat UUS - NUSA_TENGGARA_BARAT_UUS","BPD Nusa Tenggara Timur - NUSA_TENGGARA_TIMUR","BPD Papua - PAPUA","BPD Riau Dan Kepri - RIAU_DAN_KEPRI","BPD Riau Dan Kepri UUS - RIAU_DAN_KEPRI_UUS","BPD Sulawesi Tengah - SULAWESI","BPD Sulawesi Tenggara - SULAWESI_TENGGARA","BPD Sulselbar - SULSELBAR","BPD Sulselbar UUS - SULSELBAR_UUS","Bank Sulutgo - SULUTGO","BPD Sumatera Barat - SUMATERA_BARAT","BPD Sumatera Barat UUS - SUMATERA_BARAT_UUS","BPD Sumsel Dan Babel - SUMSEL_DAN_BABEL","BPD Sumsel Dan Babel UUS - SUMSEL_DAN_BABEL_UUS","BPD Sumut - SUMUT","BPD Sumut UUS - SUMUT_UUS","Centratama Nasional Bank - CENTRATAMA","Citibank - CITIBANK","Deutsche Bank - DEUTSCHE","Hongkong and Shanghai Bank Corporation (HSBC) - HSBC","Hongkong and Shanghai Bank Corporation (HSBC) UUS - HSBC_UUS","JP Morgan Chase Bank - JPMORGAN","Prima Master Bank - PRIMA_MASTER","Standard Charted Bank - STANDARD_CHARTERED","Bank Mitra Niaga - MITRA_NIAGA","Bank Ekspor Indonesia - EKSPOR_INDONESIA","Bank Arta Niaga Kencana - ARTA_NIAGA_KENCANA","Bank BJB Syariah - BJB_SYR","BPR Karyajatnika Sadaya - BPR_KS","KSP SAHABAT MITRA SEJATI - SOBATKU","BPR Danagung Abadi - DANAGUNG_ABADI","BPR Danagung Bakti - DANAGUNG_BAKTI","BPR Danagung Ramulti - DANAGUNG_RAMULTI","Bank Syariah Indonesia - BSI","Bank International Indonesia - BII","Bank Agris - AGRIS","Bank Agroniaga - AGRONIAGA","Bank Himpunan Saudara 1906 - HIMPUNAN_SAUDARA","Bank Metro Express - METRO_EXPRESS","Bank Mutiara - MUTIARA","Bank QNB Kesawan - QNB_KESAWAN","Bank Sahabat Purba Danarta - SAHABAT_PURBA_DANARTA","Bank Sinar Harapan Bali - SINAR_HARAPAN_BALI","Bank Windu Kentjana Int - WINDU","BPD Sulut - SULUT","Bank Negara Indonesia 1946 (UUS) - BNI_UUS","Bank International Indonesia (UUS) - BII_UUS","Bank BJB (UUS) - BJB_UUS","Bank Eksekutif Internasional - PUNDI_INDONESIA","Bank ABN Amro - ABN_AMRO","Korea Exchange Bank Danamon - KEBD","Bank Barclays Indonesia - BARCLAYS","Bank OCBC - Indonesia - OCBC_INDONESIA"};
        return name;
    }
    public String nama_bank(String bank){
        String[] hasil = bank.split(" - ");
        return hasil[1];
    }

}
